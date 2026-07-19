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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.feature.battle.ui.BattlePlaybackContent
import com.yourteam.cardgacharpg.feature.collection.ui.CardImage
import com.yourteam.cardgacharpg.feature.gacha.ui.color

// Owner: Person 5 (Robin)
//
// UI-Polish (Abgabe-Sprint) — "Kolosseum bei Nacht":
// - dunkler Rot-Verlauf (eigene Bereichs-Farbe neben Collection-Blau / Gacha-Violett / Home-Gruen)
// - Katzen-Assets in den Formations-Kacheln (CardImage, Person 1)
// - NEU: Der Arena-Kampf wird jetzt VOR dem Ergebnis via BattlePlaybackContent (Person 3)
//   abgespielt — gleiche Kampf-UI wie Testkampf & Kampagne, statt Sofort-Ergebnis.

private val ArenaBgTop = Color(0xFF3A1220)
private val ArenaBgBottom = Color(0xFF120608)
private val ArenaGold = Color(0xFFFFC107)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArenaScreen(
    viewModel: ArenaViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Playback-Gate: erst wenn der Kampf zu Ende "abgespielt" wurde, kommt das Ergebnis.
    // remember(state.result): reset bei jedem neuen Kampf.
    var showResult by remember(state.result) { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("⚔ Arena", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        modifier = Modifier.background(Brush.verticalGradient(listOf(ArenaBgTop, ArenaBgBottom)))
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val result = state.result
        if (result != null) {
            val log = result.log
            if (log != null && !showResult) {
                // 1) Kampf-Playback (Person 3) — danach erst das Ergebnis aufdecken
                Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                    BattlePlaybackContent(
                        log = log,
                        modifier = Modifier.fillMaxSize(),
                        playerLabel = "Deine Formation",
                        enemyLabel = "Gegner — Arena"
                    ) { finished ->
                        Button(
                            onClick = { showResult = true },
                            enabled = finished,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ArenaGold,
                                contentColor = Color(0xFF3A2A00)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (finished) "Zum Ergebnis" else "Kampf läuft…", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // 2) Ergebnis (Sieg/Niederlage, Trophäen, Gold, Überlebende)
                Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                    ArenaResultScreen(
                        result = result,
                        onDismiss = viewModel::onResultDismissed
                    )
                }
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
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    state.league.displayName + "-Liga",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = when (state.league.displayName) {
                        "Gold" -> ArenaGold
                        "Silber" -> Color(0xFFB8C4CE)
                        else -> Color(0xFFCD7F32)
                    }
                )
            }

            Spacer(Modifier.height(24.dp))
            Text("Gegner", style = MaterialTheme.typography.labelLarge, color = Color.White)
            Spacer(Modifier.height(8.dp))
            FormationGrid(cards = state.opponentDeck)

            Spacer(Modifier.height(24.dp))
            Text("Deine Formation", style = MaterialTheme.typography.labelLarge, color = Color.White)
            Spacer(Modifier.height(8.dp))
            if (state.playerPreview.isEmpty()) {
                Text(
                    "Keine aktive Formation — platziere zuerst Helden im Formations-Editor.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            } else {
                FormationGrid(cards = state.playerPreview)
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = viewModel::onFight,
                enabled = !state.isFighting && state.playerPreview.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ArenaGold,
                    contentColor = Color(0xFF3A2A00),
                    disabledContainerColor = ArenaGold.copy(alpha = 0.35f),
                    disabledContentColor = Color(0xFF3A2A00).copy(alpha = 0.6f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isFighting) "Kampf läuft..." else "⚔ Antreten", fontWeight = FontWeight.Bold)
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
    // UI-Polish: zeigt das Katzen-Asset (CardImage) mit Name/Rolle als Overlay unten
    val rc = card.rarity.color()
    Box(
        modifier = Modifier
            .aspectRatio(0.75f)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF1A0D12))
            .border(1.dp, rc, RoundedCornerShape(10.dp))
    ) {
        CardImage(
            imageAssetName = card.imageAssetName,
            contentDescription = card.name,
            modifier = Modifier.fillMaxSize()
        )
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
                card.name,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                "${card.role.name.lowercase().replaceFirstChar { it.uppercase() }} · Lv.${card.level}",
                style = MaterialTheme.typography.labelSmall,
                color = rc
            )
        }
    }
}
