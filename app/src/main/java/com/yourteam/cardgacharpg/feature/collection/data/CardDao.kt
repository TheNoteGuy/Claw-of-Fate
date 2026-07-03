package com.yourteam.cardgacharpg.feature.collection.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yourteam.cardgacharpg.core.model.Element
import com.yourteam.cardgacharpg.core.model.Rarity
import kotlinx.coroutines.flow.Flow

// Owner: Person 1 (Leila) — getAll(), getByElement(), getByRarity(), getById(), update()
// ⚠ insertAll() ist Teil des Public Contracts -> wird von Person 2 nach jedem Gacha-Pull aufgerufen
@Dao
interface CardDao {

    @Query("SELECT * FROM cards ORDER BY level DESC, id ASC")
    fun getAll(): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE element = :element")
    fun getByElement(element: Element): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE rarity = :rarity")
    fun getByRarity(rarity: Rarity): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE id = :id")
    fun getById(id: Int): Flow<CardEntity?>

    @Update
    suspend fun update(card: CardEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(card: CardEntity): Long

    // Bulk-Insert nach Gacha-Pull — Schnittstellenvertrag mit Person 2

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(cards: List<CardEntity>): List<Long>
}