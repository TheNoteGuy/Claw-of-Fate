package com.yourteam.cardgacharpg.feature.arena.domain

import com.yourteam.cardgacharpg.core.model.Card
import kotlin.math.roundToInt

// Owner: Person 5 (Robin) — scales card stats to arena baseline
//
// In Arena skaliert nicht das eigene Level, sondern nur die
// Seltenheit. Jede Karte wird auf eine feste Basislinie normiert:
// 700 HP / 100 ATK / 40 DEF / 50 SPD), skaliert mit  Rarity.statMultiplier
// den Person 1 für den normalen StatCalculator nutzt
object StatNormalization {

    private const val BASELINE_HP = 700
    private const val BASELINE_ATK = 100
    private const val BASELINE_DEF = 40
    private const val BASELINE_SPD = 50


    fun normalize(card: Card): Card {
        val mult = card.rarity.statMultiplier
        return card.copy(
            currentHp = (BASELINE_HP * mult).roundToInt(),
            currentAtk = (BASELINE_ATK * mult).roundToInt(),
            currentDef = (BASELINE_DEF * mult).roundToInt(),
            currentSpd = (BASELINE_SPD * mult).roundToInt()
        )
    }

    fun normalizeFormation(cards: List<Card>): List<Card> = cards.map(::normalize)
}