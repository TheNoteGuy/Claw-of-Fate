package com.yourteam.cardgacharpg.feature.battle.ui

// Owner: Person 3 (Marc)
import android.R
import android.content.ClipData
import android.content.ClipDescription
import android.widget.EditText
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.feature.arena.ui.ArenaResultScreen
import com.yourteam.cardgacharpg.feature.battle.data.FormationEntity
import com.yourteam.cardgacharpg.feature.battle.data.FormationRepository
import com.yourteam.cardgacharpg.feature.collection.ui.CardImage
import com.yourteam.cardgacharpg.feature.collection.ui.CardTile
import com.yourteam.cardgacharpg.feature.collection.ui.CollectionScreen
import com.yourteam.cardgacharpg.feature.collection.ui.CollectionViewModel
import com.yourteam.cardgacharpg.feature.collection.ui.EmptyCollectionHint
import com.yourteam.cardgacharpg.feature.collection.ui.FilterBar
import com.yourteam.cardgacharpg.feature.collection.ui.RarityIndicator
import com.yourteam.cardgacharpg.feature.gacha.ui.color
import com.yourteam.cardgacharpg.feature.gacha.ui.label
import kotlinx.coroutines.flow.Flow
import java.text.Normalizer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormationEditorScreen(
    viewModel: FormationEditorViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formationen bearbeiten") },
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
            if (uiState.isLoading) {
                Box(Modifier
                    .fillMaxSize()
                    .padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Scaffold
            }

            val text = remember { mutableStateOf(uiState.formation?.name.orEmpty()) }
            TextField(
                text.value,
                { text.value = it; viewModel.onSetName(it) },
                placeholder = { Text("Name der Formation") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            //FormationGrid(viewModel, uiState.cards)

            Spacer(Modifier.height(24.dp))

            //DraggableCollectionScreen()
        }
    }
}

@Composable
private fun FormationGrid(viewModel: FormationEditorViewModel, cards: List<Card?>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(cards) { card ->
            FormationTile(card = card, viewModel)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)

@Composable
private fun FormationTile(card: Card?, viewModel: FormationEditorViewModel) {
    val dndSelected = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val draggedData = event.toAndroidDragEvent().clipData
                if (draggedData.itemCount > 0) {
                    viewModel.activateCard(draggedData.getItemAt(0).text.toString().toInt())
                    return true
                } else return false
            }
        }
    }

    ElevatedCard(
        modifier = Modifier
            .aspectRatio(0.75f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { event ->
                        event
                            .mimeTypes()
                            .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    }, target = dndSelected
                )
        )
    }
}

@Composable
private fun FormationCardTile(card: Card) {
    val rc = card.rarity.color()
    Box(
        modifier = Modifier
            .aspectRatio(0.75f)
            .clip(RoundedCornerShape(10.dp))
            .background(rc.copy(alpha = 0.15f))
            .border(1.dp, rc, RoundedCornerShape(10.dp))
            .padding(6.dp)
    ) {
        Column {
            Text(card.rarity.label(), style = MaterialTheme.typography.labelSmall, color = rc)
            Text(card.name, style = MaterialTheme.typography.labelMedium, maxLines = 1, fontWeight = FontWeight.SemiBold)
            Text(
                "${card.element.name.lowercase().replaceFirstChar { it.uppercase() }} · ${card.role.name.lowercase().replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableCollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel(),
    onCardClick: (Card) -> Unit = {},
    onOpenGacha: () -> Unit = {} // TEMP
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        FilterBar(
            filter = uiState.filter,
            isEmbedded = true,
            onElementSelected = viewModel::setElementFilter,
            onRoleSelected = viewModel::setRoleFilter,
            onRaritySelected = viewModel::setRarityFilter,
            onLevelBracketSelected = viewModel::setLevelBracketFilter,
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
                    CardTile(
                        modifier = Modifier.dragAndDropSource {
                            detectTapGestures(onLongPress = {
                                startTransfer(
                                    DragAndDropTransferData(
                                        ClipData.newPlainText(
                                            card.id.toString(),
                                            card.id.toString()
                                        )
                                    )
                                )
                            })
                        },
                        card = card,
                        onClick = { onCardClick(card) }
                    )
                }
            }
        }
    }
}



@Composable
fun DraggableCardTile(modifier: Modifier = Modifier, card: Card, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, modifier = Modifier.aspectRatio(0.75f)) {
        Box(modifier = modifier.fillMaxSize()) {
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
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
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