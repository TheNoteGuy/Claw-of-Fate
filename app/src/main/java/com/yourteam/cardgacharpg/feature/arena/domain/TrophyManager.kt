package com.yourteam.cardgacharpg.feature.arena.domain

// Owner: Person 5 (Robin) — analog zu CurrencyManager

import com.yourteam.cardgacharpg.feature.arena.data.ArenaDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Platzhalter — bei Bedarf im Team an die finalen GDD-Werte anpassen.
enum class League(val displayName: String, val minTrophies: Int) {
    GOLD("Gold", 1500),
    SILVER("Silber", 500),
    BRONZE("Bronze", 0)
}

class TrophyManager @Inject constructor(
    private val arenaDao: ArenaDao
) {
    val trophies: Flow<Int> = arenaDao.getProfile().map { it?.trophies ?: 0 }
    val league: Flow<League> = trophies.map { leagueFor(it) }
    suspend fun ensureInitialized() = arenaDao.ensureRow()
    suspend fun applyBattleResult(won: Boolean) = arenaDao.updateTrophies(deltaFor(won))

    companion object {
        const val TROPHIES_ON_WIN = 15
        const val TROPHIES_ON_LOSS = -10
        const val MIN_TROPHIES = 0
        fun deltaFor(won: Boolean): Int = if (won) TROPHIES_ON_WIN else TROPHIES_ON_LOSS


         // Reine Funktion, kein DAO-Zugriff -> Basis für den Pflicht-Unit-Test ohne Mocking.
        fun applyResult(currentTrophies: Int, won: Boolean): Int =
            (currentTrophies + deltaFor(won)).coerceAtLeast(MIN_TROPHIES)

        // Liga-Einstufung anhand des aktuellen Trophy-Stands
        fun leagueFor(trophies: Int): League =
            League.entries.first { trophies >= it.minTrophies }
    }
}