package com.example.hindilearn.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val id: Int = 1,
    val xp: Int = 0,
    val streak: Int = 0,
    val hearts: Int = 5,
    val coins: Int = 0,
    val unlockedNodeId: String = "episode_0_1",
    val selectedLanguage: String = "EN",
    val protagonistState: String = "Scared",
    val unlockedMemories: String = "", // comma separated
    val unlockedAchievements: String = "" // comma separated
)

@Entity(tableName = "srs_items")
data class SrsItemEntity(
    @PrimaryKey val wordId: String,
    val box: Int = 0,
    val nextReviewTime: Long = 0L
)

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE id = 1")
    fun getProgress(): UserProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProgress(progress: UserProgressEntity): Long
}

@Dao
interface SrsItemDao {
    @Query("SELECT * FROM srs_items WHERE nextReviewTime <= :currentTime")
    fun getDueItems(currentTime: Long): List<SrsItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveItem(item: SrsItemEntity): Long
}

@Database(entities = [UserProgressEntity::class, SrsItemEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProgressDao(): UserProgressDao
    abstract fun srsItemDao(): SrsItemDao
}
