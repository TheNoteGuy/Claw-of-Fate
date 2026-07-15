package com.yourteam.cardgacharpg.feature.campaign.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yourteam.cardgacharpg.core.ui.theme.CardGachaRPGTheme
import com.yourteam.cardgacharpg.feature.campaign.data.LevelProgressEntity

// Owner: Person 4 (Yassin)
// Ein organischerer "Wald-Pfad" für Akt 1.
@Composable
fun CampaignMapScreen(
    viewModel: CampaignViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CampaignMapContent(
        state = state,
        onBack = onBack,
        onLevelClicked = { viewModel.onLevelClicked(it) }
    )

    // Das Detail-Sheet anzeigen, wenn ein Level ausgewählt wurde
    state.selectedLevel?.let { level ->
        LevelDetailSheet(
            level = level,
            onDismiss = { viewModel.onDetailSheetDismissed() },
            onStartBattle = { levelId ->
                // TODO: Navigation zum Kampf-Screen
                viewModel.onDetailSheetDismissed()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CampaignMapContent(
    state: CampaignUiState,
    onBack: () -> Unit = {},
    onLevelClicked: (Int) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Akt 1: Der dunkle Wald", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1B2612)),
                actions = {
                    Surface(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = CircleShape,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = "⭐ ${state.totalStarsCollected} / 30",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFFFFD700)
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFF1B2612)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Green)
            }
        } else {
            // Konstanten für das Pfad-Layout
            val nodeHeight = 110.dp
            val spacerHeight = 80.dp // Erhöht
            val horizontalPadding = 60.dp
            val circleRadius = 35.dp
            val verticalPadding = 60.dp // Erhöht

            // Ein scrollbarer Pfad
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1B2612), Color(0xFF0D1408))
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .drawBehind {
                            val center = size.width / 2
                            val nodeHeightPx = nodeHeight.toPx()
                            val spacerPx = spacerHeight.toPx()
                            val startY = verticalPadding.toPx() + circleRadius.toPx()

                            for (i in 0 until state.levels.size - 1) {
                                val l1 = state.levels[i]
                                val l2 = state.levels[i + 1]

                                val x1 = center + (if (i % 2 == 0) (-horizontalPadding).toPx() else horizontalPadding.toPx())
                                val y1 = startY + i * (nodeHeightPx + spacerPx)

                                val x2 = center + (if ((i + 1) % 2 == 0) (-horizontalPadding).toPx() else horizontalPadding.toPx())
                                val y2 = startY + (i + 1) * (nodeHeightPx + spacerPx)

                                // Pfad-Design: Erdiges Braun für freigeschaltete Wege, Dunkel/Gepunktet für gesperrte
                                val isPathUnlocked = l1.isUnlocked && l2.isUnlocked
                                drawLine(
                                    color = if (isPathUnlocked) Color(0xFF8B5A2B) else Color(0xFF2A2A2A).copy(alpha = 0.5f),
                                    start = Offset(x1, y1),
                                    end = Offset(x2, y2),
                                    strokeWidth = if (isPathUnlocked) 14f else 6f,
                                    cap = StrokeCap.Round,
                                    pathEffect = if (isPathUnlocked) null else PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                                )
                            }
                        }
                        .padding(vertical = verticalPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    state.levels.forEachIndexed { index, level ->
                        LevelNode(
                            level = level,
                            horizontalOffset = if (index % 2 == 0) (-horizontalPadding) else horizontalPadding,
                            onClick = { if (level.isUnlocked) onLevelClicked(level.levelId) }
                        )

                        if (level.levelId < LevelProgressEntity.TOTAL_LEVELS) {
                            Spacer(modifier = Modifier.height(spacerHeight))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LevelNode(
    level: LevelProgressEntity,
    horizontalOffset: Dp,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .offset(x = horizontalOffset)
            .width(100.dp)
            .height(110.dp), // Feste Höhe für präzise Pfad-Berechnung
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(
                    if (level.isUnlocked) Color(0xFF4E6B3E) else Color(0xFF2A2A2A)
                )
                .clickable(enabled = level.isUnlocked, onClick = onClick)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            if (!level.isUnlocked) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray)
            } else {
                Text(
                    text = level.levelId.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        if (level.isUnlocked) {
            // Sterne unter dem Kreis
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(3) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (index < level.stars) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f)
                    )
                }
            }
        }
        
        Text(
            text = if (level.levelId == 10) "BOSS" else "Wald",
            style = MaterialTheme.typography.labelSmall,
            color = if (level.isUnlocked) Color.LightGray else Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CampaignMapScreenPreview() {
    CardGachaRPGTheme {
        CampaignMapContent(
            state = CampaignUiState(
                levels = listOf(
                    LevelProgressEntity(1, isUnlocked = true, stars = 3),
                    LevelProgressEntity(2, isUnlocked = true, stars = 2),
                    LevelProgressEntity(3, isUnlocked = true, stars = 0),
                    LevelProgressEntity(4, isUnlocked = false, stars = 0),
                    LevelProgressEntity(10, isUnlocked = false, stars = 0)
                ),
                isLoading = false
            )
        )
    }
}
