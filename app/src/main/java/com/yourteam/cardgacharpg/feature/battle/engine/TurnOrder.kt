package com.yourteam.cardgacharpg.feature.battle.engine

import com.yourteam.cardgacharpg.core.model.BattleParticipant

// Owner: Person 3 (Marc) — pure Kotlin, initiative sorting
//
// Phase 1 laut GDD: alle Einheiten (beide Seiten gemeinsam) nach SPD absteigend sortieren.
// Gleichstand wird über die Karten-ID aufgelöst (aufsteigend), damit die Reihenfolge bei
// gleichem SPD-Wert IMMER identisch ist -> Voraussetzung für den Determinismus-Test
// (gleiche Formation 1.000x -> identisches Ergebnis, GDD-Pflicht).
object TurnOrder {

    val INITIATIVE_COMPARATOR: Comparator<BattleParticipant> =
        compareByDescending<BattleParticipant> { it.spd }.thenBy { it.cardId }

    fun sortByInitiative(units: List<BattleParticipant>): List<BattleParticipant> =
        units.sortedWith(INITIATIVE_COMPARATOR)
}
