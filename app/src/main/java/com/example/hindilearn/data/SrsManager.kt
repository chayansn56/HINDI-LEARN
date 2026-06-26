package com.example.hindilearn.data

import android.content.Context

object SrsManager {
    private var db: AppDatabase? = null

    fun init(context: Context) {
        db = androidx.room.Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "hindi-learn-db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    // Standard Leitner system intervals in days
    private val boxIntervals = mapOf(
        0 to 0L,  // 0 days (due immediately)
        1 to 1L,  // 1 day
        2 to 3L,  // 3 days
        3 to 7L,  // 7 days
        4 to 14L, // 14 days
        5 to 30L  // 30 days
    )

    fun getDueItems(): List<String> {
        val currentTime = System.currentTimeMillis()
        val items = db?.srsItemDao()?.getDueItems(currentTime) ?: emptyList()
        return items.map { it.wordId }
    }

    fun processReview(wordId: String, correct: Boolean) {
        val currentTime = System.currentTimeMillis()
        var entity = db?.srsItemDao()?.getDueItems(Long.MAX_VALUE)?.find { it.wordId == wordId }
        
        var currentBox = entity?.box ?: 0
        
        if (correct) {
            currentBox++
            if (currentBox > 5) currentBox = 5 // Max box 5
        } else {
            currentBox = 1 // Reset to box 1 if incorrect
        }

        val daysToAdd = boxIntervals[currentBox] ?: 1L
        val nextReviewTime = currentTime + (daysToAdd * 24 * 60 * 60 * 1000)

        db?.srsItemDao()?.saveItem(
            SrsItemEntity(
                wordId = wordId,
                box = currentBox,
                nextReviewTime = nextReviewTime
            )
        )
    }

    fun addWordIfNotExists(wordId: String) {
        val exists = db?.srsItemDao()?.getDueItems(Long.MAX_VALUE)?.any { it.wordId == wordId } ?: false
        if (!exists) {
            db?.srsItemDao()?.saveItem(
                SrsItemEntity(
                    wordId = wordId,
                    box = 0,
                    nextReviewTime = 0L // due immediately
                )
            )
        }
    }
}
