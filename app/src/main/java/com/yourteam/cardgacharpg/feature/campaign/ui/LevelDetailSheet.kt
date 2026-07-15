package com.yourteam.cardgacharpg.feature.campaign.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourteam.cardgacharpg.core.ui.theme.CardGachaRPGTheme
import com.yourteam.cardgacharpg.feature.campaign.data.LevelProgressEntity

// Owner: Person 4 (Yassin)
// Ein modales BottomSheet (oder Dialog), das Infos zum gewählten Level anzeigt.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelDetailSheet(
    level: LevelProgressEntity,
    onDismiss: () -> Unit,
    onStartBattle: (Int) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1B2612), // Passend zum Wald-Theme
        contentColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp, start = 24.dp, end = 24.dp, top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (level.levelId == 10) "BOSS: Der Waldwächter" else "Level ${level.levelId}: Waldpfad",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (level.levelId == 10) Color(0xFFFF4444) else Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sterne-Anzeige
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = if (index < level.stars) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Bereite deine Helden vor! Die Gegner in diesem Gebiet sind tückisch.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onStartBattle(level.levelId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4E6B3E),
                    contentColor = Color.White
                ),
                shape = CircleShape
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("KAMPF STARTEN", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Preview
@Composable
private fun LevelDetailSheetPreview() {
    CardGachaRPGTheme {
        Surface(color = Color.Black) {
            LevelDetailSheet(
                level = LevelProgressEntity(levelId = 1, isUnlocked = true, stars = 2),
                onDismiss = {},
                onStartBattle = {}
            )
        }
    }
}
