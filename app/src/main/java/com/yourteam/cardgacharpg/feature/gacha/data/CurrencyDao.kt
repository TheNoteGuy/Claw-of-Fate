package com.yourteam.cardgacharpg.feature.gacha.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// ⚠ PUBLIC CONTRACT — Owner: Person 2 (Nico). Consumed by Person 4 (PvE) & Person 5 (Arena/Dashboard)
//
// Schnittstellenvertrag (ab Woche 2 stabil halten):
//   - getGems()/getGold() als Flow  -> Dashboard (Person 5) zeigt Live-Werte
//   - addGems/addGold(amount)        -> Belohnungen (Person 4 + 5)
//   - spendGems(amount): Boolean     -> Pull (intern), gibt false zurück wenn zu wenig Gems
//
// WICHTIG: Direkt-Zugriffe auf diese Methoden bitte über den CurrencyManager laufen lassen,
// damit "prüfen + abziehen" atomar bleibt (keine negativen Stände, keine Race Conditions).
@Dao
interface CurrencyDao {

    @Query("SELECT gems FROM currency WHERE id = :id")
    fun observeGems(id: Int = CurrencyEntity.SINGLETON_ID): Flow<Int?>

    @Query("SELECT gold FROM currency WHERE id = :id")
    fun observeGold(id: Int = CurrencyEntity.SINGLETON_ID): Flow<Int?>

    @Query("SELECT * FROM currency WHERE id = :id")
    suspend fun get(id: Int = CurrencyEntity.SINGLETON_ID): CurrencyEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ensureRow(entity: CurrencyEntity = CurrencyEntity(gems = CurrencyEntity.STARTING_GEMS))

    // Bedingter Abzug: setzt nur, wenn genug Gems da sind. Gibt betroffene Zeilen zurück
    // (1 = erfolgreich abgezogen, 0 = zu wenig Gems). Race-condition-sicher, weil die
    // Bedingung Teil des UPDATE ist.
    @Query("UPDATE currency SET gems = gems - :amount WHERE id = :id AND gems >= :amount")
    suspend fun trySpendGems(amount: Int, id: Int = CurrencyEntity.SINGLETON_ID): Int

    @Query("UPDATE currency SET gems = gems + :amount WHERE id = :id")
    suspend fun addGems(amount: Int, id: Int = CurrencyEntity.SINGLETON_ID)

    @Query("UPDATE currency SET gold = gold + :amount WHERE id = :id")
    suspend fun addGold(amount: Int, id: Int = CurrencyEntity.SINGLETON_ID)
}