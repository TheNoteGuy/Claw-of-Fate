package com.yourteam.cardgacharpg.feature.gacha.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yourteam.cardgacharpg.feature.gacha.domain.GachaEngine

// Owner: Person 2 (Nico)
// Stateless-ish Composable: liest ausschließlich uiState und ruft ViewModel-Funktionen auf.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GachaScreen(
    viewModel: GachaViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Einmalige Meldungen (z.B. "Nicht genug Gems") als Snackbar zeigen.
    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onMessageShown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gacha") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Zurück"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
            // Gem-Zähler
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    "💎 ${state.gems}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            // Banner-Platzhalter
            Text(
                "Claw of Fate – Katzen-Banner",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))
            PityIndicator(pityCount = state.pityCount, isSoftPity = state.isSoftPity)
            Spacer(Modifier.height(32.dp))

            // Pull-Buttons (deaktiviert, wenn zu wenig Gems oder gerade ein Pull läuft).
            Button(
                onClick = viewModel::onSinglePull,
                enabled = state.canSinglePull,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Single-Pull  (${GachaEngine.SINGLE_PULL_COST} 💎)")
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = viewModel::onTenPull,
                enabled = state.canTenPull,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("10er-Pull  (${GachaEngine.TEN_PULL_COST} 💎)")
            }

            if (state.isPulling) {
                Spacer(Modifier.height(24.dp))
                CircularProgressIndicator()
            }
        }
    }
}