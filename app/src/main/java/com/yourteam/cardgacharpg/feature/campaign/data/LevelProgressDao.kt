package com.yourteam.cardgacharpg.feature.campaign.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// ⚠ PUBLIC CONTRACT — Owner: Person 4 (Yassin). Consumed by Person 5
@Dao
interface LevelProgressDao {
    // 1. Befehl: Alle Level abrufen
    // 'Flow' ist wie eine Live-Kamera. Wenn sich in der Datenbank etwas ändert,
    // sagt der Flow dem Bildschirm automatisch Bescheid, dass er sich neu zeichnen soll!
    @Query("SELECT * FROM level_progress ORDER BY levelId ASC")
    fun getAllLevelProgress(): Flow<List<LevelProgressEntity>>

    // 2. Befehl: Ein einzelnes Level abrufen (z.B. um nachzuschauen, ob Level 5 schon offen ist)
    @Query("SELECT * FROM level_progress WHERE levelId = :levelId")
    suspend fun getLevelById(levelId: Int): LevelProgressEntity?

    // 3. Befehl: Die ersten 10 Level beim App-Start in die leere Tabelle einfügen
    // IGNORE bedeutet: Wenn sie schon da sind, mach nichts (sonst überschreiben wir den Fortschritt!)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInitialLevels(levels: List<LevelProgressEntity>)

    // 4. Befehl: Ein Level updaten (z.B. isUnlocked auf true setzen oder Sterne ändern)
    @Update
    suspend fun updateLevel(level: LevelProgressEntity)
}
