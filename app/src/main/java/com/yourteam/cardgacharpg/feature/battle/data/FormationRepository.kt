package com.yourteam.cardgacharpg.feature.battle.data

import com.yourteam.cardgacharpg.core.model.BattleFormationInput
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.core.model.Position
import com.yourteam.cardgacharpg.core.model.toParticipant
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

// Owner: Person 3 (Marc)
//
// Schnittstellenvertrag (Projektplan Abschnitt 6):
// - stellt die aktive Formation als Flow<List<Card?>> bereit (6 Slots, null = leerer Slot),
//   damit FormationViewModel + Person 4/5 (Kampf-Trigger) sie lesen können
// - nutzt CardDao NUR lesend (über Person 1s CardRepository) — kein eigenes Card-Schema
class FormationRepository @Inject constructor(
    private val formationDao: FormationDao,
    private val cardRepository: CardRepository
) {

    /** 6 Slots in fester Reihenfolge (slot0..slot5), null = leer. Slot 0-2 = vorne, 3-5 = hinten. */
    val activeFormation: Flow<List<Card?>> = combine(
        formationDao.observe(),
        cardRepository.getAll()
    ) { entity, allCards ->
        val cardsById = allCards.associateBy { it.id }
        val slots = if (entity != null) {
            listOf(entity.slot0, entity.slot1, entity.slot2, entity.slot3, entity.slot4, entity.slot5)
        } else {
            List(FormationEntity.TOTAL_SLOTS) { null }
        }
        slots.map { id -> id?.let { cardsById[it] } }
    }

    suspend fun ensureInitialized() = formationDao.ensureRow()

    /** Speichert 6 Slots (slot0..slot5). [slots] muss genau 6 Einträge haben (leere Slots = null). */
    suspend fun save(slots: List<Card?>) {
        require(slots.size == FormationEntity.TOTAL_SLOTS) { "Eine Formation braucht genau 6 Slots (auch leere)." }
        formationDao.save(
            FormationEntity(
                slot0 = slots[0]?.id,
                slot1 = slots[1]?.id,
                slot2 = slots[2]?.id,
                slot3 = slots[3]?.id,
                slot4 = slots[4]?.id,
                slot5 = slots[5]?.id
            )
        )
    }

    /**
     * Baut die aktuell gespeicherte Formation als [BattleFormationInput] für den BattleSimulator.
     * Wird von Person 4 (PvE) / Person 5 (Arena) genutzt, um die Spieler-Seite eines Kampfs zu bauen —
     * z.B. `BattleSimulator.simulate(formationRepository.currentBattleFormation(), gegnerFormation)`.
     */
    suspend fun currentBattleFormation(): BattleFormationInput {
        val slots = activeFormation.first()
        val front = slots.take(FormationEntity.FRONT_ROW_SLOTS)
            .filterNotNull()
            .map { it.toParticipant(Position.FRONT) }
        val back = slots.drop(FormationEntity.FRONT_ROW_SLOTS)
            .filterNotNull()
            .map { it.toParticipant(Position.BACK) }
        return BattleFormationInput(front = front, back = back)
    }
}
