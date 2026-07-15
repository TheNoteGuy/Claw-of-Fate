package com.yourteam.cardgacharpg.feature.campaign.ui

// Owner: Person 4 (Yassin)
import androidx.lifecycle.ViewModel
import com.yourteam.cardgacharpg.feature.campaign.data.CampaignRepository
import com.yourteam.cardgacharpg.feature.campaign.domain.StarRatingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.yourteam.cardgacharpg.feature.campaign.data.LevelProgressEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CampaignViewModel @Inject constructor(
    private val campaignRepository: CampaignRepository,
    private val starRatingUseCase: StarRatingUseCase
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

    /** Diese Methode wird aufgerufen, sobald das Match-Ergebnis feststeht. */
    fun onBattleFinished(levelId: Int, isVictory: Boolean, survivingUnits: Int, totalUnits: Int) {
        viewModelScope.launch {
            // 1. Sterne berechnen
            val earnedStars = starRatingUseCase.calculateStars(isVictory, survivingUnits, totalUnits)

            if (earnedStars > 0) {
                // 2. Schnappschuss aus dem State holen (Sicher vor Endlosschleifen!)
                val currentLevels = uiState.value.levels
                val playedLevel = currentLevels.find { it.levelId == levelId }

                if (playedLevel != null) {
                    // Sterne eintragen (nur wenn sie besser sind als vorher)
                    val bestStars = maxOf(playedLevel.stars, earnedStars)
                    campaignRepository.updateLevelProgress(playedLevel.copy(stars = bestStars))

                    // 3. Nächstes Level freischalten
                    if (levelId < LevelProgressEntity.TOTAL_LEVELS) {
                        val nextLevel = currentLevels.find { it.levelId == levelId + 1 }
                        if (nextLevel != null && !nextLevel.isUnlocked) {
                            campaignRepository.updateLevelProgress(nextLevel.copy(isUnlocked = true))
                        }
                    }
                }
            }
        }
    }
}
