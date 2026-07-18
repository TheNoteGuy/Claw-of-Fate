package com.yourteam.cardgacharpg.feature.battle.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourteam.cardgacharpg.core.model.BattleFormationInput
import com.yourteam.cardgacharpg.core.model.BattleLog
import com.yourteam.cardgacharpg.core.model.Position
import com.yourteam.cardgacharpg.core.model.toParticipant
import com.yourteam.cardgacharpg.feature.arena.data.AiDeckPool
import com.yourteam.cardgacharpg.feature.battle.data.FormationRepository
import com.yourteam.cardgacharpg.feature.battle.engine.BattleSimulator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

// Owner: Person 3 (Marc) — FA04: löst BattleSimulator aus und hält das Ergebnis für die UI.
//
// HINWEIS: Solange Person 4 (PvE) / Person 5 (Arena) den Gegner noch nicht über ihre eigenen
// Flows liefern, nutzt dieser "Testkampf" einen zufälligen Gegner aus Person 5s AiDeckPool
// (rein lesend). Sobald echte Kampagnen-/Arena-Gegner verfügbar sind, muss hier nur die
// enemyFormation ausgetauscht werden — der Rest (BattleSimulator.simulate(...)) bleibt gleich.
data class BattleUiState(
    val isLoading: Boolean = true,
    val log: BattleLog? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class BattleViewModel @Inject constructor(
    private val formationRepository: FormationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BattleUiState())
    val uiState: StateFlow<BattleUiState> = _uiState.asStateFlow()

    private val rng = Random.Default

    init {
        runTestBattle()
    }

    fun runTestBattle() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, log = null) }
        viewModelScope.launch {
            val playerFormation = formationRepository.currentBattleFormation()
            if (playerFormation.all.isEmpty()) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Formation ist leer — bitte zuerst Helden platzieren.")
                }
                return@launch
            }

            val enemyDeck = AiDeckPool.randomDeck(rng)
            val enemyFormation = BattleFormationInput(
                front = enemyDeck.take(3).map { it.toParticipant(Position.FRONT) },
                back = enemyDeck.drop(3).map { it.toParticipant(Position.BACK) }
            )

            val log = BattleSimulator.simulate(playerFormation, enemyFormation)
            _uiState.update { it.copy(isLoading = false, log = log) }
        }
    }
}
