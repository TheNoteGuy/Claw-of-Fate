package com.yourteam.cardgacharpg.feature.gacha.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourteam.cardgacharpg.core.model.Card

// Owner: Person 2 (Nico)
// Deckt die gezogenen Karten auf, farbcodiert nach Seltenheit.
// (Stretch: Karten nacheinander mit Animation aufdecken – hier erst mal statisches Grid.)
@Composable
fun PullResultScreen(
    cards: List<Card>,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            if (cards.size > 1) "${cards.size}er-Pull" else "Ergebnis",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(if (cards.size > 1) 3 else 1),
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cards) { card -> PulledCard(card) }
        }

        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        ) {
            Text("Weiter")
        }
    }
}

@Composable
private fun PulledCard(card: Card) {
    val rc = card.rarity.color()
    Box(
        modifier = Modifier
            .aspectRatio(0.72f)
            .clip(RoundedCornerShape(12.dp))
            .background(rc.copy(alpha = 0.15f))
            .border(2.dp, rc, RoundedCornerShape(12.dp))
            .padding(8.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Column {
            Text(
                card.rarity.label(),
                style = MaterialTheme.typography.labelMedium,
                color = rc,
                fontWeight = FontWeight.Bold
            )
            Text(
                card.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            // TODO: statt Text hier das Katzen-Bild (card.imageAssetName) rendern,
            //       sobald die Assets von Person 1 vorliegen.
        }
    }
}