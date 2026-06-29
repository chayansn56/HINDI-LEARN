package com.example.hindilearn.srs

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// ──────────────────────────────────────────────────────────────────────────────────
// Room Entity
// ──────────────────────────────────────────────────────────────────────────────────

/**
 * Room database entity representing a single SRS flashcard.
 *
 * This entity maps directly to the "srs_cards" table and stores all fields
 * needed for the SM-2 spaced repetition algorithm. Convert to/from the
 * domain model [SrsCard] using the provided extension functions.
 */
@Entity(tableName = "srs_cards")
data class SrsCardEntity(
    @PrimaryKey
    val cardId: String,
    val hindiWord: String,
    val englishMeaning: String,
    val vietnameseMeaning: String,
    val transliteration: String,
    val skillType: String,
    val easeFactor: Float = 2.5f,
    val interval: Int = 0,
    val repetitions: Int = 0,
    val nextReviewDate: Long = System.currentTimeMillis(),
    val lastReviewDate: Long? = null,
    val quality: Int? = null
)

// ──────────────────────────────────────────────────────────────────────────────────
// Entity ↔ Domain Model Mappers
// ──────────────────────────────────────────────────────────────────────────────────

/** Convert a Room entity to the domain [SrsCard] model. */
fun SrsCardEntity.toDomainModel(): SrsCard = SrsCard(
    cardId = cardId,
    hindiWord = hindiWord,
    englishMeaning = englishMeaning,
    vietnameseMeaning = vietnameseMeaning,
    transliteration = transliteration,
    skillType = skillType,
    easeFactor = easeFactor,
    interval = interval,
    repetitions = repetitions,
    nextReviewDate = nextReviewDate,
    lastReviewDate = lastReviewDate,
    quality = quality
)

/** Convert a domain [SrsCard] to the Room entity for persistence. */
fun SrsCard.toEntity(): SrsCardEntity = SrsCardEntity(
    cardId = cardId,
    hindiWord = hindiWord,
    englishMeaning = englishMeaning,
    vietnameseMeaning = vietnameseMeaning,
    transliteration = transliteration,
    skillType = skillType,
    easeFactor = easeFactor,
    interval = interval,
    repetitions = repetitions,
    nextReviewDate = nextReviewDate,
    lastReviewDate = lastReviewDate,
    quality = quality
)

// ──────────────────────────────────────────────────────────────────────────────────
// Stats Data Class
// ──────────────────────────────────────────────────────────────────────────────────

/**
 * Aggregated statistics for the SRS system, exposed via DAO queries.
 *
 * @property totalCards Total number of cards in the collection.
 * @property cardsDueToday Number of cards due for review right now.
 * @property cardsNew Number of cards never reviewed (new).
 * @property cardsLearning Number of cards being actively learned (rep < 3, reviewed).
 * @property cardsMastered Number of well-established cards (interval >= 21, ease >= 2.5).
 * @property cardsBySkill Map of skill type → card count (e.g., {"vocabulary": 150, "grammar": 30}).
 */
data class SrsStats(
    val totalCards: Int = 0,
    val cardsDueToday: Int = 0,
    val cardsNew: Int = 0,
    val cardsLearning: Int = 0,
    val cardsMastered: Int = 0,
    val cardsBySkill: Map<String, Int> = emptyMap()
)

// ──────────────────────────────────────────────────────────────────────────────────
// DAO (Data Access Object)
// ──────────────────────────────────────────────────────────────────────────────────

/**
 * Data Access Object for SRS card CRUD operations and statistical queries.
 *
 * All queries return [Flow] for reactive UI updates via Room's
 * invalidation-based observation mechanism.
 */
@Dao
interface SrsCardDao {

    // ── Insert / Update / Delete ──────────────────────────────────────────────

    /**
     * Insert a new card. If a card with the same [cardId] already exists,
     * it will be replaced (upsert behavior).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCard(card: SrsCardEntity): Long

    /**
     * Insert multiple cards in a single transaction.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCards(cards: List<SrsCardEntity>): List<Long>

    /**
     * Update an existing card. The card is matched by its primary key [cardId].
     */
    @Update
    fun updateCard(card: SrsCardEntity): Int

    /**
     * Delete a card by its entity reference.
     */
    @Delete
    fun deleteCard(card: SrsCardEntity): Int

    /**
     * Delete a card by its ID.
     */
    @Query("DELETE FROM srs_cards WHERE cardId = :cardId")
    fun deleteCardById(cardId: String): Int

    // ── Read Operations ───────────────────────────────────────────────────────

    /**
     * Get a single card by its unique ID.
     * Returns null if no card with the given ID exists.
     */
    @Query("SELECT * FROM srs_cards WHERE cardId = :cardId LIMIT 1")
    fun getCardById(cardId: String): SrsCardEntity?

    /**
     * Observe a single card reactively.
     */
    @Query("SELECT * FROM srs_cards WHERE cardId = :cardId LIMIT 1")
    fun observeCardById(cardId: String): Flow<SrsCardEntity?>

    /**
     * Get all cards in the collection, sorted by next review date.
     */
    @Query("SELECT * FROM srs_cards ORDER BY nextReviewDate ASC")
    fun getAllCards(): Flow<List<SrsCardEntity>>

    /**
     * Get all cards as a one-shot list (not a Flow).
     */
    @Query("SELECT * FROM srs_cards ORDER BY nextReviewDate ASC")
    fun getAllCardsOnce(): List<SrsCardEntity>

    // ── Due Cards ─────────────────────────────────────────────────────────────

