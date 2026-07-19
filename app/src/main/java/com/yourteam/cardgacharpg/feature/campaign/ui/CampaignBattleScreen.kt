package com.yourteam.cardgacharpg.feature.campaign.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yourteam.cardgacharpg.feature.battle.ui.BattlePlaybackContent
import com.yourteam.cardgacharpg.feature.campaign.domain.CampaignBattleOutcome

// Owner: Person 4 (Yassin) — FA04: spielt den Kampagnen-Kampf ab (Playback-UI von Person 3)
// und leitet nach Abschluss mit dem fertigen Outcome (Sterne + Belohnungen) weiter.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignBattleScreen(
    viewModel: CampaignBattleViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onFinished: (CampaignBattleOutcome) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Level ${state.levelId}: Der Dunkle Wald") }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }

                state.errorMessage != null -> Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(state.errorMessage ?: "")
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onBack) { Text("Zurück zur Karte") }
                }

                state.log != null -> {
                    val log = state.log!!
                    val outcome = state.outcome
                    BattlePlaybackContent(
                        log = log,
                        modifier = Modifier.fillMaxSize(),
                        playerLabel = "Deine Formation",
                        enemyLabel = "Gegner — Der Dunkle Wald"
                    ) { finished ->
                        Button(
                            onClick = { outcome?.let(onFinished) },
                            enabled = finished && outcome != null,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (finished) "Zum Ergebnis" else "Kampf läuft…")
                        }
                    }
                }
            }
        }
    }
}
