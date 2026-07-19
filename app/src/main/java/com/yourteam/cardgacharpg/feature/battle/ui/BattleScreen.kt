package com.yourteam.cardgacharpg.feature.battle.ui

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
import com.yourteam.cardgacharpg.core.model.BattleLog
import com.yourteam.cardgacharpg.core.model.BattleSide

// Owner: Person 3 (Marc) — FA04: Testkampf aus dem Formations-Editor.
// Simulation != Animation (GDD): BattleSimulator hat bereits fertig gerechnet, dieser Screen
// spielt den Log via BattlePlaybackContent ab (Formationen + HP-Balken + Kampfverlauf-Box).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleScreen(
    viewModel: BattleViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onFinished: (isVictory: Boolean, log: BattleLog) -> Unit = { _, _ -> }
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Testkampf") }) }
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
                    Button(onClick = onBack) { Text("Zurück zur Formation") }
                }

                state.log != null -> {
                    val log = state.log!!
                    BattlePlaybackContent(
                        log = log,
                        modifier = Modifier.fillMaxSize(),
                        playerLabel = "Deine Formation",
                        enemyLabel = "Gegner (Arena-Pool)"
                    ) { finished ->
                        Button(
                            onClick = { onFinished(log.winner == BattleSide.PLAYER, log) },
                            enabled = finished,
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
