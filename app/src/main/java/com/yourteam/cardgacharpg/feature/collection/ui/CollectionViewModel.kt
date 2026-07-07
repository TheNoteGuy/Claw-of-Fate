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

// Owner: Person 1 (Leila) — FA02: filterbare Collection (Element, Rolle, Seltenheit) + Sortierung

data class CollectionFilterState(
    val element: Element? = null,
    val role: Role? = null,
    val rarity: Rarity? = null
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
                    (filter.rarity == null || card.rarity == filter.rarity)
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
    fun clearFilters() { filterState.value = CollectionFilterState() }
    fun setSortOrder(order: CollectionSortOrder) { sortOrderState.value = order }
}