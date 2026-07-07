package com.yourteam.cardgacharpg.feature.gacha.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourteam.cardgacharpg.feature.gacha.domain.GachaEngine

// Owner: Person 2 (Nico)
// Zeigt den Pity-Fortschritt (z.B. "45 / 90") + Soft-Pity-Warnung ab Pull 70.
@Composable
fun PityIndicator(
    pityCount: Int,
    isSoftPity: Boolean,
    modifier: Modifier = Modifier
) {
    val progress = pityCount.toFloat() / GachaEngine.HARD_PITY

    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Pity", style = MaterialTheme.typography.labelLarge)
            Text(
                "$pityCount / ${GachaEngine.HARD_PITY}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
        )
        if (isSoftPity) {
            Text(
                "Soft-Pity aktiv – erhöhte Legendary-Chance!",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFFFFC107),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}