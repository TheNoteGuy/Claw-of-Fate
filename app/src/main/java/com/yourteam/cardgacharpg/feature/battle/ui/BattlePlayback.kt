package com.yourteam.cardgacharpg.feature.battle.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yourteam.cardgacharpg.core.model.ActionEvent
import com.yourteam.cardgacharpg.core.model.ActionType
import com.yourteam.cardgacharpg.core.model.BattleLog
import com.yourteam.cardgacharpg.core.model.BattleParticipant
import com.yourteam.cardgacharpg.core.model.BattleSide
import com.yourteam.cardgacharpg.core.model.Position
import kotlinx.coroutines.delay

// Owner: Person 3 (Marc) — FA04: gemeinsame Playback-UI fuer alle Kampf-Screens
// (Testkampf aus dem Formations-Editor UND PvE-Kampagne, Person 4).
//
// Simulation != Animation (GDD): BattleSimulator hat beim Erzeugen des BattleLog bereits
// fertig gerechnet. Diese Datei spielt den Log lediglich Schritt fuer Schritt ab:
//   - beide Formationen (Gegner oben, Spieler unten) mit HP-Balken pro Karte
//   - HP werden aus ActionEvent.targetHpAfter rekonstruiert und live aktualisiert
//   - scrollbare "Kampfverlauf"-Box, die Runde fuer Runde die Aktionen aufdeckt
//   - 1x/2x-Speed und Skip (GDD Accessibility: Battle-Skip + 2x-Speed-Schaltflaeche)

private const val BASE_STEP_DELAY_MS = 700L

/** Ein aufzudeckender Schritt des Kampfverlaufs. */
private data class PlaybackStep(val roundNumber: Int, val action: ActionEvent)

/** HP-Schluessel: Seite + Karten-ID (IDs sind nur INNERHALB einer Formation eindeutig). */
private data class UnitKey(val side: BattleSide, val cardId: Int)

/**
 * Auf welcher Seite steht das Ziel dieser Aktion?
 * HEAL trifft immer das eigene Team, ATTACK immer das gegnerische. SKILL ist mehrdeutig
 * (Support loest bei vollem Ladungsstand eine SKILL-Heilung auf die EIGENE Seite aus,
 * siehe BattleSimulator.performAction) -> daher wird die targetCardId gegen die
 * Formations-Snapshots aufgeloest.
 */
private fun targetSideOf(action: ActionEvent, playerIds: Set<Int>, enemyIds: Set<Int>): BattleSide {
    val opposite = if (action.actorSide == BattleSide.PLAYER) BattleSide.ENEMY else BattleSide.PLAYER
    return when (action.actionType) {
        ActionType.HEAL -> action.actorSide
        ActionType.ATTACK -> opposite
        ActionType.SKILL -> {
            val targetId = action.targetCardId
            val ownIds = if (action.actorSide == BattleSide.PLAYER) playerIds else enemyIds
            val otherIds = if (action.actorSide == BattleSide.PLAYER) enemyIds else playerIds
            when {
                targetId == null -> opposite
                targetId in otherIds -> opposite            // Skill-Angriff (Normalfall)
                targetId in ownIds -> action.actorSide      // Support-Skill-Heilung
                else -> opposite
            }
        }
    }
}

/**
 * Spielt einen fertig simulierten [log] ab.
 * [footer] bekommt mitgeteilt, ob das Playback fertig ist (fuer "Zum Ergebnis"-Buttons).
 */
@Composable
fun BattlePlaybackContent(
    log: BattleLog,
    modifier: Modifier = Modifier,
    playerLabel: String = "Deine Formation",
    enemyLabel: String = "Gegner",
    footer: @Composable (playbackFinished: Boolean) -> Unit = {}
) {
    val steps = remember(log) {
        log.rounds.flatMap { round -> round.actions.map { PlaybackStep(round.roundNumber, it) } }
    }

    var revealedCount by remember(log) { mutableIntStateOf(0) }
    var speed by remember { mutableIntStateOf(1) } // 1x oder 2x
    val finished by remember(log) { derivedStateOf { revealedCount >= steps.size } }

    // Auto-Playback: alle BASE_STEP_DELAY_MS / speed einen weiteren Schritt aufdecken.
    LaunchedEffect(log, speed) {
        while (revealedCount < steps.size) {
            delay(BASE_STEP_DELAY_MS / speed)
            revealedCount++
        }
    }

    // HP-Stand nach den bisher aufgedeckten Schritten rekonstruieren.
    val playerIds = remember(log) { log.playerUnits.map { it.cardId }.toSet() }
    val enemyIds = remember(log) { log.enemyUnits.map { it.cardId }.toSet() }
    val hpByUnit: Map<UnitKey, Int> = remember(log, revealedCount) {
        val hp = mutableMapOf<UnitKey, Int>()
        log.playerUnits.forEach { hp[UnitKey(BattleSide.PLAYER, it.cardId)] = it.maxHp }
        log.enemyUnits.forEach { hp[UnitKey(BattleSide.ENEMY, it.cardId)] = it.maxHp }
        steps.take(revealedCount).forEach { step ->
            val targetId = step.action.targetCardId ?: return@forEach
            hp[UnitKey(targetSideOf(step.action, playerIds, enemyIds), targetId)] = step.action.targetHpAfter
        }
        hp
    }

    Column(modifier = modifier) {
        // --- Gegner-Formation (hinten oben, vorne Richtung Mitte) ---
        Text(enemyLabel, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(4.dp))
        FormationRows(
            units = log.enemyUnits,
            side = BattleSide.ENEMY,
            hpByUnit = hpByUnit,
            frontRowFirst = false
        )

        Spacer(Modifier.height(10.dp))

        // --- Spieler-Formation (vorne oben Richtung Mitte, hinten unten) ---
        Text(playerLabel, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(4.dp))
        FormationRows(
            units = log.playerUnits,
            side = BattleSide.PLAYER,
            hpByUnit = hpByUnit,
            frontRowFirst = true
        )

        Spacer(Modifier.height(12.dp))

        // --- Kampfverlauf: scrollbare Box, Runde fuer Runde ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Kampfverlauf", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { speed = if (speed == 1) 2 else 1 }) { Text("${speed}\u00D7") }
                if (!finished) {
                    TextButton(onClick = { revealedCount = steps.size }) { Text("Überspringen") }
                }
            }
        }

        BattleLogBox(
            steps = steps,
            revealedCount = revealedCount,
            playerIds = playerIds,
            enemyIds = enemyIds,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
        )

        Spacer(Modifier.height(12.dp))
        footer(finished)
    }
}

