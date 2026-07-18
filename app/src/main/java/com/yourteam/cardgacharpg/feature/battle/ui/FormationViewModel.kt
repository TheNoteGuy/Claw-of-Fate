package com.yourteam.cardgacharpg.feature.battle.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.feature.battle.data.FormationEntity
import com.yourteam.cardgacharpg.feature.battle.data.FormationRepository
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Owner: Person 3 (Marc) — FA03: Formations-Editor (3x2-Grid)
//
// Tap-to-Place statt Drag-and-Drop (Projektplan erlaubt ausdrücklich "Drag-and-Drop ODER
// Tap-to-Place") — robuster über verschiedene Bildschirmgrößen/Eingabemethoden hinweg und ohne
// zusätzliche Gesture-Dependency.

data class FormationUiState(
    val slots: List<Card?> = List(FormationEntity.TOTAL_SLOTS) { null }, // slot0-2 = vorne, slot3-5 = hinten
    val availableCards: List<Card> = emptyList(), // Karten, die NICHT bereits in der Formation stehen
    val duplicateWarning: String? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class FormationViewModel @Inject constructor(
    private val formationRepository: FormationRepository,
    private val cardRepository: CardRepository
) : ViewModel() {

    private val transient = MutableStateFlow(FormationUiState())

    val uiState: StateFlow<FormationUiState> = combine(
        formationRepository.activeFormation,
        cardRepository.getAll(),
        transient
    ) { slots, allCards, t ->
        val usedIds = slots.filterNotNull().map { it.id }.toSet()
        t.copy(
            slots = slots,
            availableCards = allCards.filter { it.id !in usedIds },
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FormationUiState()
    )

    init {
        viewModelScope.launch { formationRepository.ensureInitialized() }
    }

    /**
     * Setzt [card] in [slotIndex] (0..5). Bricht mit einer Warnung ab, wenn dieselbe Karte
     * bereits in einem ANDEREN Slot steht — Editor-Validierung laut Projektplan:
     * "gleiche Karte doppelt -> Warnung anzeigen, Aktion abbrechen".
     */
    fun placeCard(slotIndex: Int, card: Card) {
        require(slotIndex in 0 until FormationEntity.TOTAL_SLOTS) { "slotIndex muss zwischen 0 und 5 liegen." }
        val current = uiState.value.slots

        val duplicateSlot = current.indexOfFirst { it?.id == card.id }
        if (duplicateSlot != -1 && duplicateSlot != slotIndex) {
            transient.update {
                it.copy(duplicateWarning = "${card.name} ist bereits in Slot ${duplicateSlot + 1} platziert.")
            }
            return
        }

        val updated = current.toMutableList().also { it[slotIndex] = card }
        persist(updated)
    }

    fun clearSlot(slotIndex: Int) {
        require(slotIndex in 0 until FormationEntity.TOTAL_SLOTS) { "slotIndex muss zwischen 0 und 5 liegen." }
        val updated = uiState.value.slots.toMutableList().also { it[slotIndex] = null }
        persist(updated)
    }

    fun onWarningShown() = transient.update { it.copy(duplicateWarning = null) }

    private fun persist(slots: List<Card?>) {
        viewModelScope.launch { formationRepository.save(slots) }
    }
}
