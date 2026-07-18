package com.yourteam.cardgacharpg.feature.battle.engine

import com.yourteam.cardgacharpg.core.model.Element
import kotlin.math.max
import kotlin.math.roundToInt

// Owner: Person 3 (Marc) — pure Kotlin, no Android imports
//
// Schadensformel laut GDD (Projektplan Abschnitt 6):
//   mitigation = DEF / (DEF + 100)
//   damage = ATK * (1 - mitigation) * type_modifier
//   Minimum ist immer 1 (kein Angriff darf 0 Schaden machen).
//
// Beispielrechnung aus dem Pflicht-Test (ATK 200 / DEF 100 -> 100 Schaden):
//   mitigation(100) = 100/(100+100) = 0.5
//   damage = 200 * (1-0.5) * 1.0 = 100  ✓
object DamageFormula {

    private const val MITIGATION_CONSTANT = 100.0

    fun mitigation(def: Int): Double {
        require(def >= 0) { "DEF darf nicht negativ sein, war $def" }
        return def / (def + MITIGATION_CONSTANT)
    }

    /**
     * Element-Multiplikator für einen Angriff von [attacker] auf [defender] (siehe Element.kt):
     * Feuer > Natur, Natur > Wasser, Wasser > Feuer: jeweils +25% (nur in die überlegene Richtung).
     * Arkan vs Arkan: +50% Schaden, beidseitig (Sonderregel laut GDD).
     */
    fun elementModifier(attacker: Element, defender: Element): Double = when {
        attacker == Element.ARKAN && defender == Element.ARKAN -> 1.5
        attacker == Element.FEUER && defender == Element.NATUR -> 1.25
        attacker == Element.NATUR && defender == Element.WASSER -> 1.25
        attacker == Element.WASSER && defender == Element.FEUER -> 1.25
        else -> 1.0
    }

    /**
     * Berechnet den finalen Schaden eines Angriffs. Rundet kaufmännisch, Minimum immer 1.
     * [skillMultiplier] erlaubt Phase 3 (aktive Fähigkeit bei vollem Ladungsstand) einen Bonus
     * draufzusetzen, ohne die Grundformel zu verändern (Standardangriff nutzt 1.0).
     */
    fun calculateDamage(
        atk: Int,
        def: Int,
        attackerElement: Element,
        defenderElement: Element,
        skillMultiplier: Double = 1.0
    ): Int {
        val mit = mitigation(def)
        val typeMod = elementModifier(attackerElement, defenderElement)
        val raw = atk * (1.0 - mit) * typeMod * skillMultiplier
        return max(1, raw.roundToInt())
    }
}
