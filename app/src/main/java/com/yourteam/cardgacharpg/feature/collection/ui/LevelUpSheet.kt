package com.yourteam.cardgacharpg.feature.collection.ui

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.feature.collection.domain.StatCalculator

// Owner: Person 1 (Leila)
// FA06: Level-Up-Dialog. Stat-Werte werden animiert (animateIntAsState): sobald der echte
// Level-Up durch den LevelUpUseCase durchgeführt wurde und der übergebene [card]-State sich
// ändert, zählen HP/ATK/DEF/SPD sichtbar hoch statt hart zu springen. Zusätzlich eine reine
// Vorschau (StatCalculator, keine Seiteneffekte) für das NÄCHSTE Level, bevor bestätigt wurde.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelUpSheet(
    card: Card,
    potionsAvailable: Int,
    potionsRequired: Int,
    isLevelingUp: Boolean,
    errorMessage: String?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val isMaxLevel = card.level >= card.rarity.maxLevel
    val canAfford = potionsAvailable >= potionsRequired

    val previewLevel = (card.level + 1).coerceAtMost(card.rarity.maxLevel)
    val previewHp = StatCalculator.calculateStat(card.baseHp, card.rarity, previewLevel)
    val previewAtk = StatCalculator.calculateStat(card.baseAtk, card.rarity, previewLevel)
    val previewDef = StatCalculator.calculateStat(card.baseDef, card.rarity, previewLevel)
    val previewSpd = StatCalculator.calculateStat(card.baseSpd, card.rarity, previewLevel)

    val animatedHp by animateIntAsState(targetValue = card.currentHp, label = "hp")
    val animatedAtk by animateIntAsState(targetValue = card.currentAtk, label = "atk")
    val animatedDef by animateIntAsState(targetValue = card.currentDef, label = "def")
    val animatedSpd by animateIntAsState(targetValue = card.currentSpd, label = "spd")

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Text(
                "Level Up — ${card.name}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))

            if (isMaxLevel) {
                Text("Diese Karte hat bereits das Maximallevel (${card.rarity.maxLevel}) für ${card.rarity.name} erreicht.")
                return@Column
            }

            LevelUpRow("Aktuelles Level", "${card.level}")
            LevelUpRow("Nächstes Level", "${card.level + 1}")
            Spacer(Modifier.height(8.dp))
            LevelUpRow("Benötigte XP-Tränke", "$potionsRequired")
            LevelUpRow(
                "Vorhandene XP-Tränke",
                "$potionsAvailable",
                valueColor = if (canAfford) Color.Unspecified else MaterialTheme.colorScheme.error
            )

            if (!canAfford) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Nicht genug XP-Tränke für dieses Level-Up.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            errorMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(20.dp))
            Text("Stat-Vorschau", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            StatPreviewRow("HP", animatedHp, previewHp)
            StatPreviewRow("ATK", animatedAtk, previewAtk)
            StatPreviewRow("DEF", animatedDef, previewDef)
            StatPreviewRow("SPD", animatedSpd, previewSpd)

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onConfirm,
                enabled = canAfford && !isLevelingUp,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLevelingUp) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Level Up")
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LevelUpRow(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Composable
private fun StatPreviewRow(label: String, current: Int, preview: Int) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("$current", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            if (preview != current) {
                Text(
                    " → $preview",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}