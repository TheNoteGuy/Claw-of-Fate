package com.yourteam.cardgacharpg.feature.collection.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.core.model.Element
import com.yourteam.cardgacharpg.core.model.Rarity
import com.yourteam.cardgacharpg.core.model.Role
import com.yourteam.cardgacharpg.feature.gacha.ui.color

// Owner: Person 1 (Leila) — FA02: Collection-Übersicht mit Filterleiste, Leer-State
//
// UI-Polish (Abgabe-Sprint):
// - Eigene TopAppBar mit Zurück-Pfeil (Navigation kommt vom NavGraph, Person 5)
// - Gacha-Sprung raus aus der Filterleiste (wurde abgeschnitten) -> eigener Promo-Banner
//   ("Gambling-Teaser") unter der Filterleiste, der immer voll sichtbar ist
// - Game-Look: dunkler Blau-Verlauf als Hintergrund (Gacha nutzt bewusst einen
//   ANDEREN, violetten Verlauf -> Bereiche sind sofort unterscheidbar)
// - CardTile: Rarity-farbener Rahmen, Element-Symbol, sattere Text-Overlays

private val CollectionBgTop = Color(0xFF0E1B2E)    // dunkles Nachtblau
private val CollectionBgBottom = Color(0xFF060B14)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel(),
    onCardClick: (Card) -> Unit = {},
    onOpenGacha: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("📚 Collection", fontWeight = FontWeight.Bold) },
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
        modifier = Modifier.background(
            Brush.verticalGradient(listOf(CollectionBgTop, CollectionBgBottom))
        )
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            FilterBar(
                filter = uiState.filter,
                onElementSelected = viewModel::setElementFilter,
                onRoleSelected = viewModel::setRoleFilter,
                onRaritySelected = viewModel::setRarityFilter,
                onLevelBracketSelected = viewModel::setLevelBracketFilter,
                onClear = viewModel::clearFilters
            )

            GachaTeaserBanner(onOpenGacha = onOpenGacha)

            when {
                uiState.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
                uiState.isEmpty -> EmptyCollectionHint(onOpenGacha = onOpenGacha)
                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.cards, key = { it.id }) { card ->
                        CardTile(card = card, onClick = { onCardClick(card) })
                    }
                }
            }
        }
    }
}

/**
 * "Willst du nicht noch mehr Gambling machen?!" — Promo-Banner statt des frueheren
 * Gacha-Buttons in der Filterleiste (der wurde auf schmalen Screens abgeschnitten).
 * Wegklickbar; taucht pro Screen-Besuch einmal auf.
 */
@Composable
private fun GachaTeaserBanner(onOpenGacha: () -> Unit) {
    var dismissed by remember { mutableStateOf(false) }
    if (dismissed) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.horizontalGradient(listOf(Color(0xFF4A1D6E), Color(0xFF7B2CBF)))
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "✨ Dein Glück wartet!",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "Neue legendäre Katzen im Banner — zieh sie dir!",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
        Button(
            onClick = onOpenGacha,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFC107),
                contentColor = Color(0xFF3A2A00)
            ),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text("Gacha ▶", fontWeight = FontWeight.Bold)
        }
        IconButton(onClick = { dismissed = true }, modifier = Modifier.size(28.dp)) {
            Text("✕", color = Color.White.copy(alpha = 0.7f))
        }
    }
}

@Composable
private fun FilterBar(
    filter: CollectionFilterState,
    onElementSelected: (Element?) -> Unit,
    onRoleSelected: (Role?) -> Unit,
    onRaritySelected: (Rarity?) -> Unit,
    onLevelBracketSelected: (LevelBracket?) -> Unit,
    onClear: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterDropdown("Element", Element.entries, filter.element, { it.name }, onElementSelected)
        FilterDropdown("Rolle", Role.entries, filter.role, { it.name }, onRoleSelected)
        FilterDropdown("Seltenheit", Rarity.entries, filter.rarity, { it.name }, onRaritySelected)
        FilterDropdown("Level", LevelBracket.entries, filter.levelBracket, { it.displayLabel }, onLevelBracketSelected)

        if (filter.element != null || filter.role != null || filter.rarity != null || filter.levelBracket != null) {
            TextButton(onClick = onClear) { Text("Zurücksetzen") }
        }
    }
}

@Composable
private fun <T> FilterDropdown(
    label: String,
    options: List<T>,
    selected: T?,
    optionLabel: (T) -> String,
    onSelected: (T?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        AssistChip(
            onClick = { expanded = true },
            label = { Text(selected?.let(optionLabel) ?: label) },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = if (selected != null) Color(0xFF2C4A7C) else Color(0xFF16243D),
                labelColor = Color.White
            ),
            border = null
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Alle") }, onClick = { onSelected(null); expanded = false })
            options.forEach { option ->
                DropdownMenuItem(text = { Text(optionLabel(option)) }, onClick = { onSelected(option); expanded = false })
            }
        }
    }
}

@Composable
private fun CardTile(card: Card, onClick: () -> Unit) {
    val rarityColor = card.rarity.color()
    // Card statt ElevatedCard: nur die Card-Overload hat einen border-Parameter (Rarity-Rahmen)
    Card(
        onClick = onClick,
        modifier = Modifier.aspectRatio(0.75f),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121E30)),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, rarityColor.copy(alpha = 0.8f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CardImage(
                imageAssetName = card.imageAssetName,
                contentDescription = card.name,
                modifier = Modifier.fillMaxSize()
            )
            // Accessibility: Rarity-Form oben links, nicht nur über Farbe erkennbar
            RarityIndicator(
                rarity = card.rarity,
                showLabel = false,
                dotSize = 16.dp,
                modifier = Modifier.align(Alignment.TopStart).padding(6.dp)
            )
            // Stack-Badge (Karten-Stacking): Duplikate erhoehen count statt neue Zeilen
            if (card.count > 1) {
                Text(
                    text = "x${card.count}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFC107),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                        )
                    )
                    .padding(8.dp)
            ) {
                Text(
                    card.name,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 1
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "${card.element.symbol()} ${card.rarity.name}",
                        style = MaterialTheme.typography.labelSmall,
                        color = rarityColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Lv.${card.level}", style = MaterialTheme.typography.labelSmall, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun EmptyCollectionHint(onOpenGacha: () -> Unit) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🐾", style = MaterialTheme.typography.displayMedium)
            Spacer(Modifier.height(8.dp))
            Text("Noch keine Karten gesammelt.", color = Color.White)
            Spacer(Modifier.height(4.dp))
            Text(
                "Gehe zum Gacha, um Helden zu ziehen.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onOpenGacha,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B2CBF))
            ) { Text("✨ Zum Gacha") }
        }
    }
}
