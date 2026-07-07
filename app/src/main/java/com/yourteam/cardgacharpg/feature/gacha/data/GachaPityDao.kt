package com.yourteam.cardgacharpg.feature.gacha.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Owner: Person 2 (Nico) — get(), update(count)
@Dao
interface GachaPityDao {

    // Flow, damit die UI live auf den Pity-Stand reagieren kann.
    @Query("SELECT * FROM gacha_pity WHERE id = :id")
    fun observe(id: Int = GachaPityEntity.SINGLETON_ID): Flow<GachaPityEntity?>

    // Einmaliger Lesezugriff (z.B. direkt vor einem Pull).
    @Query("SELECT * FROM gacha_pity WHERE id = :id")
    suspend fun get(id: Int = GachaPityEntity.SINGLETON_ID): GachaPityEntity?

    // Stellt sicher, dass die Singleton-Zeile existiert (beim ersten App-Start aufrufen).
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ensureRow(entity: GachaPityEntity = GachaPityEntity())

    @Query("UPDATE gacha_pity SET pityCount = :count, lastPullTimestamp = :timestamp WHERE id = :id")
    suspend fun update(count: Int, timestamp: Long, id: Int = GachaPityEntity.SINGLETON_ID)
}