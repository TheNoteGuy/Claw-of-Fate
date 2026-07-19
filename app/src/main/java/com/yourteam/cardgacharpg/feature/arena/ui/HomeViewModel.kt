package com.yourteam.cardgacharpg.feature.arena.ui

// Owner: Person 5 (Robin) — Flow.combine() of Card/Currency/LevelProgress/Trophy/Formation
// + Weekly-Reward-Trigger: echter 7-Tage-Zyklus (Button) + Debug-Override (Skip-Cooldown)
//
// ERLEDIGT: FormationDao (P3) + CampaignRepository (P4) sind jetzt angebunden —
// activeFormationSize und campaignStarsTotal kommen aus echten Daten.

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourteam.cardgacharpg.feature.arena.data.ArenaDao
import com.yourteam.cardgacharpg.feature.arena.domain.TrophyManager
import com.yourteam.cardgacharpg.feature.arena.domain.WeeklyRewardScheduler
import com.yourteam.cardgacharpg.feature.battle.data.FormationDao
import com.yourteam.cardgacharpg.feature.campaign.data.CampaignRepository
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class HomeUiState(
    val gems: Int = 0,
    val gold: Int = 0,
    val trophies: Int = 0,
    val cardCount: Int = 0,
    val activeFormationSize: Int = 0,
    // true = Slot belegt; Index 0-2 = vordere Reihe, 3-5 = hintere Reihe (FA03)
    val formationSlots: List<Boolean> = List(6) { false },
    val campaignStarsTotal: Int = 0,
    val campaignMaxStars: Int = 30, // 10 Level x 3 Sterne
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val currencyDao: CurrencyDao,
    private val arenaDao: ArenaDao,
    private val formationDao: FormationDao,
    campaignRepository: CampaignRepository,
    private val weeklyRewardScheduler: WeeklyRewardScheduler
) : ViewModel() {

    init {
        viewModelScope.launch { arenaDao.ensureRow() }
    }

    // combine() nimmt max. 5 Flows mit benannten Parametern — Formation + Kampagne
    // werden deshalb zuerst zu einem Pair gebuendelt und dann aussen dazukombiniert.
    private val progressFlow = combine(
        formationDao.observe(),
        campaignRepository.getAllLevelProgress()
    ) { formation, levels -> formation to levels }

    val uiState: StateFlow<HomeUiState> = combine(
        cardRepository.getAll(),
        currencyDao.observeGems(),
        currencyDao.observeGold(),
        arenaDao.getProfile(),
        progressFlow
    ) { cards, gems, gold, arenaProfile, progress ->
        val (formation, levels) = progress
        val slots = listOf(
            formation?.slot0, formation?.slot1, formation?.slot2,
            formation?.slot3, formation?.slot4, formation?.slot5
        )
        HomeUiState(
            gems = gems ?: 0,
            gold = gold ?: 0,
            trophies = arenaProfile?.trophies ?: 0,
            cardCount = cards.size,
            activeFormationSize = slots.count { it != null },
            formationSlots = slots.map { it != null },
            campaignStarsTotal = levels.sumOf { it.stars },
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    // Transiente Snackbar-Nachricht für die Weekly-Reward-Buttons. null = nichts anzeigen.
    private val _weeklyRewardMessage = MutableStateFlow<String?>(null)
    val weeklyRewardMessage: StateFlow<String?> = _weeklyRewardMessage.asStateFlow()

    fun claimWeeklyReward() {
        viewModelScope.launch {
            val reward = weeklyRewardScheduler.checkAndPayout(force = false)
            _weeklyRewardMessage.value = if (reward != null) {
                "+$reward Gems erhalten!"
            } else {
                notDueMessage()
            }
        }
    }

    /**
     * DEBUG/DEMO-Override: überspringt den 7-Tage-Cooldown explizit
     */
    fun skipCooldownAndClaim() {
        viewModelScope.launch {
            val reward = weeklyRewardScheduler.checkAndPayout(force = true)
            _weeklyRewardMessage.value = "+$reward Gems erhalten! (Cooldown übersprungen — Debug)"
        }
    }

    /** Vom HomeScreen aufrufen, nachdem die Snackbar die Nachricht angezeigt hat. */
    fun consumeWeeklyRewardMessage() {
        _weeklyRewardMessage.value = null
    }

    private suspend fun notDueMessage(): String {
        val profile = arenaDao.get() ?: return "Kein Arena-Profil gefunden."
        val elapsed = System.currentTimeMillis() - profile.lastRewardTimestamp
        val remainingMillis = (WeeklyRewardScheduler.CYCLE_DURATION_MILLIS - elapsed).coerceAtLeast(0)
        val remainingDays = TimeUnit.MILLISECONDS.toDays(remainingMillis) + 1 // aufrunden
        return "Noch nicht fällig — verfügbar in ca. $remainingDays Tag(en)."
    }

    // Nur noch Debug-Werkzeug (HomeScreen zeigt den Button nur bei BuildConfig.DEBUG):
    // der echte Arena-Kampf-Flow laeuft inzwischen ueber ArenaViewModel.onFight().
    fun fakeBattle() {
        viewModelScope.launch {
            val won = kotlin.random.Random.nextBoolean()
            arenaDao.updateTrophies(TrophyManager.deltaFor(won))
        }
    }
}