package com.yourteam.cardgacharpg.feature.battle.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Owner: Person 3 (Marc) — FA04: Sieg/Niederlage-Ergebnis nach einem Kampf.
// Reine Anzeige — echte Belohnungen (Gold/Gems/Sterne) vergeben Person 4 (PvE) bzw.
// Person 5 (Arena) selbst, sobald deren Kampf-Trigger BattleSimulator aufrufen.
@Composable
fun BattleResultScreen(
    isVictory: Boolean = false,
    playerSurvivors: Int = 0,
    playerTotalUnits: Int = 0,
    onContinue: () -> Unit = {}
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            Icon(
                imageVector = if (isVictory) Icons.Default.Check else Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = if (isVictory) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(16.dp))
            Text(
                if (isVictory) "SIEG!" else "NIEDERLAGE",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (isVictory) Color(0xFFFFC107) else Color.White
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "$playerSurvivors von $playerTotalUnits Helden überlebt",
                color = Color.White.copy(alpha = 0.85f)
            )
            Spacer(Modifier.height(32.dp))
            Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) {
                Text("Weiter")
            }
        }
    }
}
