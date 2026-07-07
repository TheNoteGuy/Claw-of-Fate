package com.yourteam.cardgacharpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import com.yourteam.cardgacharpg.core.ui.theme.CardGachaRPGTheme
import com.yourteam.cardgacharpg.feature.collection.ui.CollectionScreen

// Single-activity host.
//
// WICHTIG (temporär): Solange NavGraph.kt noch nicht implementiert ist, wird hier
// direkt CollectionScreen() geladen, damit Person 1 (Leila) ihre UI im Emulator
// testen kann. Sobald NavGraph steht, ersetzen durch:
//
//   CardGachaRPGTheme { NavGraph() }
//
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardGachaRPGTheme {
                CollectionScreen()
            }
        }
    }
}
