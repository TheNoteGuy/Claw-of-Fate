package com.yourteam.cardgacharpg.feature.battle.data

import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.map

// Owner: Person 3 (Marc)
class FormationRepository @Inject constructor(
    private val formationDao: FormationDao,
    private val cardRepository: CardRepository
) {
    fun getAllFormations() =
        formationDao.getAll()

    fun getFormation(formationId: Long) =
        formationDao.get(formationId)

    suspend fun setName(id: Long, name: String) =
        formationDao.setName(id, name.trim())

    suspend fun create(): Long =
        create("Neue Formation")

    suspend fun create(name: String): Long {
        return withContext(Dispatchers.IO) {
            formationDao.create(FormationEntity(name))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getCards(): Flow<List<Card?>> =
        formationDao.get().map { entities -> listOf(
            entities.slot0,
            entities.slot1,
            entities.slot2,
            entities.slot3,
            entities.slot4,
            entities.slot5,
        )}.flatMapLatest { ids ->
            combine(ids.map { id -> cardRepository.getById(id)}) { latest -> latest.toList() }
        }



    suspend fun setActive(formationId: Long) =
        formationDao.setActive(ActiveFormationEntity(formationId))

    /*suspend fun addCardToFormation(slot: Int, cardId: Int) =
        formationDao.addToFormation("slot"+slot.toString(), cardId)*/
}