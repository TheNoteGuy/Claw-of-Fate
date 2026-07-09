package com.yourteam.cardgacharpg.feature.arena.domain

import com.yourteam.cardgacharpg.feature.arena.data.ArenaDao
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

// Owner: Person 5 — simuliert die wöchentliche Arena-Auszahlung (50–150 Gems je Liga).

class WeeklyRewardScheduler @Inject constructor(
    private val arenaDao: ArenaDao,
    private val currencyManager: CurrencyManager
) {
    companion object {
        val CYCLE_DURATION_MILLIS: Long = TimeUnit.DAYS.toMillis(7)

        // Belohnungsspannen pro Liga
        private val BRONZE_RANGE = 50..80
        private val SILVER_RANGE = 80..120
        private val GOLD_RANGE = 120..150
    }

    suspend fun checkAndPayout(force: Boolean = false, rng: Random = Random.Default): Int? {
        val profile = arenaDao.get() ?: run {
            arenaDao.ensureRow()
            arenaDao.get()
        } ?: return null

        val now = System.currentTimeMillis()
        val isDue = force || (now - profile.lastRewardTimestamp) >= CYCLE_DURATION_MILLIS
        if (!isDue) return null

        val league = TrophyManager.leagueFor(profile.trophies)
        val range = rewardRangeFor(league)
        val reward = rng.nextInt(range.first, range.last + 1)

        currencyManager.addGems(reward)
        arenaDao.resetWeeklyReward(timestamp = now)

        return reward
    }

    private fun rewardRangeFor(league: League): IntRange = when (league) {
        League.BRONZE -> BRONZE_RANGE
        League.SILVER -> SILVER_RANGE
        League.GOLD -> GOLD_RANGE
    }
}