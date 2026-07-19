package com.yourteam.cardgacharpg.feature.arena.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Owner: Person 5 (Robin)
// Deckt das Ergebnis eines Arena-Kampfs auf (Sieg/Niederlage + Trophäen-Delta).

@Composable
fun ArenaResultScreen(
    result: ArenaBattleResult,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            if (result.won) "Sieg! 🏆" else "Niederlage",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = if (result.won) Color(0xFFFFC107) else Color.White
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "${if (result.trophyDelta >= 0) "+" else ""}${result.trophyDelta} 🏆",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        if (result.playerTotalUnits > 0) {
            Spacer(Modifier.height(4.dp))
            Text(
                "${result.playerSurvivors} von ${result.playerTotalUnits} Helden überlebt",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
        if (result.goldReward > 0) {
            Spacer(Modifier.height(4.dp))
            Text(
                "+${result.goldReward} 🪙",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFFFC107)
            )
        }
        Spacer(Modifier.height(32.dp))
        Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
            Text("Weiter")
        }
    }
}