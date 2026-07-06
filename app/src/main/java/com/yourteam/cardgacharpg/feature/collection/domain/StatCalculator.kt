package com.yourteam.cardgacharpg.feature.collection.domain

import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.core.model.Rarity
import kotlin.math.roundToInt

// Owner: Person 1 (Leila) — pure Kotlin, HP/ATK/DEF/SPD calc
//
// Formel: currentStat = baseStat * rarity.statMultiplier * levelFactor(level)
// levelFactor ist bewusst simpel gehalten (linear +8%/Level) — im Team anpassbar,
// falls eine andere Kurve (z.B. exponentiell) gewünscht ist.
object StatCalculator {

    private const val LEVEL_FACTOR_PER_LEVEL = 0.08 // +8% Stats pro Level über Level 1

    /**
     * Faktor für ein gegebenes Level (Level 1 => 1.0, Level 2 => 1.08, ...).
     */
    fun levelFactor(level: Int): Double {
        require(level >= 1) { "Level muss >= 1 sein, war: $level" }
        return 1.0 + (level - 1) * LEVEL_FACTOR_PER_LEVEL
    }

    /**
     * Berechnet einen einzelnen Stat (HP/ATK/DEF/SPD) aus Basiswert, Seltenheit und Level.
     * Rundet kaufmännisch, Minimum ist immer 1 (kein Stat darf 0 werden).
     */
    fun calculateStat(baseStat: Int, rarity: Rarity, level: Int): Int {
        val multiplier = rarity.statMultiplier * levelFactor(level)
        return (baseStat * multiplier).roundToInt().coerceAtLeast(1)
    }

    /**
     * Berechnet alle vier currentXxx-Felder neu und gibt eine aktualisierte Kopie der Karte zurück.
     * Level wird dabei auf [1, rarity.maxLevel] geklemmt (Schutz gegen Überleveling/Fehleingaben).
     */
    fun recalculate(card: Card): Card {
        val clampedLevel = card.level.coerceIn(1, card.rarity.maxLevel)
        return card.copy(
            level = clampedLevel,
            currentHp = calculateStat(card.baseHp, card.rarity, clampedLevel),
            currentAtk = calculateStat(card.baseAtk, card.rarity, clampedLevel),
            currentDef = calculateStat(card.baseDef, card.rarity, clampedLevel),
            currentSpd = calculateStat(card.baseSpd, card.rarity, clampedLevel)
        )
    }
}