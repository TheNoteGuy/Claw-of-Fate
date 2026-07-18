package com.yourteam.cardgacharpg.feature.battle.engine

import com.yourteam.cardgacharpg.core.model.ActionEvent
import com.yourteam.cardgacharpg.core.model.ActionType
import com.yourteam.cardgacharpg.core.model.BattleFormationInput
import com.yourteam.cardgacharpg.core.model.BattleLog
import com.yourteam.cardgacharpg.core.model.BattleParticipant
import com.yourteam.cardgacharpg.core.model.BattleSide
import com.yourteam.cardgacharpg.core.model.Position
import com.yourteam.cardgacharpg.core.model.Role
import com.yourteam.cardgacharpg.core.model.RoundEvent
import kotlin.math.max
import kotlin.math.roundToInt

// ⚠ PUBLIC CONTRACT — Owner: Person 3 (Marc). PURE KOTLIN — NO Android imports! Consumed by Person 4 & Person 5
//
// Deterministischer Auto-Battler laut GDD (Projektplan Abschnitt 6):
//   Phase 1 — Initiative: siehe TurnOrder (SPD absteigend, Gleichstand via Karten-ID)
//   Phase 2 — Aktion: Ziel nach Rollen-Regel (siehe Role.kt für die genaue Zielwahl-Tabelle)
//   Phase 3 — Fähigkeit & Passiv: aktive Fähigkeit bei vollem Ladungsstand
//   Rundendeckel: max. 30 Runden; danach gewinnt die Seite mit höherem HP%-Anteil
//
// ANNAHME zum Ladungsstand (im GDD nicht exakt beziffert -> im Team anpassbar, siehe Konstanten
// unten): jede ausgeführte Aktion füllt die eigene Ladung um CHARGE_PER_ACTION. Bei vollem
// Ladungsstand wird statt eines normalen Angriffs/Heils die "aktive Fähigkeit" (Skill2 aus dem
// Katalog, Person 1) mit SKILL_DAMAGE_MULTIPLIER ausgelöst und die Ladung geht auf 0 zurück.
// Das ist bewusst simpel gehalten, da die konkreten Skill-Effekte (SkillCatalog) aktuell nur
// Text/Flavor sind und keine eigene Mechanik pro Skill-ID definieren.
//
// ANNAHME zur Zielwahl bei MAGIER/SUPPORT-Fallback: "Einzelziel" bedeutet laut GDD nur
// kein Flächenschaden, keine Reihen-Priorität -> es wird das gegnerische Ziel mit dem
// niedrigsten HP-Prozentsatz gewählt (Fokus-Feuer), Gleichstand via Karten-ID. Tank/Krieger
// und Ranger wenden dieselbe Fokus-Regel INNERHALB ihrer bevorzugten Reihe an.
object BattleSimulator {

    const val MAX_ROUNDS = 30
    const val CHARGE_PER_ACTION = 25
    const val CHARGE_FULL = 100
    const val SKILL_DAMAGE_MULTIPLIER = 1.5
    private const val HEAL_ATK_FACTOR = 0.5

    private class UnitState(val participant: BattleParticipant, val side: BattleSide) {
        var currentHp: Int = participant.maxHp
        var charge: Int = 0
        val isAlive: Boolean get() = currentHp > 0
    }

    private val INITIATIVE_COMPARATOR: Comparator<UnitState> =
        compareByDescending<UnitState> { it.participant.spd }.thenBy { it.participant.cardId }

    fun simulate(player: BattleFormationInput, enemy: BattleFormationInput): BattleLog {
        val playerUnits = player.all.map { UnitState(it, BattleSide.PLAYER) }
        val enemyUnits = enemy.all.map { UnitState(it, BattleSide.ENEMY) }

        require(playerUnits.map { it.participant.cardId }.toSet().size == playerUnits.size) {
            "Karten-IDs innerhalb der eigenen Formation müssen eindeutig sein."
        }
        require(enemyUnits.map { it.participant.cardId }.toSet().size == enemyUnits.size) {
            "Karten-IDs innerhalb der Gegner-Formation müssen eindeutig sein."
        }

        val rounds = mutableListOf<RoundEvent>()
        var roundNumber = 0

        while (roundNumber < MAX_ROUNDS) {
            if (playerUnits.none { it.isAlive } || enemyUnits.none { it.isAlive }) break
            roundNumber++

            val order = (playerUnits + enemyUnits).filter { it.isAlive }.sortedWith(INITIATIVE_COMPARATOR)
            val actions = mutableListOf<ActionEvent>()

            for (actor in order) {
                if (!actor.isAlive) continue // kann durch eine vorherige Aktion in dieser Runde gestorben sein

                val ownTeam = if (actor.side == BattleSide.PLAYER) playerUnits else enemyUnits
                val enemyTeam = if (actor.side == BattleSide.PLAYER) enemyUnits else playerUnits
                if (enemyTeam.none { it.isAlive }) continue // Kampf in dieser Runde bereits entschieden

                actions += performAction(actor, ownTeam, enemyTeam)
            }

            rounds += RoundEvent(roundNumber, actions)
        }

        val playerAliveCount = playerUnits.count { it.isAlive }
        val enemyAliveCount = enemyUnits.count { it.isAlive }
        val reachedCap = roundNumber >= MAX_ROUNDS && playerAliveCount > 0 && enemyAliveCount > 0

        val winner = when {
            playerAliveCount > 0 && enemyAliveCount == 0 -> BattleSide.PLAYER
            enemyAliveCount > 0 && playerAliveCount == 0 -> BattleSide.ENEMY
            playerAliveCount == 0 && enemyAliveCount == 0 -> null
            else -> decideByHpPercent(playerUnits, enemyUnits)
        }

        return BattleLog(
            rounds = rounds,
            winner = winner,
            playerSurvivors = playerAliveCount,
            playerTotalUnits = playerUnits.size,
            enemySurvivors = enemyAliveCount,
            enemyTotalUnits = enemyUnits.size,
            reachedRoundCap = reachedCap
        )
    }

