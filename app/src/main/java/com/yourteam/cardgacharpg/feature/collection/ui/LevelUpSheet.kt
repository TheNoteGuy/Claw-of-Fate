package com.yourteam.cardgacharpg.feature.collection.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourteam.cardgacharpg.core.model.Card

// Owner: Person 1 (Leila)
// FA06: Level-Up-Dialog. Rein state-driven — der komplette Ablauf (Kosten prüfen,
// Tränke abziehen, Stats neu berechnen) läuft über CardDetailViewModel -> LevelUpUseCase.
// Dieses Composable zeigt nur an und meldet Nutzerinteraktionen zurück.

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