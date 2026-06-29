package com.example.hindilearn.srs

import kotlin.math.floor
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Spaced Repetition System Engine implementing the SM-2 algorithm variant.
 *
 * ## SM-2 Algorithm Overview
 * The SM-2 algorithm, developed by Piotr Woźniak (SuperMemo), schedules flashcard reviews
 * at optimally increasing intervals based on the learner's recall quality.
 *
 * ### Core Concepts:
 * - **Ease Factor (EF)**: A multiplier (>= 1.3) that adjusts interval growth. Higher EF
 *   means easier cards are reviewed less frequently. Starts at 2.5.
 * - **Interval (days)**: Number of days until the next review. Grows exponentially
 *   with successful recalls.
 * - **Repetitions**: Count of consecutive successful reviews (quality >= 3).
 * - **Quality (0-5)**: User's self-assessment of recall difficulty:
 *   0 = Complete blackout, 1 = Incorrect but recognized, 2 = Incorrect but easy to recall,
 *   3 = Correct with serious difficulty, 4 = Correct with hesitation, 5 = Perfect recall
 *
 * ### Scheduling Logic:
 * - Quality 0-2 (failure): Reset to repetition 0, interval 1 day (start over)
 * - Quality 3 (hard): Keep repetition count, multiply interval by ease factor
 * - Quality 4-5 (good/easy): Increment repetition, use graduated intervals:
 *   - 1st repetition → 1 day
 *   - 2nd repetition → 6 days
 *   - 3rd+ repetition → previous interval × ease factor
 *
 * ### Ease Factor Update Formula:
 * EF' = max(1.3, EF + 0.1 - (5 - quality) × (0.08 + (5 - quality) × 0.02))
 * This gradually decreases EF for poor recalls and increases it for easy recalls.
 */
object SrsEngine {

    // ──────────────────────────────────────────────────────────────────────────────
    // SM-2 Constants
    // ──────────────────────────────────────────────────────────────────────────────

    /** Minimum ease factor allowed — prevents cards from being scheduled too frequently. */
    const val MIN_EASE_FACTOR = 1.3f

    /** Default ease factor for new cards. */
    const val DEFAULT_EASE_FACTOR = 2.5f

    /** Quality rating threshold below which the card is considered "failed". */
    private const val FAILURE_THRESHOLD = 3

    /** First interval after a failed card is reviewed successfully (1 day). */
    private const val INTERVAL_FIRST_REP = 1

    /** Second interval after two consecutive successes (6 days). */
    private const val INTERVAL_SECOND_REP = 6

    /** Interval to assign when a card fails (reset to 1 day). */
    private const val INTERVAL_AFTER_FAILURE = 1

    // ──────────────────────────────────────────────────────────────────────────────
    // Core SM-2 Algorithm
    // ──────────────────────────────────────────────────────────────────────────────

