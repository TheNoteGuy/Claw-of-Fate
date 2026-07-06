package com.yourteam.cardgacharpg.feature.collection.domain

import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao
import com.yourteam.cardgacharpg.feature.collection.data.ItemType
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import kotlin.math.ceil

// Owner: Person 1 (Leila) — XP cost + stat multiplier
//
// Ablauf (FA06):
// 1. Prüfen ob Karte bereits Maximallevel (je Seltenheit) hat -> Fehler, keine Überleveling
// 2. XP-Trank-Kosten fürs nächste Level berechnen
// 3. Inventory-Bestand prüfen (InventoryDao, geteilt mit Person 2 -> siehe InventoryEntity.kt)
// 4. Bei ausreichendem Bestand: Tränke abziehen, Level erhöhen, Stats via StatCalculator neu berechnen,
//    Karte über CardRepository persistieren

class LevelUpUseCase @Inject constructor(
    private val cardRepository: CardRepository,
    private val inventoryDao: InventoryDao
)
{

    companion object {
        private const val XP_PER_POTION = 50
        private const val BASE_XP_COST = 100
        private const val XP_COST_INCREASE_PER_LEVEL = 20
    }

    /**
     * XP-Kosten, um von (targetLevel - 1) auf targetLevel zu kommen.
     */
    fun xpCostForLevel(targetLevel: Int): Int {
        require(targetLevel >= 2) { "targetLevel muss >= 2 sein (Level 1 kostet nichts)." }
        return BASE_XP_COST + (targetLevel - 2) * XP_COST_INCREASE_PER_LEVEL
    }

    /**
     * Anzahl benötigter XP-Tränke (aufgerundet) für targetLevel.
     */
    fun potionsRequiredForLevel(targetLevel: Int): Int =
        ceil(xpCostForLevel(targetLevel) / XP_PER_POTION.toDouble()).toInt()

    /**
     * Führt ein Level-Up durch. Gibt Result.failure zurück statt zu crashen,
     * damit die UI (LevelUpSheet) den Fehlerfall sauber anzeigen kann.
     */
    suspend fun levelUp(card: Card): Result<Card> {
        if (card.level >= card.rarity.maxLevel) {
            return Result.failure(
                IllegalStateException(
                    "Karte ist bereits auf Maximallevel (${card.rarity.maxLevel}) für ${card.rarity}."
                )
            )
        }

        val targetLevel = card.level + 1
        val requiredPotions = potionsRequiredForLevel(targetLevel)

        val available = inventoryDao.getByType(ItemType.XP_POTION).firstOrNull()?.amount ?: 0
        if (available < requiredPotions) {
            return Result.failure(
                IllegalStateException(
                    "Nicht genug XP-Tränke: benötigt $requiredPotions, vorhanden $available."
                )
            )
        }

        inventoryDao.adjustAmount(ItemType.XP_POTION, -requiredPotions)

        val leveledCard = StatCalculator.recalculate(card.copy(level = targetLevel))
        cardRepository.update(leveledCard)

        return Result.success(leveledCard)
    }
}