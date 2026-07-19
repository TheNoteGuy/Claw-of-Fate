package com.yourteam.cardgacharpg.feature.arena.ui

// Owner: Person 5 (Robin) — Dashboard-Hub

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yourteam.cardgacharpg.BuildConfig
import com.yourteam.cardgacharpg.feature.arena.domain.TrophyManager

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onOpenCollection: () -> Unit = {},
    onOpenGacha: () -> Unit = {},
    onOpenFormation: () -> Unit = {},
    onOpenCampaign: () -> Unit = {},
    onOpenArena: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val weeklyRewardMessage by viewModel.weeklyRewardMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(weeklyRewardMessage) {
        weeklyRewardMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.consumeWeeklyRewardMessage()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) { CircularProgressIndicator() }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CurrencyBar(gems = uiState.gems, gold = uiState.gold)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TrophyBadge(trophies = uiState.trophies, modifier = Modifier.weight(1f))
                CampaignProgressCard(
                    starsTotal = uiState.campaignStarsTotal,
                    maxStars = uiState.campaignMaxStars,
                    modifier = Modifier.weight(1f)
                )
            }

            FormationPreviewCard(
                formationSlots = uiState.formationSlots,
                activeFormationSize = uiState.activeFormationSize,
                cardCount = uiState.cardCount,
                onClick = onOpenFormation
            )

            NavigationHub(
                onOpenCollection = onOpenCollection,
                onOpenGacha = onOpenGacha,
                onOpenFormation = onOpenFormation,
                onOpenCampaign = onOpenCampaign,
                onOpenArena = onOpenArena
            )

            Button(onClick = viewModel::claimWeeklyReward, modifier = Modifier.fillMaxWidth()) {
                Text("🎁 Weekly Reward abholen")
            }

            // Debug-Werkzeuge nur in Debug-Builds sichtbar (BuildConfig.DEBUG) —
            // im Release/Abgabe-Build (assembleRelease) verschwinden beide Buttons.
            if (BuildConfig.DEBUG) {
                TextButton(onClick = viewModel::skipCooldownAndClaim, modifier = Modifier.fillMaxWidth()) {
                    Text("⏭ Cooldown überspringen (Debug)")
                }
                OutlinedButton(onClick = viewModel::fakeBattle, modifier = Modifier.fillMaxWidth()) {
                    Text("🎲 Fake-Kampf (Trophy-Test, Debug)")
                }
            }
        }
    }
}

@Composable
private fun CurrencyBar(gems: Int, gold: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            CurrencyItem(icon = "💎", label = "Gems", value = gems)
            CurrencyItem(icon = "🪙", label = "Gold", value = gold)
        }
    }
}

@Composable
private fun CurrencyItem(icon: String, label: String, value: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = icon, style = MaterialTheme.typography.titleLarge)
        Column {
            Text(text = value.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = label, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun TrophyBadge(trophies: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "🏆 $trophies", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                text = TrophyManager.leagueFor(trophies).displayName + "-Liga",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun CampaignProgressCard(starsTotal: Int, maxStars: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$starsTotal / $maxStars ★",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { if (maxStars > 0) starsTotal.toFloat() / maxStars else 0f },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(4.dp))
            Text(text = "Kampagne", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun FormationPreviewCard(
    formationSlots: List<Boolean>,
    activeFormationSize: Int,
    cardCount: Int,
    onClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Aktive Formation ($activeFormationSize/6)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))

            // Echtes Slot-Layout aus der DB: hintere Reihe (Slots 3-5) oben,
            // vordere Reihe (Slots 0-2) unten — wie im Formations-Editor / Kampf.
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormationRow(slots = formationSlots, indices = listOf(3, 4, 5))
                FormationRow(slots = formationSlots, indices = listOf(0, 1, 2))
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = if (cardCount == 0) "Noch keine Karten — geh zum Gacha!" else "$cardCount Karten in deiner Sammlung",
                style = MaterialTheme.typography.bodySmall
            )
            TextButton(onClick = onClick, modifier = Modifier.align(Alignment.End)) {
                Text("Formation bearbeiten")
            }
        }
    }
}

@Composable
private fun FormationRow(slots: List<Boolean>, indices: List<Int>) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        indices.forEach { index ->
            FormationSlot(
                filled = slots.getOrElse(index) { false },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FormationSlot(filled: Boolean, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(32.dp),
        tonalElevation = if (filled) 4.dp else 0.dp,
        color = if (filled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    ) {
        if (filled) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "⚔", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun NavigationHub(
    onOpenCollection: () -> Unit,
    onOpenGacha: () -> Unit,
    onOpenFormation: () -> Unit,
    onOpenCampaign: () -> Unit,
    onOpenArena: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Bereiche", style = MaterialTheme.typography.titleMedium)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onOpenCollection, modifier = Modifier.weight(1f)) { Text("Collection") }
            OutlinedButton(onClick = onOpenGacha, modifier = Modifier.weight(1f)) { Text("Gacha") }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onOpenCampaign, modifier = Modifier.weight(1f)) { Text("Kampagne") }
            Button(onClick = onOpenArena, modifier = Modifier.weight(1f)) { Text("Arena") }
        }
    }
}