    /**
     * Calculates the next review state for a card using the SM-2 algorithm.
     *
     * This is the heart of the spaced repetition system. Given a card's current
     * scheduling state and the user's quality rating (0-5), it computes:
     * - Updated ease factor
     * - Updated repetition count
     * - Next interval in days
     * - Next review timestamp (epoch millis)
     *
     * @param card The current state of the SRS card.
     * @param quality The user's recall quality rating (0-5).
     *   0 = Complete failure, 5 = Perfect recall.
     * @return A new [SrsCard] with updated scheduling fields. The cardId and content
     *   fields remain unchanged.
     *
     * @throws IllegalArgumentException if quality is not in range 0..5.
     */
    fun calculateNextReview(card: SrsCard, quality: Int): SrsCard {
        require(quality in 0..5) {
            "Quality must be between 0 and 5, got $quality"
        }

        val now = System.currentTimeMillis()
        val newRepetitions: Int
        val newInterval: Int

        when {
            // ── Quality 0, 1, 2: FAILURE — The learner could not recall the answer
            // satisfactorily. Reset repetitions and set interval to 1 day to re-learn
            // the card from scratch.
            quality < FAILURE_THRESHOLD -> {
                newRepetitions = 0
                newInterval = INTERVAL_AFTER_FAILURE
            }

            // ── Quality 3: HARD recall — Correct but with serious difficulty.
            // Keep the repetition count the same (don't promote) and extend the interval
            // by multiplying with the ease factor. This gives more time but doesn't
            // reward the learner as much as quality 4-5.
            quality == 3 -> {
                newRepetitions = card.repetitions
                // If the card has never been reviewed successfully (interval 0),
                // give it an initial interval of 1 day instead of staying at 0.
                newInterval = if (card.interval == 0) {
                    INTERVAL_FIRST_REP
                } else {
                    (card.interval * card.easeFactor).roundToInt()
                }
            }

            // ── Quality 4, 5: GOOD to PERFECT recall — The learner answered correctly
            // with little to no hesitation. Increment repetitions and apply graduated
            // interval scheduling.
            else -> {
                newRepetitions = card.repetitions + 1
                newInterval = when (newRepetitions) {
                    // First successful repetition: review again in 1 day
                    1 -> INTERVAL_FIRST_REP
                    // Second successful repetition: jump to 6 days
                    2 -> INTERVAL_SECOND_REP
                    // Third+ repetition: exponential growth using ease factor
                    else -> (card.interval * card.easeFactor).roundToInt()
                }
            }
        }

        // ── Ease Factor Update ──────────────────────────────────────────────────
        // The ease factor adjusts based on the quality of recall.
        // For low quality (0-1), the factor decreases significantly.
        // For high quality (4-5), the factor increases slightly.
        // The formula: EF' = max(1.3, EF + 0.1 - (5-q)*(0.08 + (5-q)*0.02))
        //
        // Breakdown for quality=5 (perfect): EF' = EF + 0.1 - 0 = EF + 0.1
        // For quality=4 (easy):             EF' = EF + 0.1 - 0.10 = EF + 0.0
        // For quality=3 (hard):             EF' = EF + 0.1 - 0.22 = EF - 0.12
        // For quality=2 (difficult):        EF' = EF + 0.1 - 0.36 = EF - 0.26
        // For quality=1 (very hard):        EF' = EF + 0.1 - 0.52 = EF - 0.42
        // For quality=0 (blackout):         EF' = EF + 0.1 - 0.70 = EF - 0.60
        val qualityDelta = 5 - quality
        val newEaseFactor = max(
            MIN_EASE_FACTOR,
            card.easeFactor + 0.1f - qualityDelta * (0.08f + qualityDelta * 0.02f)
        )

        // Calculate next review timestamp: current time + interval in days
        val intervalMillis = newInterval * 24L * 60L * 60L * 1000L
        val nextReviewDate = now + intervalMillis

        return card.copy(
            easeFactor = newEaseFactor,
            interval = newInterval,
            repetitions = newRepetitions,
            nextReviewDate = nextReviewDate,
            lastReviewDate = now,
            quality = quality
        )
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // Card Filtering
    // ──────────────────────────────────────────────────────────────────────────────

    /**
     * Returns cards that are due for review (nextReviewDate <= now).
     *
     * Cards are sorted by most overdue first (earliest nextReviewDate).
     * A [limit] parameter caps the number of cards returned, preventing
     * overwhelming the learner with too many reviews at once.
     *
     * @param allCards All cards in the user's collection.
     * @param limit Maximum number of due cards to return (default 20).
     * @return A list of due cards, sorted by urgency (most overdue first).
     */
    fun getDueCards(allCards: List<SrsCard>, limit: Int = 20): List<SrsCard> {
        val now = System.currentTimeMillis()
        return allCards
            .filter { it.nextReviewDate <= now }
            .sortedBy { it.nextReviewDate } // Most overdue first
            .take(limit)
    }

    /**
     * Returns due cards, optionally filtered by skill type.
     *
     * This is useful when the learner wants to focus on a specific skill
     * (e.g., vocabulary, grammar, pronunciation, culture) during a review session.
     *
     * @param allCards All cards in the user's collection.
     * @param skillType Optional skill type filter (e.g., "vocabulary", "grammar").
     *   If null, returns all due cards regardless of skill type.
     * @return A list of due cards matching the filter criteria, sorted by urgency.
     */
    fun getCardsForReview(
        allCards: List<SrsCard>,
        skillType: String? = null
    ): List<SrsCard> {
        val now = System.currentTimeMillis()
        return allCards
            .asSequence()
            .filter { it.nextReviewDate <= now }
            .let { sequence ->
                if (skillType != null) {
                    sequence.filter { it.skillType.equals(skillType, ignoreCase = true) }
                } else {
                    sequence
                }
            }
            .sortedBy { it.nextReviewDate }
            .toList()
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // Card Status Classification
    // ──────────────────────────────────────────────────────────────────────────────

    /**
     * Classifies a card's learning status based on its SRS state.
     *
     * - **NEW**: Never been reviewed (repetitions == 0, lastReviewDate == null)
     * - **LEARNING**: Being actively learned (repetitions < 3 and has been reviewed)
     * - **REVIEW**: Known but needs periodic review (repetitions >= 3)
     * - **MASTERED**: Long interval with high ease factor (interval >= 21 days and easeFactor >= 2.5)
     */
    fun getCardStatus(card: SrsCard): CardStatus {
        return when {
            card.lastReviewDate == null && card.repetitions == 0 -> CardStatus.NEW
            card.repetitions < 3 -> CardStatus.LEARNING
            card.interval >= 21 && card.easeFactor >= 2.5f -> CardStatus.MASTERED
            else -> CardStatus.REVIEW
        }
    }

    /**
     * Returns the optimal retention rate for a card based on its ease factor.
     * Higher ease factor → higher predicted retention.
     */
    fun getPredictedRetention(card: SrsCard): Float {
        // Simplified retention model: maps ease factor (1.3-3.0) to retention (70%-98%)
        return when {
            card.easeFactor <= MIN_EASE_FACTOR -> 0.70f
            card.easeFactor >= 3.0f -> 0.98f
            else -> 0.70f + (card.easeFactor - MIN_EASE_FACTOR) / (3.0f - MIN_EASE_FACTOR) * 0.28f
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────────
// Data Classes
// ──────────────────────────────────────────────────────────────────────────────────

/**
 * Represents a single flashcard in the Spaced Repetition System.
 *
 * This is a pure domain model (not a Room entity). Use [SrsCardEntity] for
 * database persistence and map between the two as needed.
 *
 * @property cardId Unique identifier for this card.
 * @property hindiWord The Hindi word/phrase (in Devanagari script).
 * @property englishMeaning English translation or meaning.
 * @property vietnameseMeaning Vietnamese translation (for Vietnamese speakers).
 * @property transliteration Romanized transliteration of the Hindi word.
 * @property skillType Category of this card: "vocabulary", "grammar", "pronunciation", "culture".
 * @property easeFactor SM-2 ease factor (>= 1.3, default 2.5). Controls interval growth rate.
 * @property interval Days until next review.
 * @property repetitions Number of consecutive successful reviews (quality >= 3).
 * @property nextReviewDate Epoch millis timestamp of when this card should be reviewed next.
 * @property lastReviewDate Epoch millis of the last review, or null if never reviewed.
 * @property quality Last quality rating (0-5), or null if never rated.
 */
data class SrsCard(
    val cardId: String,
    val hindiWord: String,
    val englishMeaning: String,
    val vietnameseMeaning: String,
    val transliteration: String,
    val skillType: String,
    val easeFactor: Float = SrsEngine.DEFAULT_EASE_FACTOR,
    val interval: Int = 0,
    val repetitions: Int = 0,
    val nextReviewDate: Long = System.currentTimeMillis(),
    val lastReviewDate: Long? = null,
    val quality: Int? = null
)

/**
 * Classification of a card's learning state.
 */
enum class CardStatus {
    /** Card has never been reviewed. */
    NEW,
    /** Card is being actively learned (few successful repetitions). */
    LEARNING,
    /** Card is in the review phase (known but needs periodic reinforcement). */
    REVIEW,
    /** Card is well-established with long intervals. */
    MASTERED
}