@Composable
private fun FormationRows(
    units: List<BattleParticipant>,
    side: BattleSide,
    hpByUnit: Map<UnitKey, Int>,
    frontRowFirst: Boolean
) {
    val front = units.filter { it.position == Position.FRONT }
    val back = units.filter { it.position == Position.BACK }
    val rows = if (frontRowFirst) listOf(front, back) else listOf(back, front)

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        rows.forEach { row ->
            if (row.isEmpty()) return@forEach
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                row.forEach { unit ->
                    UnitTile(
                        unit = unit,
                        currentHp = hpByUnit[UnitKey(side, unit.cardId)] ?: unit.maxHp,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Leere Plaetze auffuellen, damit Spalten unter-/uebereinander ausgerichtet bleiben
                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun UnitTile(unit: BattleParticipant, currentHp: Int, modifier: Modifier = Modifier) {
    val alive = currentHp > 0
    val hpFraction by animateFloatAsState(
        targetValue = (currentHp.toFloat() / unit.maxHp).coerceIn(0f, 1f),
        label = "hp_${unit.cardId}"
    )
    val barColor = when {
        !alive -> MaterialTheme.colorScheme.error
        hpFraction < 0.3f -> MaterialTheme.colorScheme.error
        hpFraction < 0.6f -> Color(0xFFE6A817)
        else -> Color(0xFF4E9B4E)
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .alpha(if (alive) 1f else 0.4f)
            .padding(6.dp)
    ) {
        Text(
            text = unit.name,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
        Spacer(Modifier.height(3.dp))
        LinearProgressIndicator(
            progress = { hpFraction },
            color = barColor,
            trackColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = if (alive) "$currentHp / ${unit.maxHp}" else "K.O.",
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BattleLogBox(
    steps: List<PlaybackStep>,
    revealedCount: Int,
    playerIds: Set<Int>,
    enemyIds: Set<Int>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val revealed = steps.take(revealedCount)

    // Immer zum neuesten Eintrag scrollen, solange das Playback laeuft.
    LaunchedEffect(revealedCount) {
        if (revealed.isNotEmpty()) listState.animateScrollToItem(revealed.size - 1)
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        itemsIndexed(revealed) { index, step ->
            val isNewRound = index == 0 || revealed[index - 1].roundNumber != step.roundNumber
            if (isNewRound) {
                Text(
                    text = "— Runde ${step.roundNumber} —",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
            // SKILL ist mehrdeutig (Support-Skill = Heilung aufs eigene Team) ->
            // fuer den Text aufloesen, ob das Ziel ein Verbuendeter ist.
            val targetIsAlly =
                targetSideOf(step.action, playerIds, enemyIds) == step.action.actorSide
            ActionLine(step.action, targetIsAlly = targetIsAlly)
        }
    }
}

@Composable
private fun ActionLine(action: ActionEvent, targetIsAlly: Boolean) {
    val sidePrefix = if (action.actorSide == BattleSide.PLAYER) "🐱" else "👾"
    val text = when {
        action.actionType == ActionType.HEAL ->
            "$sidePrefix ${action.actorName} heilt ${action.targetName} um ${action.amount} HP"
        action.actionType == ActionType.SKILL && targetIsAlly ->
            "$sidePrefix ${action.actorName} wirkt seinen Skill: ${action.targetName} +${action.amount} HP"
        action.actionType == ActionType.SKILL ->
            "$sidePrefix ${action.actorName} entfesselt seinen Skill auf ${action.targetName}: ${action.amount} Schaden" +
                    if (action.targetDefeated) " (besiegt!)" else ""
        else ->
            "$sidePrefix ${action.actorName} greift ${action.targetName} an: ${action.amount} Schaden" +
                    if (action.targetDefeated) " (besiegt!)" else ""
    }
    Text(text, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 1.dp))
}
