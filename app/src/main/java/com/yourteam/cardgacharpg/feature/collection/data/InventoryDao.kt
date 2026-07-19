package com.yourteam.cardgacharpg.feature.collection.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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

    // Legt die Zeile fuer den itemType an, falls sie noch fehlt (amount = 0), ohne einen
    // bestehenden Bestand zu ueberschreiben (im Gegensatz zu upsert/REPLACE).
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfAbsent(entity: InventoryEntity)

    /**
     * Belohnungs-Gutschrift (z.B. XP-Traenke nach Kampagnen-Sieg, Person 4): stellt zuerst
     * sicher, dass die Zeile existiert, und addiert dann atomar. Noetig, weil adjustAmount()
     * als reines UPDATE ein No-Op waere, solange noch nie ein Bestand angelegt wurde.
     */
    @Transaction
    suspend fun addAmount(itemType: ItemType, delta: Int) {
        insertIfAbsent(InventoryEntity(itemType = itemType, amount = 0))
        adjustAmount(itemType, delta)
    }
}