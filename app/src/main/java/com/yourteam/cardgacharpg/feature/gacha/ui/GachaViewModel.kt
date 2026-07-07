package com.yourteam.cardgacharpg.feature.gacha.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyManager
import com.yourteam.cardgacharpg.feature.gacha.data.GachaPityDao
import com.yourteam.cardgacharpg.feature.gacha.domain.GachaEngine
import com.yourteam.cardgacharpg.feature.gacha.domain.HeroPool
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

// Owner: Person 2 (Nico)
// Orchestriert den kompletten Pull-Flow und hält den UI-State als StateFlow.
// Reihenfolge eines Pulls: Kosten prüfen -> Gems abziehen -> würfeln (GachaEngine)
// -> Pity aktualisieren -> Karten bauen -> CardRepository.insertAll() -> Ergebnis emittieren.
@HiltViewModel
class GachaViewModel @Inject constructor(
    private val currencyManager: CurrencyManager,
    private val pityDao: GachaPityDao,
    private val cardRepository: CardRepository,
    private val heroPool: HeroPool
) : ViewModel() {

    // Injizierbar gemacht, damit Tests einen festen Seed setzen können.
    private val rng: Random = Random.Default

    // Transienter State (Loading, letztes Ergebnis, Snackbar-Meldung).
    private val transient = MutableStateFlow(
        GachaUiState()
    )

    // Persistenter State (Gems + Pity) kommt live aus der DB und wird mit dem
    // transienten State zu einem einzigen UiState zusammengeführt.
    val uiState: StateFlow<GachaUiState> =
        combine(
            currencyManager.gems,
            pityDao.observe(),
            transient
        ) { gems, pity, t ->
            t.copy(
                gems = gems,
                pityCount = pity?.pityCount ?: 0
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GachaUiState()
        )

    init {
        // Singleton-Zeilen anlegen, falls es der allererste Start ist.
        viewModelScope.launch {
            currencyManager.ensureInitialized()
            pityDao.ensureRow()
        }
    }

    fun onSinglePull() = pull(GachaEngine.SINGLE_PULL_COST, count = 1)

    fun onTenPull() = pull(GachaEngine.TEN_PULL_COST, count = GachaEngine.TEN_PULL_SIZE)

    private fun pull(cost: Int, count: Int) {
        if (transient.value.isPulling) return
        transient.update { it.copy(isPulling = true, message = null) }

        viewModelScope.launch {
            // 1. Gems atomar abziehen. Schlägt fehl, wenn zu wenig da sind.
            val paid = currencyManager.trySpendGems(cost)
            if (!paid) {
                transient.update {
                    it.copy(isPulling = false, message = "Nicht genug Gems für diesen Pull.")
                }
                return@launch
            }

            // 2. Würfeln (mit aktuellem Pity-Stand aus der DB).
            val startPity = pityDao.get()?.pityCount ?: 0
            val batch = GachaEngine.pullSequence(startPity, count, rng)

            // 3. Seltenheiten -> konkrete Karten.
            val cards = batch.results.map { rarity -> heroPool.randomHeroOf(rarity, rng) }

            // 4. Karten persistieren (Schnittstellenvertrag mit Person 1).
            cardRepository.insertAll(cards)

            // 5. Pity persistieren (überlebt App-Neustart).
            pityDao.update(count = batch.endPity, timestamp = System.currentTimeMillis())

            // 6. Ergebnis an die UI.
            transient.update { it.copy(isPulling = false, lastPull = cards) }
        }
    }

    /** Vom PullResultScreen aufgerufen, wenn der Spieler das Ergebnis wegtippt. */
    fun onResultDismissed() = transient.update { it.copy(lastPull = null) }

    /** Nachdem die Snackbar angezeigt wurde. */
    fun onMessageShown() = transient.update { it.copy(message = null) }
}