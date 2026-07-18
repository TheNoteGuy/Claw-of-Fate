package com.yourteam.cardgacharpg.core.model

// Owner: Person 3 (Marc) — Ergebnis von BattleSimulator.simulate(...)
//
// WICHTIG (Projektplan Abschnitt 6): BattleLog wird NICHT persistiert — reines In-Memory-Modell
// für die aktuelle Kampf-Session. Die UI (BattleScreen) konsumiert `rounds`, um den Kampf
// Runde für Runde anzuzeigen (Simulation != Animation, siehe FA04 — der Simulator hat beim
// Erzeugen dieses Logs bereits fertig gerechnet).

enum class BattleSide { PLAYER, ENEMY }

enum class ActionType { ATTACK, SKILL, HEAL }

data class ActionEvent(
    val actorCardId: Int,
    val actorName: String,
    val actorSide: BattleSide,
    val actionType: ActionType,
    val targetCardId: Int? = null,
    val targetName: String? = null,
    // Schaden bei ATTACK/SKILL, geheilte Menge bei HEAL
    val amount: Int = 0,
    val targetDefeated: Boolean = false,
    val targetHpAfter: Int = 0
)

data class RoundEvent(
    val roundNumber: Int,
    val actions: List<ActionEvent>
)

data class BattleLog(
    val rounds: List<RoundEvent>,
    // null nur theoretisch möglich (z.B. leere Formationen auf beiden Seiten) — durch die
    // HP-Prozent-Tiebreak-Regel bei Rundendeckel gibt es sonst IMMER einen Sieger.
    val winner: BattleSide?,
    val playerSurvivors: Int,
    val playerTotalUnits: Int,
    val enemySurvivors: Int,
    val enemyTotalUnits: Int,
    // true = Kampf wurde nach 30 Runden per HP%-Regel entschieden statt durch Vernichtung
    val reachedRoundCap: Boolean
)
