package com.yourteam.cardgacharpg.feature.gacha.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yourteam.cardgacharpg.feature.gacha.domain.GachaEngine

// Owner: Person 2 (Nico)
// Stateless-ish Composable: liest ausschließlich uiState und ruft ViewModel-Funktionen auf.
//
// UI-Polish (Abgabe-Sprint) — "Casino-Nacht"-Look, bewusst ANDERS als der blaue
// Collection-Screen (Wiedererkennung der Bereiche):
// - violetter Nachthimmel-Verlauf + pulsierende Glitzer-Sterne (InfiniteTransition)
// - goldener Pull-Button (der 10er-Pull als Hauptaktion — mehr Value pro Gem!)
// - Pity-Anzeige nach UNTEN verschoben + (i)-Info-Dialog, der das Pity-System erklärt

private val GachaBgTop = Color(0xFF2A0E45)     // tiefes Violett
private val GachaBgBottom = Color(0xFF0D0518)
private val GachaGold = Color(0xFFFFC107)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GachaScreen(
    viewModel: GachaViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showPityInfo by remember { mutableStateOf(false) }

    // Einmalige Meldungen (z.B. "Nicht genug Gems") als Snackbar zeigen.
    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onMessageShown()
        }
    }

    if (showPityInfo) {
        PityInfoDialog(onDismiss = { showPityInfo = false })
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Gacha", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Zurück"
                        )
                    }
                },
                actions = {
                    Text(
                        "💎 ${state.gems}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.background(
            Brush.verticalGradient(listOf(GachaBgTop, GachaBgBottom))
        )
    ) { padding ->
        // Ergebnis-Overlay hat Vorrang, sobald ein Pull abgeschlossen ist.
        val pulled = state.lastPull
        if (pulled != null) {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                PullResultScreen(cards = pulled, onDismiss = viewModel::onResultDismissed)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))
            SparkleBanner()

            Spacer(Modifier.weight(1f))

            // Pull-Buttons (deaktiviert, wenn zu wenig Gems oder gerade ein Pull läuft).
            OutlinedButton(
                onClick = viewModel::onSinglePull,
                enabled = state.canSinglePull,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Single-Pull  (${GachaEngine.SINGLE_PULL_COST} 💎)")
            }
            Spacer(Modifier.height(12.dp))
            // 10er-Pull als goldene Hauptaktion (100 💎 Rabatt gegenüber 10 Einzel-Pulls)
            Button(
                onClick = viewModel::onTenPull,
                enabled = state.canTenPull,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GachaGold,
                    contentColor = Color(0xFF3A2A00),
                    disabledContainerColor = GachaGold.copy(alpha = 0.3f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "✨ 10er-Pull  (${GachaEngine.TEN_PULL_COST} 💎) ✨",
                    fontWeight = FontWeight.Bold
                )
            }

            if (state.isPulling) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator(color = GachaGold)
            }

            Spacer(Modifier.height(20.dp))

            // Pity-Anzeige unten, mit Info-Button (was bedeutet "Pity" überhaupt?)
            PityIndicator(
                pityCount = state.pityCount,
                isSoftPity = state.isSoftPity,
                onInfoClick = { showPityInfo = true }
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

/** Banner-Titel mit pulsierenden Glitzer-Sternen (InfiniteTransition, GPU-günstig). */
@Composable
private fun SparkleBanner() {
    val transition = rememberInfiniteTransition(label = "sparkle")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle_phase"
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        // Glitzer-Sterne: gegenläufig pulsierend (phase / 1-phase) um den Titel verteilt
        Sparkle(alpha = phase, scale = 0.8f + phase * 0.5f, offsetX = (-130).dp, offsetY = (-24).dp)
        Sparkle(alpha = 1f - phase, scale = 0.6f + (1f - phase) * 0.6f, offsetX = 130.dp, offsetY = (-14).dp)
        Sparkle(alpha = phase, scale = 0.5f + phase * 0.4f, offsetX = 110.dp, offsetY = 30.dp)
        Sparkle(alpha = 1f - phase, scale = 0.7f + (1f - phase) * 0.4f, offsetX = (-105).dp, offsetY = 34.dp)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "🐱 Claw of Fate",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                "Katzen-Banner",
                style = MaterialTheme.typography.titleMedium,
                color = GachaGold,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Legendary-Chance: ${(GachaEngine.BASE_LEGENDARY_RATE * 100).toInt()} % — steigend ab Pull ${GachaEngine.SOFT_PITY_START}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun Sparkle(
    alpha: Float,
    scale: Float,
    offsetX: androidx.compose.ui.unit.Dp,
    offsetY: androidx.compose.ui.unit.Dp
) {
    Text(
        "✦",
        color = GachaGold,
        fontSize = 20.sp,
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .alpha(alpha.coerceIn(0f, 1f))
            .scale(scale)
    )
}

/** Erklärt das Pity-System in Spielersprache (Info-Button an der Pity-Anzeige). */
@Composable
private fun PityInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Was ist Pity? 🍀") },
        text = {
            Text(
                "Das Pity-System schützt dich vor Dauer-Pech:\n\n" +
                        "• Jeder Pull ohne Legendary erhöht deinen Pity-Zähler um 1.\n" +
                        "• Ab Pull ${GachaEngine.SOFT_PITY_START} (Soft-Pity) steigt deine " +
                        "Legendary-Chance mit jedem weiteren Pull deutlich an.\n" +
                        "• Spätestens bei Pull ${GachaEngine.HARD_PITY} (Hard-Pity) ist ein " +
                        "Legendary GARANTIERT.\n\n" +
                        "Sobald du ein Legendary ziehst, startet der Zähler wieder bei 0."
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Verstanden!") }
        }
    )
}
