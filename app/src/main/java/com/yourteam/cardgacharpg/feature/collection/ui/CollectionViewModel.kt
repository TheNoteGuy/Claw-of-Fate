package com.yourteam.cardgacharpg.feature.collection.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.core.model.Element
import com.yourteam.cardgacharpg.core.model.Rarity
import com.yourteam.cardgacharpg.core.model.Role
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// Owner: Person 1 (Leila) — FA02: filterbare Collection (Element, Rolle, Seltenheit, Level) + Sortierung

// Level-Filter als grobe Buckets statt Einzelwert — GDD verlangt einen "Level"-Filter, ein exakter
// Wert wäre bei Levelspannen 1-50 kaum nutzbar. Buckets decken alle Rarity-Maxlevels ab (bis 50).
enum class LevelBracket(val displayLabel: String, val range: IntRange) {
    LVL_1_10("1–10", 1..10),
    LVL_11_20("11–20", 11..20),
    LVL_21_30("21–30", 21..30),
    LVL_31_40("31–40", 31..40),
    LVL_41_50("41–50", 41..50)
}

data class CollectionFilterState(
    val element: Element? = null,
    val role: Role? = null,
    val rarity: Rarity? = null,
    val levelBracket: LevelBracket? = null
)

enum class CollectionSortOrder { LEVEL_DESC, RARITY_DESC }

data class CollectionUiState(
    val cards: List<Card> = emptyList(),
    val filter: CollectionFilterState = CollectionFilterState(),
    val sortOrder: CollectionSortOrder = CollectionSortOrder.LEVEL_DESC,
    val isLoading: Boolean = true
) {
    val isEmpty: Boolean get() = !isLoading && cards.isEmpty()
}

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val filterState = MutableStateFlow(CollectionFilterState())
    private val sortOrderState = MutableStateFlow(CollectionSortOrder.LEVEL_DESC)

    val uiState: StateFlow<CollectionUiState> = combine(
        cardRepository.getAll(),
        filterState,
        sortOrderState
    ) { cards, filter, sortOrder ->
        val filtered = cards.filter { card ->
            (filter.element == null || card.element == filter.element) &&
                    (filter.role == null || card.role == filter.role) &&
                    (filter.rarity == null || card.rarity == filter.rarity) &&
                    (filter.levelBracket == null || card.level in filter.levelBracket.range)
        }
        val sorted = when (sortOrder) {
            CollectionSortOrder.LEVEL_DESC ->
                filtered.sortedWith(compareByDescending<Card> { it.level }.thenBy { it.id })
            CollectionSortOrder.RARITY_DESC ->
                filtered.sortedWith(compareByDescending<Card> { it.rarity.ordinal }.thenByDescending { it.level })
        }
        CollectionUiState(cards = sorted, filter = filter, sortOrder = sortOrder, isLoading = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CollectionUiState()
    )

    fun setElementFilter(element: Element?) { filterState.value = filterState.value.copy(element = element) }
    fun setRoleFilter(role: Role?) { filterState.value = filterState.value.copy(role = role) }
    fun setRarityFilter(rarity: Rarity?) { filterState.value = filterState.value.copy(rarity = rarity) }
    fun setLevelBracketFilter(bracket: LevelBracket?) { filterState.value = filterState.value.copy(levelBracket = bracket) }
    fun clearFilters() { filterState.value = CollectionFilterState() }
    fun setSortOrder(order: CollectionSortOrder) { sortOrderState.value = order }
}