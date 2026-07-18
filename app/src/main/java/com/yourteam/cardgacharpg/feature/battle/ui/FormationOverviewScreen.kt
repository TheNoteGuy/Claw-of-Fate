package com.yourteam.cardgacharpg.feature.battle.ui

// Owner: Person 3 (Marc)
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yourteam.cardgacharpg.feature.battle.data.FormationEntity
import com.yourteam.cardgacharpg.feature.collection.ui.CardImage
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormationOverviewScreen(
    viewModel: FormationOverviewViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onOpenFormationEditor: (Long) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formation auswählen") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            FormationsGrid(uiState.formations, viewModel, onOpenFormationEditor)
        }
    }
}

@Composable
private fun FormationsGrid(formations: List<FormationEntity>,  viewModel: FormationOverviewViewModel, onOpenEditor: (Long) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        //modifier = Modifier.height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(formations, key = { it.id }) { formation -> FormationTile(formation, onOpenEditor) }
        item { FormationCreationTile(viewModel, onOpenEditor) }
    }
}

@Composable
private fun FormationTile(formation: FormationEntity, onOpenEditor: (Long) -> Unit) {
    ElevatedCard(onClick = { onOpenEditor(formation.id) }, modifier = Modifier.aspectRatio(0.75f)) {
        Box(
            modifier = Modifier
                .aspectRatio(0.75f)
                .clip(RoundedCornerShape(10.dp))
                .padding(6.dp)
        ) {
            Column {
                Text("lol" + formation.id + formation.name)
            }
        }
    }
    /*ElevatedCard(onClick = onClick, modifier = Modifier.aspectRatio(0.75f)) {
        Box(modifier = Modifier.fillMaxSize()) {
            CardImage(
                imageAssetName = card.imageAssetName,
                contentDescription = card.name,
                modifier = Modifier.fillMaxSize()
            )
            // Accessibility: Rarity-Form oben links, nicht nur über Farbverlauf/Text unten erkennbar
            RarityIndicator(
                rarity = card.rarity,
                showLabel = false,
                dotSize = 16.dp,
                modifier = Modifier.align(Alignment.TopStart).padding(6.dp)
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
    }*/
}

@Composable
private fun FormationCreationTile(viewModel: FormationOverviewViewModel, onOpenEditor: (Long) -> Unit) {
    ElevatedCard(
        onClick = { onOpenEditor(viewModel.onCreate()) },
        modifier = Modifier.aspectRatio(0.75f),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                Icons.Filled.Add,
                "Formation creating button",
                modifier = Modifier.fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.primary
                    )
            )
        }
    }
}