package com.yourteam.cardgacharpg.feature.gacha.data

import androidx.room.withTransaction
import com.yourteam.cardgacharpg.core.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Owner: Person 2 (Nico) — atomare Gem/Gold-Transaktionen.
// Kapselt den CurrencyDao. Andere Bereiche (P4/P5) benutzen bevorzugt diese Klasse,
// nicht den DAO direkt, damit "prüfen + abziehen" garantiert atomar bleibt.
class CurrencyManager @Inject constructor(
    private val db: AppDatabase,
    private val currencyDao: CurrencyDao
) {

    val gems: Flow<Int> = currencyDao.observeGems().map { it ?: 0 }
    val gold: Flow<Int> = currencyDao.observeGold().map { it ?: 0 }

    /** Beim App-Start einmal aufrufen, damit die Singleton-Zeile existiert. */
    suspend fun ensureInitialized() = currencyDao.ensureRow()

    /**
     * Versucht [amount] Gems abzuziehen. true = erfolgreich, false = zu wenig Gems.
     * Der Abzug ist atomar (Bedingung steckt im UPDATE), daher kann der Stand nie negativ werden.
     */
    suspend fun trySpendGems(amount: Int): Boolean {
        require(amount >= 0) { "amount darf nicht negativ sein" }
        return currencyDao.trySpendGems(amount) == 1
    }

    suspend fun addGems(amount: Int) = currencyDao.addGems(amount)

    suspend fun addGold(amount: Int) = currencyDao.addGold(amount)

    /**
     * Beispiel für eine zusammengesetzte atomare Transaktion (falls du später mal
     * mehrere Änderungen in einem Rutsch machst, z.B. Gems abziehen + Gold gutschreiben).
     */
    suspend fun <R> runAtomic(block: suspend () -> R): R = db.withTransaction { block() }
}