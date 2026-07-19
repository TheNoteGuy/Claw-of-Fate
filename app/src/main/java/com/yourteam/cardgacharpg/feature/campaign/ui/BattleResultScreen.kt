package com.yourteam.cardgacharpg.feature.campaign.ui

// Owner: Person 4 (Yassin) — PvE variant
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourteam.cardgacharpg.core.ui.theme.CardGachaRPGTheme

@Composable
fun PveBattleResultScreen(
    isVictory: Boolean,
    starsEarned: Int,
    levelId: Int,
    // Belohnungen aus CompleteCampaignLevelUseCase (0 = nichts anzeigen, z.B. bei Niederlage)
    goldReward: Int = 0,
    gemReward: Int = 0,
    xpPotionReward: Int = 0,
    onContinue: () -> Unit
) {
    val backgroundColor = if (isVictory) Color(0xFF1B2612) else Color(0xFF261212)
    val accentColor = if (isVictory) Color(0xFF4E6B3E) else Color(0xFF6B3E3E)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(backgroundColor, Color.Black)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            // Titel / Icon
            Surface(
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.2f),
                modifier = Modifier.size(120.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, accentColor)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isVictory) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = if (isVictory) Color.Green else Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (isVictory) "SIEG!" else "NIEDERLAGE",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = if (isVictory) Color.White else Color(0xFFFF4444)
            )

            Text(
                text = "Level $levelId",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sterne-Anzeige (immer sichtbar für einheitliches Layout)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val isFilled = index < starsEarned
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = if (isFilled) Color(0xFFFFD700) else Color.White.copy(alpha = 0.1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isVictory) {
                Text(
                    text = when(starsEarned) {
                        3 -> "Perfekt abgeschlossen!"
                        2 -> "Gute Arbeit!"
                        else -> "Level geschafft!"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray
                )
            } else {
                Text(
                    text = "Deine Helden brauchen mehr Training.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            // Belohnungs-Anzeige: was der Sieg konkret eingebracht hat (inkl. XP-Traenke,
            // damit der Spieler die neue Leveling-Ressource auch SIEHT — FA06-Zufluss)
            if (isVictory && (goldReward > 0 || gemReward > 0 || xpPotionReward > 0)) {
                Spacer(modifier = Modifier.height(24.dp))
                Surface(
                    color = Color.Black.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (goldReward > 0) RewardItem(icon = "🪙", amount = goldReward, label = "Gold")
                        if (gemReward > 0) RewardItem(icon = "💎", amount = gemReward, label = "Gems")
                        if (xpPotionReward > 0) RewardItem(icon = "🧪", amount = xpPotionReward, label = "XP-Tränke")
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (isVictory) "WEITER" else "ZURÜCK ZUR KARTE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun RewardItem(icon: String, amount: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 22.sp)
        Text(
            text = "+$amount",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
    }
}

@Preview
@Composable
private fun VictoryPreview() {
    CardGachaRPGTheme {
        PveBattleResultScreen(
            isVictory = true,
            starsEarned = 3,
            levelId = 1,
            goldReward = 55,
            gemReward = 50,
            xpPotionReward = 2,
            onContinue = {}
        )
    }
}

@Preview
@Composable
private fun DefeatPreview() {
    CardGachaRPGTheme {
        PveBattleResultScreen(
            isVictory = false,
            starsEarned = 0,
            levelId = 10,
            onContinue = {}
        )
    }
}
