package com.yourteam.cardgacharpg.feature.collection.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.yourteam.cardgacharpg.core.model.Element
import com.yourteam.cardgacharpg.core.model.Rarity
import kotlinx.coroutines.flow.Flow

// Owner: Person 1 (Leila) — getAll(), getByElement(), getByRarity(), getById(), update()
// ⚠ insertAll() ist Teil des Public Contracts -> wird von Person 2 nach jedem Gacha-Pull aufgerufen
//
// NEU (Karten-Stacking): insertAll() dupliziert nicht mehr. Existiert bereits eine Karte mit
// gleichem heroId + rarity, wird stattdessen deren count erhoeht (siehe insertOrStack()).
@Dao
interface CardDao {

    @Query("SELECT * FROM cards ORDER BY level DESC, id ASC")
    fun getAll(): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE element = :element")
    fun getByElement(element: Element): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE rarity = :rarity")
    fun getByRarity(rarity: Rarity): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE id = :id")
    fun getById(id: Int): Flow<CardEntity?>

    @Update
    suspend fun update(card: CardEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(card: CardEntity): Long

    // Interner Baustein fuers Stacking — bitte insertOrStack() statt insertAll() direkt nutzen.
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(cards: List<CardEntity>): List<Long>

    /** Sucht den bestehenden Stapel fuer heroId + rarity (max. 1 Zeile dank Stacking). */
    @Query("SELECT * FROM cards WHERE heroId = :heroId AND rarity = :rarity LIMIT 1")
    suspend fun findByHeroAndRarity(heroId: Int, rarity: Rarity): CardEntity?

    @Query("UPDATE cards SET count = count + :delta WHERE id = :id")
    suspend fun addToCount(id: Int, delta: Int)

    /**
     * Bulk-Insert mit Duplikat-Stacking (Schnittstellenvertrag mit Person 2, GachaViewModel):
     * - Karte mit gleichem heroId + rarity existiert bereits -> count += Anzahl der Duplikate
     * - sonst -> neue Zeile mit count = Anzahl der Duplikate im Pull
     * Laeuft als eine Transaktion, damit ein 10er-Pull entweder komplett oder gar nicht landet.
     */
    @Transaction
    suspend fun insertOrStack(cards: List<CardEntity>) {
        // Auch INNERHALB eines Pulls stapeln (10er-Pull kann denselben Helden mehrfach enthalten).
        val grouped = cards.groupBy { it.heroId to it.rarity }
        for ((key, sameHero) in grouped) {
            val (heroId, rarity) = key
            val existing = findByHeroAndRarity(heroId, rarity)
            if (existing != null) {
                addToCount(existing.id, sameHero.size)
            } else {
                insert(sameHero.first().copy(count = sameHero.size))
            }
        }
    }
}
