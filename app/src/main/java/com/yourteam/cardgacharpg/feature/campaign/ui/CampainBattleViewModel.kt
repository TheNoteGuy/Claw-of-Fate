package com.yourteam.cardgacharpg.feature.campaign.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourteam.cardgacharpg.core.model.BattleLog
import com.yourteam.cardgacharpg.core.model.BattleSide
import com.yourteam.cardgacharpg.feature.battle.data.FormationRepository
import com.yourteam.cardgacharpg.feature.battle.engine.BattleSimulator
import com.yourteam.cardgacharpg.feature.campaign.data.CampaignRepository
import com.yourteam.cardgacharpg.feature.campaign.domain.CampaignBattleOutcome
import com.yourteam.cardgacharpg.feature.campaign.domain.CompleteCampaignLevelUseCase
import com.yourteam.cardgacharpg.feature.campaign.domain.EnemyCatalog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Owner: Person 4 (Yassin) — FA04 (Trigger): startet den Kampagnen-Kampf fuer ein Level.
//
// Ablauf:
//   1. Spielerformation via FormationRepository (Person 3, Schnittstellenvertrag) laden
//   2. Gegnerformation via CampaignRepository -> EnemyCatalog aufloesen
//   3. BattleSimulator.simulate(player, enemy) (Person 3) — deterministisch
//   4. Level-Abschluss + Belohnungen genau EINMAL via CompleteCampaignLevelUseCase
//      (Sterne, Freischaltung, Gold/Gems/XP-Traenke)
// Die UI (CampaignBattleScreen) spielt den fertigen Log danach nur noch ab.

data class CampaignBattleUiState(
    val levelId: Int = 0,
    val isLoading: Boolean = true,
    val log: BattleLog? = null,
    val outcome: CampaignBattleOutcome? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class CampaignBattleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val formationRepository: FormationRepository,
    private val campaignRepository: CampaignRepository,
    private val completeCampaignLevelUseCase: CompleteCampaignLevelUseCase
) : ViewModel() {

    // Muss exakt zum Argument-Namen in NavGraph.kt passen ("levelId").
    private val levelId: Int = checkNotNull(savedStateHandle["levelId"]) {
        "CampaignBattleScreen benötigt ein levelId-Argument."
    }

    private val _uiState = MutableStateFlow(CampaignBattleUiState(levelId = levelId))
    val uiState: StateFlow<CampaignBattleUiState> = _uiState.asStateFlow()

    init {
        runBattle()
    }

    private fun runBattle() {
        viewModelScope.launch {
            val playerFormation = formationRepository.currentBattleFormation()
            if (playerFormation.all.isEmpty()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Formation ist leer — platziere zuerst Helden im Formations-Editor."
                    )
                }
                return@launch
            }

            val enemyFormation = EnemyCatalog.toBattleFormation(
                campaignRepository.getEnemyFormation(levelId)
            )

            val log = BattleSimulator.simulate(playerFormation, enemyFormation)

            // Abschluss sofort nach der Simulation (nicht erst nach dem Playback), damit
            // Sterne/Belohnungen auch bei vorzeitigem Verlassen des Screens gutgeschrieben sind.
            val outcome = completeCampaignLevelUseCase(
                levelId = levelId,
                isVictory = log.winner == BattleSide.PLAYER,
                survivingUnits = log.playerSurvivors,
                totalUnits = log.playerTotalUnits
            )

            _uiState.update { it.copy(isLoading = false, log = log, outcome = outcome) }
        }
    }
}
