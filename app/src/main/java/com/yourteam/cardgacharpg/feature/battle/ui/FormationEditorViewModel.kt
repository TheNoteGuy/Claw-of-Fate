package com.yourteam.cardgacharpg.feature.battle.ui

// Owner: Person 3 (Marc)
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.feature.battle.data.FormationEntity
import com.yourteam.cardgacharpg.feature.battle.data.FormationRepository
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FormationEditorUIState(
    val formation: FormationEntity? = null,
    val cards: List<Card?> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class FormationEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val formationRepository: FormationRepository,
) : ViewModel() {
    private val formationId: Long = checkNotNull(savedStateHandle["formationId"]) {
        "FormationEditor benötigt ein formationId-Argument."
    }

    val uiState: StateFlow<FormationEditorUIState> = combine(
        formationRepository.getFormation(formationId),
        formationRepository.getCards(),
    ) { formation, cards ->
        FormationEditorUIState(formation = formation, cards = cards, isLoading = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FormationEditorUIState(),
    )// as MutableStateFlow<FormationEditorUIState>

    fun onSetName(name: String) {
        viewModelScope.launch {
            formationRepository.setName(formationId, name) }
    }

    fun activateCard(id: Int) {

    }
}