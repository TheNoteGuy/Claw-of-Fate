package com.yourteam.cardgacharpg.feature.battle.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.feature.collection.ui.CardImage
import com.yourteam.cardgacharpg.feature.gacha.ui.color

// Owner: Person 3 (Marc) — FA03: 3x2-Grid + Seitenleiste, Tap-to-Place
// Interaktion: Slot antippen -> markieren, danach eine Karte in der Liste antippen -> platziert.
// Erneutes Antippen eines belegten Slots leert ihn wieder.
//
// UI-Polish (Abgabe-Sprint): TopAppBar mit Zurueck-Pfeil (⚠ neuer Parameter onBack,
// NavGraph Person 5), dunkler Stahlblau-Verlauf als Hintergrund, goldener Kampf-Button.

private val FormationBgTop = Color(0xFF16233A)
private val FormationBgBottom = Color(0xFF0A101C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormationEditorScreen(
    viewModel: FormationViewModel = hiltViewModel(),
    onStartTestBattle: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedSlot by remember { mutableStateOf<Int?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.duplicateWarning) {
        state.duplicateWarning?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onWarningShown()
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("🛡 Formation", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.background(Brush.verticalGradient(listOf(FormationBgTop, FormationBgBottom)))
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) { CircularProgressIndicator() }
            return@Scaffold
        }

        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text(
                "Tippe einen Slot an, dann eine Karte aus der Liste, um sie zu platzieren.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(16.dp))

            Text("Hintere Reihe", style = MaterialTheme.typography.labelLarge, color = Color.White)
            SlotRow(
                slots = state.slots.subList(3, 6),
                slotOffset = 3,
                selectedSlot = selectedSlot,
                onSlotClick = { index ->
                    selectedSlot = handleSlotClick(index, state.slots, viewModel, selectedSlot)
                }
            )

            Spacer(Modifier.height(12.dp))
            Text("Vordere Reihe", style = MaterialTheme.typography.labelLarge, color = Color.White)
            SlotRow(
                slots = state.slots.subList(0, 3),
                slotOffset = 0,
                selectedSlot = selectedSlot,
                onSlotClick = { index ->
                    selectedSlot = handleSlotClick(index, state.slots, viewModel, selectedSlot)
                }
            )

            Spacer(Modifier.height(24.dp))
            Text("Verfügbare Helden", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(8.dp))

            if (state.availableCards.isEmpty()) {
                Text(
                    "Keine weiteren Helden verfügbar — alle Karten sind bereits platziert oder die Collection ist leer.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.availableCards, key = { it.id }) { card ->
                        AvailableCardTile(
                            card = card,
                            enabled = selectedSlot != null,
                            onClick = {
                                selectedSlot?.let { slot ->
                                    viewModel.placeCard(slot, card)
                                    selectedSlot = null
                                }
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            val filledSlots = state.slots.count { it != null }
            Button(
                onClick = onStartTestBattle,
                enabled = filledSlots > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC107),
                    contentColor = Color(0xFF3A2A00),
                    disabledContainerColor = Color(0xFFFFC107).copy(alpha = 0.3f),
                    disabledContentColor = Color(0xFF3A2A00).copy(alpha = 0.6f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (filledSlots == 0) "Mindestens 1 Held nötig" else "⚔ Testkampf starten ($filledSlots/6)",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Klick auf einen Slot: belegten Slot leeren, leeren Slot (de-)selektieren.
private fun handleSlotClick(
    index: Int,
    slots: List<Card?>,
    viewModel: FormationViewModel,
    currentSelection: Int?
): Int? {
    return if (slots[index] != null) {
        viewModel.clearSlot(index)
        null
    } else {
        if (currentSelection == index) null else index
    }
}

@Composable
private fun SlotRow(
    slots: List<Card?>,
    slotOffset: Int,
    selectedSlot: Int?,
    onSlotClick: (Int) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        slots.forEachIndexed { i, card ->
            val slotIndex = slotOffset + i
            FormationSlotTile(
                card = card,
                isSelected = selectedSlot == slotIndex,
                onClick = { onSlotClick(slotIndex) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FormationSlotTile(
    card: Card?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(0.8f)
            .clip(RoundedCornerShape(10.dp))
            .background(if (card != null) card.rarity.color().copy(alpha = 0.15f) else Color.White.copy(alpha = 0.06f))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (card != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CardImage(
                    imageAssetName = card.imageAssetName,
                    contentDescription = card.name,
                    modifier = Modifier.fillMaxWidth(0.8f).aspectRatio(1f).clip(RoundedCornerShape(6.dp))
                )
                Text(card.name, style = MaterialTheme.typography.labelSmall, maxLines = 1, color = Color.White)
            }
        } else {
            Text("Leer", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Composable
private fun AvailableCardTile(card: Card, enabled: Boolean, onClick: () -> Unit) {
    // UI-Polish: zeigt jetzt das Katzen-Asset (CardImage, Person 1) statt nur Text —
    // man erkennt sofort, WEN man in die Formation setzt.
    Column(
        modifier = Modifier
            .width(90.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(card.rarity.color().copy(alpha = if (enabled) 0.2f else 0.08f))
            .border(1.dp, card.rarity.color(), RoundedCornerShape(10.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardImage(
            imageAssetName = card.imageAssetName,
            contentDescription = card.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(6.dp))
        )
        Spacer(Modifier.height(4.dp))
        Text(card.name, style = MaterialTheme.typography.labelMedium, maxLines = 1, fontWeight = FontWeight.SemiBold, color = Color.White)
        Text("${card.role.name} · Lv.${card.level}", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
    }
}