    private fun performAction(actor: UnitState, ownTeam: List<UnitState>, enemyTeam: List<UnitState>): ActionEvent {
        val useSkill = actor.charge >= CHARGE_FULL
        val multiplier = if (useSkill) SKILL_DAMAGE_MULTIPLIER else 1.0
        actor.charge = if (useSkill) 0 else (actor.charge + CHARGE_PER_ACTION).coerceAtMost(CHARGE_FULL)

        // Support: heilt zuerst das eigene Team (niedrigste HP), sofern Heilbedarf besteht.
        if (actor.participant.role == Role.SUPPORT) {
            val healTarget = ownTeam
                .filter { it.isAlive && it.currentHp < it.participant.maxHp }
                .minWithOrNull(
                    compareBy(
                        { it.currentHp.toDouble() / it.participant.maxHp },
                        { it.participant.cardId }
                    )
                )

            if (healTarget != null) {
                val healAmount = max(1, (actor.participant.atk * HEAL_ATK_FACTOR * multiplier).roundToInt())
                val before = healTarget.currentHp
                healTarget.currentHp = (healTarget.currentHp + healAmount).coerceAtMost(healTarget.participant.maxHp)
                val actuallyHealed = healTarget.currentHp - before

                return ActionEvent(
                    actorCardId = actor.participant.cardId,
                    actorName = actor.participant.name,
                    actorSide = actor.side,
                    actionType = if (useSkill) ActionType.SKILL else ActionType.HEAL,
                    targetCardId = healTarget.participant.cardId,
                    targetName = healTarget.participant.name,
                    amount = actuallyHealed,
                    targetDefeated = false,
                    targetHpAfter = healTarget.currentHp
                )
            }
            // Kein Heilbedarf im eigenen Team -> unten wie ein normaler Angriff behandeln
            // (Rollen-Regel für SUPPORT-Fallback: kein Reihenfokus, siehe selectTarget()).
        }

        val target = selectTarget(actor.participant.role, enemyTeam)

        val damage = DamageFormula.calculateDamage(
            atk = actor.participant.atk,
            def = target.participant.def,
            attackerElement = actor.participant.element,
            defenderElement = target.participant.element,
            skillMultiplier = multiplier
        )
        target.currentHp = (target.currentHp - damage).coerceAtLeast(0)

        return ActionEvent(
            actorCardId = actor.participant.cardId,
            actorName = actor.participant.name,
            actorSide = actor.side,
            actionType = if (useSkill) ActionType.SKILL else ActionType.ATTACK,
            targetCardId = target.participant.cardId,
            targetName = target.participant.name,
            amount = damage,
            targetDefeated = !target.isAlive,
            targetHpAfter = target.currentHp
        )
    }

    /**
     * Zielwahl-Regel laut Role.kt:
     * Tank/Krieger -> vorne; Ranger -> hinten zuerst; Magier -> Einzelziel (kein Reihenfokus).
     * Innerhalb der jeweils bevorzugten Reihe (bzw. bei Magier/Support-Fallback über alle
     * Ziele hinweg) wird das Ziel mit dem niedrigsten HP-Prozentsatz gewählt (Fokus-Feuer),
     * Gleichstand über die Karten-ID.
     */
    private fun selectTarget(role: Role, enemyTeam: List<UnitState>): UnitState {
        val alive = enemyTeam.filter { it.isAlive }
        check(alive.isNotEmpty()) { "selectTarget wurde ohne lebende Gegner aufgerufen." }

        val candidates = when (role) {
            Role.TANK, Role.KRIEGER -> alive.filter { it.participant.position == Position.FRONT }.ifEmpty { alive }
            Role.RANGER -> alive.filter { it.participant.position == Position.BACK }.ifEmpty { alive }
            Role.MAGIER, Role.SUPPORT -> alive
        }

        return candidates.minWith(
            compareBy(
                { it.currentHp.toDouble() / it.participant.maxHp },
                { it.participant.cardId }
            )
        )
    }

    /** Rundendeckel-Regel: Sieger nach höherem HP%-Anteil (Summe aktuelle HP / Summe max HP). */
    private fun decideByHpPercent(playerUnits: List<UnitState>, enemyUnits: List<UnitState>): BattleSide {
        fun hpPercent(units: List<UnitState>): Double {
            val totalMax = units.sumOf { it.participant.maxHp }
            if (totalMax == 0) return 0.0
            return units.sumOf { it.currentHp }.toDouble() / totalMax
        }
        return if (hpPercent(playerUnits) >= hpPercent(enemyUnits)) BattleSide.PLAYER else BattleSide.ENEMY
    }
}
