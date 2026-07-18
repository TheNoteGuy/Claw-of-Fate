package com.yourteam.cardgacharpg.feature.battle.ui

// Owner: Person 3 (Marc)
import  androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourteam.cardgacharpg.feature.battle.data.FormationEntity
import com.yourteam.cardgacharpg.feature.battle.data.FormationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

data class FormationOverviewUIState(
    val formations: List<FormationEntity> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class FormationOverviewViewModel @Inject constructor(
    private val formationRepository: FormationRepository,
) : ViewModel() {
    val uiState: StateFlow<FormationOverviewUIState> = combine(
        formationRepository.getAllFormations(),
    ) { formations ->
        FormationOverviewUIState(formations = formations[0], isLoading = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FormationOverviewUIState()
    )

    fun onCreate(): Long = runBlocking {
        formationRepository.create()
    }
}