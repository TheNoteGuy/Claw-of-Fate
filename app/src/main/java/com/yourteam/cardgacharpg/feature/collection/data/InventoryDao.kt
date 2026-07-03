package com.yourteam.cardgacharpg.feature.collection.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Owner: Person 1 (Leila)
// Wird u.a. von LevelUpUseCase genutzt, um XP_POTION-Bestand zu prüfen/verringern.
@Dao
interface InventoryDao {

    @Query("SELECT * FROM inventory WHERE itemType = :itemType")
    fun getByType(itemType: ItemType): Flow<InventoryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: InventoryEntity)

    @Query("UPDATE inventory SET amount = amount + :delta WHERE itemType = :itemType")
    suspend fun adjustAmount(itemType: ItemType, delta: Int)
}