    /**
     * Get cards that are due for review (nextReviewDate <= current time).
     * Results are observed reactively and sorted by most overdue first.
     *
     * @param limit Maximum number of cards to return.
     */
    @Query("""
        SELECT * FROM srs_cards 
        WHERE nextReviewDate <= :now 
        ORDER BY nextReviewDate ASC 
        LIMIT :limit
    """)
    fun getDueCards(now: Long = System.currentTimeMillis(), limit: Int = 20): Flow<List<SrsCardEntity>>

    /**
     * Get due cards as a one-shot list.
     */
    @Query("""
        SELECT * FROM srs_cards 
        WHERE nextReviewDate <= :now 
        ORDER BY nextReviewDate ASC 
        LIMIT :limit
    """)
    fun getDueCardsOnce(now: Long = System.currentTimeMillis(), limit: Int = 20): List<SrsCardEntity>

    // ── Filtered by Skill Type ────────────────────────────────────────────────

    /**
     * Get all cards of a specific skill type.
     */
    @Query("SELECT * FROM srs_cards WHERE skillType = :skillType ORDER BY nextReviewDate ASC")
    fun getCardsBySkillType(skillType: String): Flow<List<SrsCardEntity>>

    /**
     * Get due cards filtered by skill type.
     */
    @Query("""
        SELECT * FROM srs_cards 
        WHERE nextReviewDate <= :now AND skillType = :skillType 
        ORDER BY nextReviewDate ASC 
        LIMIT :limit
    """)
    fun getDueCardsBySkillType(
        skillType: String,
        now: Long = System.currentTimeMillis(),
        limit: Int = 20
    ): List<SrsCardEntity>

    // ── Statistics ────────────────────────────────────────────────────────────

    /** Total number of cards in the collection. */
    @Query("SELECT COUNT(*) FROM srs_cards")
    fun getTotalCardCount(): Flow<Int>

    /** Number of cards due right now. */
    @Query("SELECT COUNT(*) FROM srs_cards WHERE nextReviewDate <= :now")
    fun getDueCardCount(now: Long = System.currentTimeMillis()): Flow<Int>

    /** Number of new cards (never reviewed). */
    @Query("SELECT COUNT(*) FROM srs_cards WHERE lastReviewDate IS NULL AND repetitions = 0")
    fun getNewCardCount(): Flow<Int>

    /** Number of cards being actively learned (reviewed but rep < 3). */
    @Query("SELECT COUNT(*) FROM srs_cards WHERE repetitions > 0 AND repetitions < 3")
    fun getLearningCardCount(): Flow<Int>

    /** Number of mastered cards (interval >= 21 days AND ease factor >= 2.5). */
    @Query("SELECT COUNT(*) FROM srs_cards WHERE interval >= 21 AND easeFactor >= 2.5")
    fun getMasteredCardCount(): Flow<Int>

    /**
     * Card count grouped by skill type.
     * Returns a list of [SkillCount] pairs.
     */
    @Query("SELECT skillType, COUNT(*) as count FROM srs_cards GROUP BY skillType")
    fun getCardCountsBySkill(): Flow<List<SkillCount>>

    /** Count of cards for a specific skill type. */
    @Query("SELECT COUNT(*) FROM srs_cards WHERE skillType = :skillType")
    fun getCardCountForSkill(skillType: String): Int

    /** Count of due cards for a specific skill type. */
    @Query("SELECT COUNT(*) FROM srs_cards WHERE skillType = :skillType AND nextReviewDate <= :now")
    fun getDueCountForSkill(skillType: String, now: Long = System.currentTimeMillis()): Int

    // ── Bulk Operations ──────────────────────────────────────────────────────

    /** Delete all cards. Use with caution! */
    @Query("DELETE FROM srs_cards")
    fun deleteAllCards(): Int
}

/**
 * Simple data class for holding skill type → count query results.
 * Room automatically maps the two columns.
 */
data class SkillCount(
    val skillType: String,
    val count: Int
)

// ──────────────────────────────────────────────────────────────────────────────────
// Room Database
// ──────────────────────────────────────────────────────────────────────────────────

/**
 * Room database for the Spaced Repetition System.
 *
 * Provides access to [SrsCardDao] for all SRS card operations.
 * Use [SrsDatabase.getInstance] to obtain a singleton instance.
 *
 * ### Migration Strategy
 * When the schema changes, increment [version] and add a [Migration].
 * For development, [fallbackToDestructiveMigration] is used.
 *
 * @param version Current database schema version.
 */
@Database(
    entities = [SrsCardEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SrsDatabase : RoomDatabase() {

    /** DAO for SRS card operations. */
    abstract fun srsCardDao(): SrsCardDao

    companion object {
        @Volatile
        private var INSTANCE: SrsDatabase? = null

        fun getInstance(context: android.content.Context): SrsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    SrsDatabase::class.java,
                    "srs-database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────────
// Stats Computation Helper
// ──────────────────────────────────────────────────────────────────────────────────

/**
 * Computes aggregated [SrsStats] from individual DAO Flow sources.
 *
 * This function combines multiple Room Flow queries into a single Flow<SrsStats>
 * by collecting all source flows and recomputing on each emission.
 *
 * Usage:
 * ```kotlin
 * val statsFlow = combine(
 *     dao.getTotalCardCount(),
 *     dao.getDueCardCount(),
 *     dao.getNewCardCount(),
 *     dao.getLearningCardCount(),
 *     dao.getMasteredCardCount(),
 *     dao.getCardCountsBySkill()
 * ) { total, due, newCount, learning, mastered, bySkill ->
 *     SrsStats(total, due, newCount, learning, mastered, bySkill.associate { it.skillType to it.count })
 * }
 * ```
 */
// Note: The combine function should be called in a ViewModel or Repository layer
// using kotlinx.coroutines.flow.combine. The import is left to the caller.