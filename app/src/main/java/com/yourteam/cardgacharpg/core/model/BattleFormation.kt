package com.yourteam.cardgacharpg.core.model

// Owner: Person 3 (Marc) — Eingabe-Modell für BattleSimulator.simulate(...)
//
// Wird von JEDEM Bereich genutzt, der einen Kampf auslösen will:
//   - PvE (Person 4): eigene Formation (via FormationRepository) vs. EnemyFormationEntity
//   - Arena (Person 5): beide Seiten über StatNormalization normiert
//
// Bewusst getrennt von Card: BattleParticipant enthält nur das, was der Kampf braucht
// (kein Room-/Compose-Bezug), damit BattleSimulator ein reines Kotlin-Modul bleiben kann
// (siehe Projektplan Abschnitt 6: "KEIN Android-Import!").

enum class Position { FRONT, BACK }

data class BattleParticipant(
    val cardId: Int,
    val name: String,
    val element: Element,
    val role: Role,
    val maxHp: Int,
    val atk: Int,
    val def: Int,
    val spd: Int,
    val position: Position
) {
    init {
        require(maxHp > 0) { "maxHp muss > 0 sein (war $maxHp für Karte $cardId)." }
    }
}

data class BattleFormationInput(
    val front: List<BattleParticipant>,
    val back: List<BattleParticipant>
) {
    init {
        require(front.size <= 3) { "Vordere Reihe darf maximal 3 Slots haben." }
        require(back.size <= 3) { "Hintere Reihe darf maximal 3 Slots haben." }
    }

    /** Alle Einheiten dieser Formation, unabhängig von der Reihe. */
    val all: List<BattleParticipant> get() = front + back

    companion object {
        /** Baut eine Formation direkt aus Karten (Person 1s Card-Modell). */
        fun fromCards(frontCards: List<Card>, backCards: List<Card>): BattleFormationInput = BattleFormationInput(
            front = frontCards.map { it.toParticipant(Position.FRONT) },
            back = backCards.map { it.toParticipant(Position.BACK) }
        )
    }
}

/** Snapshot einer Karte für den Kampf — nutzt die AKTUELLEN Stats (nach StatCalculator). */
fun Card.toParticipant(position: Position): BattleParticipant = BattleParticipant(
    cardId = id,
    name = name,
    element = element,
    role = role,
    maxHp = currentHp,
    atk = currentAtk,
    def = currentDef,
    spd = currentSpd,
    position = position
)
