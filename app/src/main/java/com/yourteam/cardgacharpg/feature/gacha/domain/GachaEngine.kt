package com.yourteam.cardgacharpg.feature.gacha.domain

import com.yourteam.cardgacharpg.core.model.Rarity
import kotlin.random.Random

// Owner: Person 2 (Nico) — reines Kotlin, KEINE Android-Imports.
// Enthält die komplette Gacha-Wahrscheinlichkeitslogik aus dem GDD:
//   - Basis-Raten-Tabelle (Common/Rare/Epic/Legendary)
//   - Pity-Kurve (Soft-Pity ab Pull 70, Hard-Pity bei 90)
//
// Diese Klasse ist zu 100% deterministisch, wenn man ihr ein Random mit festem Seed gibt
// -> erfüllt den Determinismus-Test (gleicher Seed => gleiche Pulls).
object GachaEngine {

    // --- Kosten (FA01) ---
    const val SINGLE_PULL_COST = 100
    const val TEN_PULL_COST = 900
    const val TEN_PULL_SIZE = 10

    // --- Pity-Grenzen (FA01) ---
    const val SOFT_PITY_START = 70   // ab hier steigt die Legendary-Rate
    const val HARD_PITY = 90         // spätestens hier garantiert ein Legendary

    // --- Basis-Raten-Tabelle (GDD): Common 50%, Rare 30%, Epic 15%, Legendary 4% ---
    // Hinweis: 50+30+15+4 = 99. Der 1%-Rest ist im GDD nicht spezifiziert; wir normieren
    // die Nicht-Legendary-Verteilung sauber auf ihre relativen Gewichte (siehe rollRarity).
    private const val WEIGHT_COMMON = 50.0
    private const val WEIGHT_RARE = 30.0
    private const val WEIGHT_EPIC = 15.0
    const val BASE_LEGENDARY_RATE = 0.04

    /**
     * Legendary-Wahrscheinlichkeit für den [pullIndex]-ten Pull einer Serie ohne Legendary.
     * [pullIndex] ist 1-basiert: der 1. Pull nach dem letzten Legendary hat Index 1.
     *
     * GDD-Kurve:
     *   Pull 1..69 : 4%
     *   Pull 70+   : 4% + (pull - 69) * 5%   (gedeckelt auf 100%)
     *
     * Ergibt: legendaryRate(70) = 0.09  und  legendaryRate(90) = 1.0.
     */
    fun legendaryRate(pullIndex: Int): Double {
        require(pullIndex >= 1) { "pullIndex muss >= 1 sein, war $pullIndex" }
        return if (pullIndex < SOFT_PITY_START) {
            BASE_LEGENDARY_RATE
        } else {
            (BASE_LEGENDARY_RATE + (pullIndex - 69) * 0.05).coerceAtMost(1.0)
        }
    }

    /**
     * Zieht EINE Karte. [pityCount] = Anzahl Pulls seit dem letzten Legendary (0 = frisch).
     * Der aktuelle Pull ist also der (pityCount + 1)-te Pull der Serie.
     *
     * Rein funktional: kein State, keine Seiteneffekte. Der Aufrufer (GachaViewModel)
     * ist dafür verantwortlich, den Pity-Zähler zu aktualisieren (siehe [nextPity]).
     */
    fun rollRarity(pityCount: Int, rng: Random): Rarity {
        require(pityCount >= 0) { "pityCount darf nicht negativ sein" }
        val pullIndex = pityCount + 1
        val legRate = legendaryRate(pullIndex)

        // 1. Legendary-Wurf zuerst (inkl. Pity)
        if (rng.nextDouble() < legRate) return Rarity.LEGENDARY

        // 2. Kein Legendary -> unter Common/Rare/Epic nach relativem Gewicht verteilen.
        val total = WEIGHT_COMMON + WEIGHT_RARE + WEIGHT_EPIC
        val roll = rng.nextDouble() * total
        return when {
            roll < WEIGHT_COMMON -> Rarity.COMMON
            roll < WEIGHT_COMMON + WEIGHT_RARE -> Rarity.RARE
            else -> Rarity.EPIC
        }
    }

    /**
     * Neuer Pity-Zähler nach einem Pull.
     * Legendary -> zurück auf 0. Alles andere -> +1.
     */
    fun nextPity(currentPity: Int, rolled: Rarity): Int =
        if (rolled == Rarity.LEGENDARY) 0 else currentPity + 1

    /**
     * Führt [count] Pulls hintereinander aus und trägt den Pity-Zähler mit.
     * Gibt die gezogenen Seltenheiten IN REIHENFOLGE + den finalen Pity-Stand zurück.
     * Wird für Single- (count=1) und 10er-Pull (count=10) genutzt und ist die Basis
     * für den 10k-Simulations-Test.
     */
    fun pullSequence(startPity: Int, count: Int, rng: Random): PullBatch {
        require(count >= 1)
        val results = ArrayList<Rarity>(count)
        var pity = startPity
        repeat(count) {
            val r = rollRarity(pity, rng)
            results.add(r)
            pity = nextPity(pity, r)
        }
        return PullBatch(results = results, endPity = pity)
    }
}

/** Ergebnis einer Pull-Serie: die gezogenen Seltenheiten + der Pity-Stand danach. */
data class PullBatch(
    val results: List<Rarity>,
    val endPity: Int
)