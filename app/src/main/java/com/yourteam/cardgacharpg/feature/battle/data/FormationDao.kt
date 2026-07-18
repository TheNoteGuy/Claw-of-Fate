package com.yourteam.cardgacharpg.feature.battle.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yourteam.cardgacharpg.core.model.Card
import kotlinx.coroutines.flow.Flow

// ⚠ PUBLIC CONTRACT — Owner: Person 3 (Marc). Consumed by Person 4 & Person 5
@Dao
interface FormationDao {
    @Query("SELECT * FROM formations WHERE id = :id")
    fun get(id: Long): Flow<FormationEntity>

    @Query("SELECT * FROM formations JOIN active_formation ON formations.id = active_formation.id")
    fun get(): Flow<FormationEntity>

    @Query("SELECT * FROM formations ORDER BY id ASC")
    fun getAll(): Flow<List<FormationEntity>>

    @Query("UPDATE formations SET name = :name WHERE id = :id")
    suspend fun setName(id: Long, name: String)

    /*@Query("SELECT cards.* FROM cards JOIN formations ON formations.slot0 = cards.id JOIN formations ON formations.slot1 = cards.id JOIN formations ON formations.slot2 = cards.id JOIN formations ON formations.slot3 = cards.id JOIN formations ON formations.slot4 = cards.id JOIN formations ON formations.slot5 = cards.id JOIN active_formation ON formations.id = active_formation.id")
    fun getCards(): Flow<List<Card>>*/

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create(formationEntity: FormationEntity): Long

/*    @Query("UPDATE formations SET :slot = :cardId WHERE id = :id")
    suspend fun addToFormation(id: Long, slot: String, cardId: Int)

    @Query("UPDATE formations JOIN active_formation ON formations.id = active_formation.id SET :slot = :cardId")
    suspend fun addToFormation(slot: String, cardId: Int)

    @Query("UPDATE formations SET :slot = -1 WHERE id = :id")
    suspend fun removeFromFormation(id: Long, slot: String)

    @Query("UPDATE formations JOIN active_formation ON formations.id = active_formation.id SET :slot = -1")
    suspend fun removeFromFormation(slot: String)*/

    @Delete
    suspend fun delete(formationEntity: FormationEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setActive(activeFormationEntity: ActiveFormationEntity)
}