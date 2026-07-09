package com.yourteam.cardgacharpg.feature.arena.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.feature.arena.data.AiDeckPool
import com.yourteam.cardgacharpg.feature.arena.domain.League
import com.yourteam.cardgacharpg.feature.arena.domain.StatNormalization
import com.yourteam.cardgacharpg.feature.arena.domain.TrophyManager
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

// Owner: Person 5 (Robin)
//
// Zwei Abhängigkeiten aus dem Schnittstellenvertrag sind im Repo noch leere Stubs und daher
// bewusst NICHT injiziert:
//   - FormationDao.get(): Flow<Formation?>                    (Person 3) -> echte Spielerformation
//   - BattleSimulator.simulate(player, enemy): BattleLog       (Person 3) -> deterministischer Kampf
// Bis dahin: "Formation" = die 6 höchststufigen eigenen Karten, und der Kampfausgang wird über
// eine einfache Stats-Summe + kleinem Zufallsfaktor geschätzt (resolvePlaceholderBattle).
// TODO (sobald Marc liefert): beides durch die echten Aufrufe ersetzen.

data class ArenaUiState(
    val trophies: Int = 0,
    val league: League = League.BRONZE,
    val opponentDeck: List<Card> = emptyList(),
    val playerPreview: List<Card> = emptyList(),
    val isFighting: Boolean = false,
    val result: ArenaBattleResult? = null,
    val isLoading: Boolean = true
)

data class ArenaBattleResult(val won: Boolean, val trophyDelta: Int, val goldReward: Int = 0)

@HiltViewModel
class ArenaViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val trophyManager: TrophyManager,
    private val currencyManager: CurrencyManager
) : ViewModel() {

    companion object {
        const val ARENA_WIN_GOLD_REWARD = 25
    }

    private val rng: Random = Random.Default

    // Transienter State: aktueller Gegner, Kampf-Status, letztes Ergebnis
    private val transient = MutableStateFlow(
        ArenaUiState(opponentDeck = AiDeckPool.randomDeck(rng))
    )

    // Persistenter State (Trophäen/Liga, eigene Karten)
    val uiState: StateFlow<ArenaUiState> = combine(
        trophyManager.trophies,
        trophyManager.league,
        cardRepository.getAll(),
        transient
    ) { trophies, league, cards, t ->
        t.copy(
            trophies = trophies,
            league = league,
            playerPreview = playerFormationPreview(cards),
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ArenaUiState()
    )

    init {
        viewModelScope.launch { trophyManager.ensureInitialized() }
    }

    /** Neuen zufälligen Gegner aus dem 12er-Pool ziehen (z.B. nach Wegtippen des Ergebnisses). */
    fun onNextOpponent() = transient.update {
        it.copy(opponentDeck = AiDeckPool.randomDeck(rng), result = null)
    }

    fun onFight() {
        val state = uiState.value
        if (state.isFighting || state.playerPreview.isEmpty()) return

        transient.update { it.copy(isFighting = true, result = null) }

        viewModelScope.launch {
            val player = StatNormalization.normalizeFormation(state.playerPreview)
            val opponent = StatNormalization.normalizeFormation(state.opponentDeck)

            // TODO (Person 3): sobald BattleSimulator.simulate(player, enemy): BattleLog fertig ist,
            // hier den echten, deterministischen Kampf auswerten statt der Platzhalter-Schätzung.
            val won = resolvePlaceholderBattle(player, opponent)
            val delta = if (won) TrophyManager.TROPHIES_ON_WIN else TrophyManager.TROPHIES_ON_LOSS

            trophyManager.applyBattleResult(won)

            val goldReward = if (won) {
                currencyManager.addGold(ARENA_WIN_GOLD_REWARD)
                ARENA_WIN_GOLD_REWARD
            } else {
                0
            }

            transient.update {
                it.copy(isFighting = false, result = ArenaBattleResult(won, delta, goldReward))
            }
        }
    }

    fun onResultDismissed() {
        transient.update { it.copy(result = null) }
        onNextOpponent()
    }

    // TODO (Person 3): durch FormationDao.get() ersetzen, sobald die Spielerformation
    // persistent gespeichert wird. Bis dahin: die 6 höchststufigen eigenen Karten.
    private fun playerFormationPreview(cards: List<Card>): List<Card> =
        cards.sortedByDescending { it.level }.take(6)

    // Platzhalter-Kampflogik NUR bis BattleSimulator (Person 3) verfügbar ist:
    // Gesamt-"Power" beider normierten Formationen vergleichen, mit ±15% Zufallsstreuung,
    // damit auch ein leicht unterlegenes Team gelegentlich gewinnen kann.
    private fun resolvePlaceholderBattle(player: List<Card>, opponent: List<Card>): Boolean {
        fun power(cards: List<Card>) = cards.sumOf { c ->
            c.currentHp + c.currentAtk * 2 + c.currentDef + c.currentSpd
        }
        val playerPower = power(player) * (0.85 + rng.nextDouble() * 0.30)
        val opponentPower = power(opponent).toDouble()
        return playerPower >= opponentPower
    }
}