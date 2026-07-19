package com.yourteam.cardgacharpg.feature.arena.ui

// Owner: Person 5 (Robin) — Dashboard-Hub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yourteam.cardgacharpg.BuildConfig
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.feature.arena.domain.TrophyManager
import com.yourteam.cardgacharpg.feature.collection.ui.CardImage
import com.yourteam.cardgacharpg.feature.gacha.ui.color
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

// UI-Polish (Abgabe-Sprint) — Wald-Look zum Setting "Der Dunkle Wald":
// dunkelgruener Vertikal-Verlauf als Hintergrund, halbtransparente gruene Karten,
// goldener Kampagnen-Stern, Liga-Badge & Kampagnen-Karte auf gleicher Hoehe (IntrinsicSize).
private val ForestTop = Color(0xFF14351C)
private val ForestBottom = Color(0xFF07130A)
private val ForestCard = Color(0xFF1C3B24)
private val StarGold = Color(0xFFFFC107)

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

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.background(Brush.verticalGradient(listOf(ForestTop, ForestBottom)))
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) { CircularProgressIndicator() }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "🌲 Claw of Fate",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color.White
            )

            CurrencyBar(gems = uiState.gems, gold = uiState.gold)

            // IntrinsicSize.Min + fillMaxHeight: beide Karten immer exakt gleich hoch
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TrophyBadge(trophies = uiState.trophies, modifier = Modifier.weight(1f).fillMaxHeight())
                CampaignProgressCard(
                    starsTotal = uiState.campaignStarsTotal,
                    maxStars = uiState.campaignMaxStars,
                    modifier = Modifier.weight(1f).fillMaxHeight()
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
                    Text("⏭ Cooldown überspringen (Debug)", color = Color.White.copy(alpha = 0.7f))
                }
                OutlinedButton(
                    onClick = viewModel::fakeBattle,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White.copy(alpha = 0.7f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🎲 Fake-Kampf (Trophy-Test, Debug)")
                }
            }
        }
    }
}

@Composable
private fun CurrencyBar(gems: Int, gold: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ForestCard)
    ) {
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
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color(0xFF9CC9A3))
        }
    }
}

@Composable
private fun TrophyBadge(trophies: Int, modifier: Modifier = Modifier) {
    val league = TrophyManager.leagueFor(trophies)
    // Liga-Farbe: Bronze/Silber/Gold — mehr Farbcodierung im Screen
    val leagueColor = when (league.displayName) {
        "Gold" -> StarGold
        "Silber" -> Color(0xFFB8C4CE)
        else -> Color(0xFFCD7F32)
    }
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = ForestCard)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🏆 $trophies",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = league.displayName + "-Liga",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = leagueColor
            )
        }
    }
}

@Composable
private fun CampaignProgressCard(starsTotal: Int, maxStars: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = ForestCard)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$starsTotal / $maxStars ",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "★",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = StarGold
                )
            }
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { if (maxStars > 0) starsTotal.toFloat() / maxStars else 0f },
                color = StarGold,
                trackColor = Color.White.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(4.dp))
            Text(text = "Kampagne", style = MaterialTheme.typography.labelMedium, color = Color(0xFF9CC9A3))
        }
    }
}

@Composable
private fun FormationPreviewCard(
    formationSlots: List<Card?>,
    activeFormationSize: Int,
    cardCount: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ForestCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "⚔ Aktive Formation ($activeFormationSize/6)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
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
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9CC9A3)
            )
            TextButton(onClick = onClick, modifier = Modifier.align(Alignment.End)) {
                Text("Formation bearbeiten")
            }
        }
    }
}

@Composable
private fun FormationRow(slots: List<Card?>, indices: List<Int>) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        indices.forEach { index ->
            FormationSlot(
                card = slots.getOrNull(index),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// UI-Polish: zeigt das Katzen-Icon der platzierten Karte (mit Rarity-Rahmen) statt
// nur eines gefuellten Kaestchens — man sieht auf einen Blick, WER in der Formation steht.
@Composable
private fun FormationSlot(card: Card?, modifier: Modifier = Modifier) {
    if (card != null) {
        Box(
            modifier = modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .border(1.5.dp, card.rarity.color(), RoundedCornerShape(8.dp))
        ) {
            CardImage(
                imageAssetName = card.imageAssetName,
                contentDescription = card.name,
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        Box(
            modifier = modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "+", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.4f))
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
        Text(
            text = "Bereiche",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onOpenCollection,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2C4A7C),
                    contentColor = Color.White          // NEU: sonst dunkelbraune onPrimary-Schrift (schlecht lesbar)
                ),
                modifier = Modifier.weight(1f)
            ) { Text("📚 Collection", fontWeight = FontWeight.Bold) }
            Button(
                onClick = onOpenGacha,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B2CBF),
                    contentColor = Color.White          // NEU
                ),
                modifier = Modifier.weight(1f)
            ) { Text("✨ Gacha", fontWeight = FontWeight.Bold) }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onOpenCampaign,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3E7C4A),
                    contentColor = Color.White          // NEU
                ),
                modifier = Modifier.weight(1f)
            ) { Text("🗺 Kampagne", fontWeight = FontWeight.Bold) }
            Button(
                onClick = onOpenArena,
                colors = ButtonDefaults.buttonColors(
                    containerColor = StarGold,
                    contentColor = Color(0xFF3A2A00)     // unverändert: dunkler Text auf Gold ist bereits gut lesbar
                ),
                modifier = Modifier.weight(1f)
            ) { Text("⚔ Arena", fontWeight = FontWeight.Bold) }
        }
    }
}