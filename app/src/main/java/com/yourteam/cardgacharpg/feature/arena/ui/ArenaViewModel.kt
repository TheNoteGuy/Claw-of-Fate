package com.yourteam.cardgacharpg.feature.arena.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourteam.cardgacharpg.core.model.BattleFormationInput
import com.yourteam.cardgacharpg.core.model.BattleSide
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.core.model.Position
import com.yourteam.cardgacharpg.core.model.Role
import com.yourteam.cardgacharpg.core.model.toParticipant
import com.yourteam.cardgacharpg.feature.arena.data.AiDeckPool
import com.yourteam.cardgacharpg.feature.arena.domain.League
import com.yourteam.cardgacharpg.feature.arena.domain.StatNormalization
import com.yourteam.cardgacharpg.feature.arena.domain.TrophyManager
import com.yourteam.cardgacharpg.feature.battle.data.FormationRepository
import com.yourteam.cardgacharpg.feature.battle.engine.BattleSimulator
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

// Owner: Person 5 (Robin) — FA07: lokale Arena gegen den 12er-KI-Pool.
//
// UPDATE (Schnittstellenvertrag jetzt erfuellt, Person 3 hat geliefert):
// Die frueheren Platzhalter (resolvePlaceholderBattle + "6 hoechststufige Karten als Formation")
// sind ersetzt durch die echten Vertragsaufrufe:
//   - FormationRepository/FormationDao  -> echte, im Editor gespeicherte Spielerformation
//   - BattleSimulator.simulate(player, enemy): BattleLog -> deterministischer Kampf
// Beide Seiten werden vor dem Kampf via StatNormalization auf die Arena-Basislinie skaliert
// (FA07: Stat-Normierung — Skill schlaegt Grinding).

data class ArenaUiState(
    val trophies: Int = 0,
    val league: League = League.BRONZE,
    val opponentDeck: List<Card> = emptyList(),
    // Die 6 Slots der aktiven Formation (slot0-2 vorne, slot3-5 hinten), null = leer.
    val playerSlots: List<Card?> = List(6) { null },
    val isFighting: Boolean = false,
    val result: ArenaBattleResult? = null,
    val isLoading: Boolean = true
) {
    /** Nur die belegten Slots — fuer die Vorschau im ArenaScreen. */
    val playerPreview: List<Card> get() = playerSlots.filterNotNull()
}

data class ArenaBattleResult(
    val won: Boolean,
    val trophyDelta: Int,
    val goldReward: Int = 0,
    val playerSurvivors: Int = 0,
    val playerTotalUnits: Int = 0
)

@HiltViewModel
class ArenaViewModel @Inject constructor(
    private val formationRepository: FormationRepository,
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

    // Persistenter State (Trophäen/Liga) + aktive Formation (FormationDao, Person 3)
    val uiState: StateFlow<ArenaUiState> = combine(
        trophyManager.trophies,
        trophyManager.league,
        formationRepository.activeFormation,
        transient
    ) { trophies, league, slots, t ->
        t.copy(
            trophies = trophies,
            league = league,
            playerSlots = slots,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ArenaUiState()
    )

    init {
        viewModelScope.launch {
            trophyManager.ensureInitialized()
            formationRepository.ensureInitialized()
        }
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
            // Frisch aus dem FormationDao lesen (nicht aus dem UI-Snapshot), damit ein direkt
            // zuvor gespeicherter Editor-Stand garantiert im Kampf ankommt.
            val slots = formationRepository.activeFormation.first()
            val player = normalizedPlayerFormation(slots)
            val opponent = normalizedOpponentFormation(state.opponentDeck)

            // Echte, deterministische Simulation (Person 3) statt der frueheren Schätzung.
            val log = BattleSimulator.simulate(player, opponent)
            val won = log.winner == BattleSide.PLAYER
            val delta = TrophyManager.deltaFor(won)

            trophyManager.applyBattleResult(won)

            val goldReward = if (won) {
                currencyManager.addGold(ARENA_WIN_GOLD_REWARD)
                ARENA_WIN_GOLD_REWARD
            } else {
                0
            }

            transient.update {
                it.copy(
                    isFighting = false,
                    result = ArenaBattleResult(
                        won = won,
                        trophyDelta = delta,
                        goldReward = goldReward,
                        playerSurvivors = log.playerSurvivors,
                        playerTotalUnits = log.playerTotalUnits
                    )
                )
            }
        }
    }

    fun onResultDismissed() {
        transient.update { it.copy(result = null) }
        onNextOpponent()
    }

    /** Spielerseite: Slot-Reihen aus dem Editor beibehalten (0-2 vorne, 3-5 hinten), normiert. */
    private fun normalizedPlayerFormation(slots: List<Card?>): BattleFormationInput {
        val normalized = slots.map { it?.let(StatNormalization::normalize) }
        return BattleFormationInput(
            front = normalized.take(3).filterNotNull().map { it.toParticipant(Position.FRONT) },
            back = normalized.drop(3).filterNotNull().map { it.toParticipant(Position.BACK) }
        )
    }

    /**
     * KI-Decks sind flache 5er-Listen ohne Slot-Info -> Reihen nach Rollen-Logik verteilen
     * (Tank/Krieger vorne, Rest hinten) und Ueberlauf > 3 in die jeweils andere Reihe schieben.
     */
    private fun normalizedOpponentFormation(deck: List<Card>): BattleFormationInput {
        val normalized = StatNormalization.normalizeFormation(deck)
        val front = normalized.filter { it.role == Role.TANK || it.role == Role.KRIEGER }.toMutableList()
        val back = normalized.filterNot { it.role == Role.TANK || it.role == Role.KRIEGER }.toMutableList()
        while (front.size > 3) back.add(0, front.removeAt(front.size - 1))
        while (back.size > 3) front.add(back.removeAt(0))
        return BattleFormationInput(
            front = front.map { it.toParticipant(Position.FRONT) },
            back = back.map { it.toParticipant(Position.BACK) }
        )
    }
}
