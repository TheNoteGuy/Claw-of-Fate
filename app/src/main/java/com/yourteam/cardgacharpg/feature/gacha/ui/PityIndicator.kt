package com.yourteam.cardgacharpg.feature.gacha.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourteam.cardgacharpg.feature.gacha.domain.GachaEngine

// Owner: Person 2 (Nico)
// Zeigt den Pity-Fortschritt (z.B. "45 / 90") + Soft-Pity-Warnung ab Pull 70.
// UI-Polish: optionaler (i)-Button, der einen Erklär-Dialog öffnet (siehe GachaScreen),
// goldener Fortschrittsbalken, helle Schrift für den dunklen Gacha-Hintergrund.
@Composable
fun PityIndicator(
    pityCount: Int,
    isSoftPity: Boolean,
    modifier: Modifier = Modifier,
    onInfoClick: (() -> Unit)? = null
) {
    val progress = pityCount.toFloat() / GachaEngine.HARD_PITY
    val gold = Color(0xFFFFC107)

    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Pity", style = MaterialTheme.typography.labelLarge, color = Color.White)
                if (onInfoClick != null) {
                    IconButton(onClick = onInfoClick, modifier = Modifier.size(24.dp)) {
                        Text("ⓘ", color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }
            Text(
                "$pityCount / ${GachaEngine.HARD_PITY}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (isSoftPity) gold else Color.White
            )
        }
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            color = gold,
            trackColor = Color.White.copy(alpha = 0.15f),
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
        )
        if (isSoftPity) {
            Text(
                "🔥 Soft-Pity aktiv – erhöhte Legendary-Chance!",
                style = MaterialTheme.typography.labelMedium,
                color = gold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
