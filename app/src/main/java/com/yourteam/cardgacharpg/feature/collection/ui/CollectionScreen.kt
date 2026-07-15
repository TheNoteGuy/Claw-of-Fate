package com.yourteam.cardgacharpg.feature.collection.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.core.model.Element
import com.yourteam.cardgacharpg.core.model.Rarity
import com.yourteam.cardgacharpg.core.model.Role

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Owner: Person 1 (Leila) — FA02: Collection-Übersicht mit Filterleiste, Leer-State
//
// TEMP (Person 2 / Nico): onOpenGacha + "Gacha"-Button in der Filterleiste, damit man beim
// Testen ohne fertigen NavGraph vom Collection-Screen zum Gacha springen kann.
// Kann raus, sobald der echte NavGraph/Navigation-Hub (Person 5) steht.

@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel(),
    onCardClick: (Card) -> Unit = {},
    onOpenGacha: () -> Unit = {} // TEMP
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        FilterBar(
            filter = uiState.filter,
            onElementSelected = viewModel::setElementFilter,
            onRoleSelected = viewModel::setRoleFilter,
            onRaritySelected = viewModel::setRarityFilter,
            onClear = viewModel::clearFilters,
            onOpenGacha = onOpenGacha // TEMP
        )

        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
            uiState.isEmpty -> EmptyCollectionHint()
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

@Composable
private fun FilterBar(
    filter: CollectionFilterState,
    onElementSelected: (Element?) -> Unit,
    onRoleSelected: (Role?) -> Unit,
    onRaritySelected: (Rarity?) -> Unit,
    onClear: () -> Unit,
    onOpenGacha: () -> Unit = {} // TEMP
) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()) // TEMP: verhindert Clipping durch den Extra-Button
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterDropdown("Element", Element.entries, filter.element, { it.name }, onElementSelected)
        FilterDropdown("Rolle", Role.entries, filter.role, { it.name }, onRoleSelected)
        FilterDropdown("Seltenheit", Rarity.entries, filter.rarity, { it.name }, onRaritySelected)

        // TEMP: Sprung zum Gacha zum Testen (raus, sobald echte Navigation existiert)
        Button(onClick = onOpenGacha) { Text("Gacha ▶") }

        if (filter.element != null || filter.role != null || filter.rarity != null) {
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
        AssistChip(onClick = { expanded = true }, label = { Text(selected?.let(optionLabel) ?: label) })
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Alle") }, onClick = { onSelected(null); expanded = false })
            options.forEach { option ->
                DropdownMenuItem(text = { Text(optionLabel(option)) }, onClick = { onSelected(option); expanded = false })
            }
        }
    }
}

// Platzhalter-Kartenvorschau, bis imageAssetName final an ein Bild-Loading angebunden ist
@Composable
private fun CardTile(card: Card, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, modifier = Modifier.aspectRatio(0.75f)) {
        Box(modifier = Modifier.fillMaxSize()) {
            CardImage(
                imageAssetName = card.imageAssetName,
                contentDescription = card.name,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                        )
                    )
                    .padding(8.dp)
            ) {
                Text(card.name, style = MaterialTheme.typography.labelMedium, color = Color.White, maxLines = 1)
                Text(card.rarity.name, style = MaterialTheme.typography.labelSmall, color = Color.White)
                Text("Lv. ${card.level}", style = MaterialTheme.typography.labelSmall, color = Color.White)
            }
        }
    }
}

@Composable
private fun EmptyCollectionHint() {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Noch keine Karten gesammelt.")
            Spacer(Modifier.height(8.dp))
            Text("Gehe zum Gacha, um Helden zu ziehen.", style = MaterialTheme.typography.bodySmall)
        }
    }
}