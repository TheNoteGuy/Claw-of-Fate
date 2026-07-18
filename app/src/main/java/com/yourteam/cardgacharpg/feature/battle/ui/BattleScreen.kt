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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yourteam.cardgacharpg.core.model.ActionEvent
import com.yourteam.cardgacharpg.core.model.ActionType
import com.yourteam.cardgacharpg.core.model.BattleLog
import com.yourteam.cardgacharpg.core.model.BattleSide
import com.yourteam.cardgacharpg.core.model.RoundEvent

// Owner: Person 3 (Marc) — FA04: spielt den fertigen BattleLog als Runden-Liste ab.
// Simulation != Animation (siehe GDD): BattleSimulator hat bereits fertig gerechnet, dieser
// Screen konsumiert nur noch den Log für die Darstellung (kein erneutes Würfeln/Rechnen hier).
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
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(log.rounds) { round -> RoundCard(round) }
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { onFinished(log.winner == BattleSide.PLAYER, log) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Zum Ergebnis")
                    }
                }
            }
        }
    }
}

@Composable
private fun RoundCard(round: RoundEvent) {
    ElevatedCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("Runde ${round.roundNumber}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            round.actions.forEach { ActionLine(it) }
        }
    }
}

@Composable
private fun ActionLine(action: ActionEvent) {
    val text = when (action.actionType) {
        ActionType.HEAL ->
            "${action.actorName} heilt ${action.targetName} um ${action.amount} HP"
        ActionType.ATTACK ->
            "${action.actorName} greift ${action.targetName} an: ${action.amount} Schaden" +
                    if (action.targetDefeated) " (besiegt!)" else ""
        ActionType.SKILL ->
            "${action.actorName} setzt seine Fähigkeit gegen ${action.targetName} ein: ${action.amount}" +
                    if (action.targetDefeated) " (besiegt!)" else ""
    }
    Text(text, style = MaterialTheme.typography.bodySmall)
}
