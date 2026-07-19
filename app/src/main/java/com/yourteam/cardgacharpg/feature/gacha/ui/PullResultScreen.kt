package com.yourteam.cardgacharpg.feature.gacha.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.core.model.Rarity
import com.yourteam.cardgacharpg.feature.collection.ui.CardImage
import kotlinx.coroutines.delay

// Owner: Person 2 (Nico)
// Deckt die gezogenen Karten nacheinander auf (Pop-in mit Scale+Fade, 150 ms Versatz) —
// Legendaries bekommen einen goldenen Glow-Rahmen + ✨. Zeigt jetzt die echten
// Katzen-Assets (CardImage, Person 1) statt reiner Text-Platzhalter.
@Composable
fun PullResultScreen(
    cards: List<Card>,
    onDismiss: () -> Unit
) {
    // Gestaffeltes Aufdecken: alle 150 ms eine weitere Karte einblenden.
    var revealedCount by remember(cards) { mutableIntStateOf(0) }
    LaunchedEffect(cards) {
        while (revealedCount < cards.size) {
            delay(150)
            revealedCount++
        }
    }

    val hasLegendary = cards.any { it.rarity == Rarity.LEGENDARY }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when {
                hasLegendary -> "✨ LEGENDARY! ✨"
                cards.size > 1 -> "${cards.size}er-Pull"
                else -> "Ergebnis"
            },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = if (hasLegendary) Color(0xFFFFC107) else Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(if (cards.size > 1) 3 else 1),
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(cards) { index, card ->
                PulledCard(card = card, revealed = index < revealedCount)
            }
        }

        Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B2CBF)),
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        ) {
            Text("Weiter", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PulledCard(card: Card, revealed: Boolean) {
    val rc = card.rarity.color()
    val isLegendary = card.rarity == Rarity.LEGENDARY

    // Pop-in: von 60 % Größe / unsichtbar auf 100 % / sichtbar
    val scale by animateFloatAsState(
        targetValue = if (revealed) 1f else 0.6f,
        animationSpec = tween(260),
        label = "pull_scale_${card.id}"
    )
    val alpha by animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = tween(260),
        label = "pull_alpha_${card.id}"
    )

    Box(
        modifier = Modifier
            .aspectRatio(0.72f)
            .scale(scale)
            .alpha(alpha)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A0F2B))
            .border(if (isLegendary) 3.dp else 2.dp, rc, RoundedCornerShape(12.dp))
    ) {
        CardImage(
            imageAssetName = card.imageAssetName,
            contentDescription = card.name,
            modifier = Modifier.fillMaxSize()
        )
        if (isLegendary) {
            Text(
                "✨",
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(
                    Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)))
                )
                .padding(6.dp)
        ) {
            Text(
                card.rarity.label(),
                style = MaterialTheme.typography.labelSmall,
                color = rc,
                fontWeight = FontWeight.Bold
            )
            Text(
                card.name,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}
