package com.yourteam.cardgacharpg.feature.arena.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.feature.gacha.ui.color
import com.yourteam.cardgacharpg.feature.gacha.ui.label

// Owner: Person 5 (Robin)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArenaScreen(
    viewModel: ArenaViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Arena") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val result = state.result
        if (result != null) {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                ArenaResultScreen(
                    result = result,
                    onDismiss = viewModel::onResultDismissed
                )
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            // Trophäen/Liga-Kopfzeile
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "🏆 ${state.trophies}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    state.league.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(24.dp))
            Text("Gegner", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            FormationGrid(cards = state.opponentDeck)

            Spacer(Modifier.height(24.dp))
            Text("Deine Formation", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            if (state.playerPreview.isEmpty()) {
                Text(
                    "Keine aktive Formation — platziere zuerst Helden im Formations-Editor.",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                FormationGrid(cards = state.playerPreview)
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = viewModel::onFight,
                enabled = !state.isFighting && state.playerPreview.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isFighting) "Kampf läuft..." else "Antreten")
            }
            if (state.isFighting) {
                Spacer(Modifier.height(16.dp))
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun FormationGrid(cards: List<Card>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cards, key = { it.id }) { card -> ArenaCardTile(card) }
    }
}

@Composable
private fun ArenaCardTile(card: Card) {
    val rc = card.rarity.color()
    Box(
        modifier = Modifier
            .aspectRatio(0.75f)
            .clip(RoundedCornerShape(10.dp))
            .background(rc.copy(alpha = 0.15f))
            .border(1.dp, rc, RoundedCornerShape(10.dp))
            .padding(6.dp)
    ) {
        Column {
            Text(card.rarity.label(), style = MaterialTheme.typography.labelSmall, color = rc)
            Text(card.name, style = MaterialTheme.typography.labelMedium, maxLines = 1, fontWeight = FontWeight.SemiBold)
            Text(
                "${card.element.name.lowercase().replaceFirstChar { it.uppercase() }} · ${card.role.name.lowercase().replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}