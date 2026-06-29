package com.example.hindilearn.srs

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SrsEngineTest {

    @Test
    fun testCalculateNextReview_NewCard_PerfectRecall() {
        val newCard = SrsCard(
            cardId = "test_1",
            hindiWord = "नमस्ते",
            englishMeaning = "Hello",
            vietnameseMeaning = "Xin chào",
            transliteration = "namaste",
            skillType = "vocabulary"
        )

        // Quality 5 (Perfect Recall)
        val updatedCard = SrsEngine.calculateNextReview(newCard, 5)

        assertEquals(1, updatedCard.repetitions)
        assertEquals(1, updatedCard.interval) // 1st rep is 1 day
        assertEquals(2.6f, updatedCard.easeFactor, 0.01f) // 2.5 + 0.1
        assertNotNull(updatedCard.lastReviewDate)
    }

    @Test
    fun testCalculateNextReview_SubsequentRepetitions() {
        val baseCard = SrsCard(
            cardId = "test_2",
            hindiWord = "धन्यवाद",
            englishMeaning = "Thank you",
            vietnameseMeaning = "Cảm ơn",
            transliteration = "dhanyavaad",
            skillType = "vocabulary",
            easeFactor = 2.5f,
            interval = 1,
            repetitions = 1
        )

        // 2nd successful repetition: interval should jump to 6 days
        val secondRep = SrsEngine.calculateNextReview(baseCard, 4)
        assertEquals(2, secondRep.repetitions)
        assertEquals(6, secondRep.interval)

        // 3rd successful repetition: interval should grow by ease factor
        val thirdRep = SrsEngine.calculateNextReview(secondRep, 4)
        assertEquals(3, thirdRep.repetitions)
        assertEquals((6 * thirdRep.easeFactor).toInt(), thirdRep.interval)
    }

    @Test
    fun testCalculateNextReview_FailedRecall() {
        val baseCard = SrsCard(
            cardId = "test_3",
            hindiWord = "हाँ",
            englishMeaning = "Yes",
            vietnameseMeaning = "Vâng",
            transliteration = "haan",
            skillType = "vocabulary",
            easeFactor = 2.4f,
            interval = 15,
            repetitions = 4
        )

        // Quality 1 (Incorrect, recognized) -> Failure
        val failedCard = SrsEngine.calculateNextReview(baseCard, 1)

        assertEquals(0, failedCard.repetitions)
        assertEquals(1, failedCard.interval) // reset to 1 day
        assertTrue(failedCard.easeFactor < 2.4f) // ease factor decreases
    }

    @Test
    fun testCalculateNextReview_EaseFactorMinimumBound() {
        val lowEaseCard = SrsCard(
            cardId = "test_4",
            hindiWord = "नहीं",
            englishMeaning = "No",
            vietnameseMeaning = "Không",
            transliteration = "nahin",
            skillType = "vocabulary",
            easeFactor = 1.35f,
            interval = 2,
            repetitions = 2
        )

        // Quality 0 (Complete failure) -> should drop ease factor below min but keep clamped
        val updatedCard = SrsEngine.calculateNextReview(lowEaseCard, 0)
        assertEquals(1.3f, updatedCard.easeFactor, 0.001f) // clamped to MIN_EASE_FACTOR
    }

    @Test
    fun testGetCardStatus() {
        val newCard = SrsCard("c1", "w1", "m1", "m2", "t1", "vocabulary")
        assertEquals(CardStatus.NEW, SrsEngine.getCardStatus(newCard))

        val learningCard = SrsCard("c2", "w1", "m1", "m2", "t1", "vocabulary", repetitions = 1, lastReviewDate = System.currentTimeMillis())
        assertEquals(CardStatus.LEARNING, SrsEngine.getCardStatus(learningCard))

        val reviewCard = SrsCard("c3", "w1", "m1", "m2", "t1", "vocabulary", repetitions = 3, interval = 5, lastReviewDate = System.currentTimeMillis())
        assertEquals(CardStatus.REVIEW, SrsEngine.getCardStatus(reviewCard))

        val masteredCard = SrsCard("c4", "w1", "m1", "m2", "t1", "vocabulary", repetitions = 5, interval = 22, easeFactor = 2.6f, lastReviewDate = System.currentTimeMillis())
        assertEquals(CardStatus.MASTERED, SrsEngine.getCardStatus(masteredCard))
    }
}
