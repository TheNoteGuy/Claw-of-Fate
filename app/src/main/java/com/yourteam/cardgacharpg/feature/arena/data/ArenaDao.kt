package com.yourteam.cardgacharpg.feature.arena.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// ⚠ PUBLIC CONTRACT — Owner: Person 5 (Robin).
@Dao
interface ArenaDao {

    @Query("SELECT * FROM arena_profile WHERE id = :id")
    fun getProfile(id: Int = ArenaProfileEntity.SINGLETON_ID): Flow<ArenaProfileEntity?>

    @Query("SELECT * FROM arena_profile WHERE id = :id")
    suspend fun get(id: Int = ArenaProfileEntity.SINGLETON_ID): ArenaProfileEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ensureRow(entity: ArenaProfileEntity = ArenaProfileEntity())

    @Query("UPDATE arena_profile SET trophies = MAX(0, trophies + :delta) WHERE id = :id")
    suspend fun updateTrophies(delta: Int, id: Int = ArenaProfileEntity.SINGLETON_ID)

    @Query("UPDATE arena_profile SET weeklyArenaCount = weeklyArenaCount + 1 WHERE id = :id")
    suspend fun incrementWeeklyCount(id: Int = ArenaProfileEntity.SINGLETON_ID)

    @Query("UPDATE arena_profile SET weeklyArenaCount = 0, lastRewardTimestamp = :timestamp WHERE id = :id")
    suspend fun resetWeeklyReward(timestamp: Long, id: Int = ArenaProfileEntity.SINGLETON_ID)
}