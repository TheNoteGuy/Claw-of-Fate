package com.yourteam.cardgacharpg.feature.battle.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// ⚠ PUBLIC CONTRACT — Owner: Person 3 (Marc). Consumed by Person 4 & Person 5
@Dao
interface FormationDao {

    // Flow, damit UI + Kampf-Trigger (Person 4/5) live auf Änderungen reagieren können.
    @Query("SELECT * FROM formation WHERE id = :id")
    fun observe(id: Int = FormationEntity.SINGLETON_ID): Flow<FormationEntity?>

    @Query("SELECT * FROM formation WHERE id = :id")
    suspend fun get(id: Int = FormationEntity.SINGLETON_ID): FormationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(formation: FormationEntity)

    // Stellt sicher, dass die Singleton-Zeile existiert (beim ersten App-Start aufrufen).
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ensureRow(entity: FormationEntity = FormationEntity())
}
