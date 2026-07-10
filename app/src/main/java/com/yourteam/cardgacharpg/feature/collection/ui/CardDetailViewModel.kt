package com.yourteam.cardgacharpg.feature.collection.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao
import com.yourteam.cardgacharpg.feature.collection.data.ItemType
import com.yourteam.cardgacharpg.feature.collection.domain.LevelUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Owner: Person 1 (Leila) — FA09 Detailansicht + FA06 Level-Up-Einstieg

data class CardDetailUiState(
    val card: Card? = null,
    val potionsAvailable: Int = 0,
    val isLoading: Boolean = true,
    val isLevelingUp: Boolean = false,
    val errorMessage: String? = null
) {
    val isMaxLevel: Boolean get() = card?.let { it.level >= it.rarity.maxLevel } ?: false
}

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val cardRepository: CardRepository,
    private val inventoryDao: InventoryDao,
    private val levelUpUseCase: LevelUpUseCase
) : ViewModel() {

    // Muss exakt zum Argument-Namen in NavGraph.kt passen ("cardId").
    private val cardId: Int = checkNotNull(savedStateHandle["cardId"]) {
        "CardDetailScreen benötigt ein cardId-Argument."
    }

    private val transient = MutableStateFlow(CardDetailUiState())

    val uiState: StateFlow<CardDetailUiState> = combine(
        cardRepository.getById(cardId),
        inventoryDao.getByType(ItemType.XP_POTION),
        transient
    ) { card, inventory, t ->
        t.copy(
            card = card,
            potionsAvailable = inventory?.amount ?: 0,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CardDetailUiState()
    )

    /** XP-Tränke für das nächste Level. null = bereits Max-Level. */
    fun potionsRequiredForNextLevel(): Int? {
        val card = uiState.value.card ?: return null
        if (card.level >= card.rarity.maxLevel) return null
        return levelUpUseCase.potionsRequiredForLevel(card.level + 1)
    }

    fun onLevelUp() {
        val card = uiState.value.card ?: return
        if (transient.value.isLevelingUp) return

        transient.update { it.copy(isLevelingUp = true, errorMessage = null) }
        viewModelScope.launch {
            levelUpUseCase.levelUp(card).fold(
                onSuccess = { transient.update { it.copy(isLevelingUp = false) } },
                onFailure = { error ->
                    transient.update { it.copy(isLevelingUp = false, errorMessage = error.message) }
                }
            )
        }
    }

    fun onErrorShown() = transient.update { it.copy(errorMessage = null) }
}