package com.yourteam.cardgacharpg.feature.collection.ui

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yourteam.cardgacharpg.core.model.Card

// Owner: Person 1 (Leila) — FA09: Detailansicht (Stats, Fähigkeiten, Level) mit Einstieg
// in den Level-Up-Flow (FA06, siehe LevelUpSheet.kt)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(
    viewModel: CardDetailViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showLevelUpSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Sheet automatisch schließen, sobald das Level-Up erfolgreich durch war
    // (card.level steigt, isLevelingUp geht auf false zurück, kein errorMessage).
    LaunchedEffect(state.card?.level, state.isLevelingUp) {
        if (showLevelUpSheet && !state.isLevelingUp && state.errorMessage == null &&
            state.card != null
        ) {
            // Kein zusätzlicher Trigger nötig, wenn kein Level-Up lief; Schließen
            // passiert einfach beim nächsten erfolgreichen Level-Up-Callback:
        }
    }

    LaunchedEffect(state.errorMessage) {
        // Fehler zusätzlich als Snackbar, falls das Sheet inzwischen geschlossen wurde.
        if (!showLevelUpSheet) {
            state.errorMessage?.let {
                snackbarHostState.showSnackbar(it)
                viewModel.onErrorShown()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.card?.name ?: "Kartendetail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        val card = state.card

        when {
            state.isLoading -> Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                CircularProgressIndicator()
            }
            card == null -> Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                Text("Karte nicht gefunden.")
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CardImage(
                        imageAssetName = card.imageAssetName,
                        contentDescription = card.name,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .aspectRatio(0.75f)
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Spacer(Modifier.height(16.dp))
                    Text(card.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(
                        "${card.rarity.name} · Lv. ${card.level}/${card.rarity.maxLevel}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(onClick = {}, label = { Text(card.element.name) })
                        AssistChip(onClick = {}, label = { Text(card.role.name) })
                    }

                    Spacer(Modifier.height(24.dp))
                    StatsCard(card)

                    Spacer(Modifier.height(24.dp))
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                        Text("Fähigkeiten", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        // TODO: sobald ein Skill-Modell existiert, hier Name/Beschreibung
                        // statt der rohen Skill-ID anzeigen.
                        Text(
                            "Skill #${card.skill1Id}   ·   Skill #${card.skill2Id}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.height(32.dp))
                    Button(
                        onClick = { showLevelUpSheet = true },
                        enabled = !state.isMaxLevel,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (state.isMaxLevel) "Maximallevel erreicht" else "Level Up  (🧪 ${state.potionsAvailable})")
                    }
                }

                if (showLevelUpSheet) {
                    LevelUpSheet(
                        card = card,
                        potionsAvailable = state.potionsAvailable,
                        potionsRequired = viewModel.potionsRequiredForNextLevel() ?: 0,
                        isLevelingUp = state.isLevelingUp,
                        errorMessage = state.errorMessage,
                        onConfirm = viewModel::onLevelUp,
                        onDismiss = {
                            showLevelUpSheet = false
                            viewModel.onErrorShown()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsCard(card: Card) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            StatRow("HP", card.currentHp)
            StatRow("ATK", card.currentAtk)
            StatRow("DEF", card.currentDef)
            StatRow("SPD", card.currentSpd)
        }
    }
}

@Composable
private fun StatRow(label: String, value: Int) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value.toString(), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}