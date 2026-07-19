package com.yourteam.cardgacharpg.feature.campaign.ui

// Owner: Person 4 (Yassin)
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourteam.cardgacharpg.feature.campaign.data.CampaignRepository
import com.yourteam.cardgacharpg.feature.campaign.domain.CompleteCampaignLevelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CampaignViewModel @Inject constructor(
    private val campaignRepository: CampaignRepository,
    private val completeCampaignLevelUseCase: CompleteCampaignLevelUseCase
) : ViewModel() {

    // 1. Interner Notizzettel (Transient State)
    private val transient = MutableStateFlow(
        CampaignUiState()
    )

    // 2. Das Schaufenster für die UI (UiState)
    val uiState: StateFlow<CampaignUiState> = combine(
        campaignRepository.getAllLevelProgress(),
        transient
    ) { dbLevels, tState ->
        tState.copy(levels = dbLevels) // man nutzt copy, weil States immutable sind
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CampaignUiState(isLoading = true) // Startet mit isLoading = true
    )

    init {
        // App-Start: Stelle sicher, dass die Level existieren und schalte Laden ab
        viewModelScope.launch {
            campaignRepository.initializeLevels()
            transient.update { it.copy(isLoading = false) }
        }
    }

    // =========================================
    // UI EVENTS (Die Knöpfe für die Map)
    // =========================================

    /** Wird aufgerufen, wenn der Spieler auf ein Level auf der Karte tippt */
    fun onLevelClicked(levelId: Int) {
        // Schreibt die ID in den State -> UI öffnet das Detail-Sheet
        transient.update { it.copy(selectedLevelId = levelId) }
    }

    /** Wird aufgerufen, wenn der Spieler das Popup (Detail-Sheet) wieder schließt */
    fun onDetailSheetDismissed() {
        // Löscht die ID aus dem State -> UI schließt das Detail-Sheet
        transient.update { it.copy(selectedLevelId = null) }
    }

    // =========================================
    // KAMPF LOGIK (Nach dem Match)
    // =========================================

    /**
     * Kompatibilitäts-API: Sterne-Vergabe, Freischaltung UND Belohnungs-Gutschrift
     * (Gold/Gems/XP-Tränke) laufen jetzt gebündelt im CompleteCampaignLevelUseCase.
     *
     * HINWEIS: Der eigentliche Kampagnen-Kampf-Flow (CampaignBattleViewModel) ruft den
     * UseCase direkt auf und braucht diese Methode NICHT — sie bleibt nur erhalten, falls
     * andere Stellen (z.B. ein manueller Test-Trigger) ein Kampfergebnis melden wollen.
     * Achtung: pro Kampf nur EINE der beiden Stellen aufrufen, sonst werden Belohnungen
     * doppelt gutgeschrieben.
     */
    fun onBattleFinished(levelId: Int, isVictory: Boolean, survivingUnits: Int, totalUnits: Int) {
        viewModelScope.launch {
            completeCampaignLevelUseCase(levelId, isVictory, survivingUnits, totalUnits)
        }
    }
}
