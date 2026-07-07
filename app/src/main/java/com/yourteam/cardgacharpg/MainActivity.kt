package com.yourteam.cardgacharpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dagger.hilt.android.AndroidEntryPoint
import com.yourteam.cardgacharpg.core.ui.theme.CardGachaRPGTheme
import com.yourteam.cardgacharpg.feature.collection.ui.CollectionScreen
import com.yourteam.cardgacharpg.feature.gacha.ui.GachaScreen

// Single-activity host.
//
// WICHTIG (temporär): Solange NavGraph.kt noch nicht implementiert ist, schalten wir hier
// per einfachem State zwischen Collection und Gacha um, damit man den Gacha-Flow im Emulator
// testen kann. Sobald der echte NavGraph (Person 5) steht, ersetzen durch:
//
//   CardGachaRPGTheme { NavGraph() }
//
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardGachaRPGTheme {
                // TEMP: primitiver Screen-Umschalter (ersetzt später der NavGraph)
                var screen by remember { mutableStateOf("collection") }
                when (screen) {
                    "gacha" -> {
                        BackHandler { screen = "collection" } // Hardware-/Gesten-Zurück
                        GachaScreen(onBack = { screen = "collection" }) // Back-Pfeil oben links
                    }
                    else -> CollectionScreen(onOpenGacha = { screen = "gacha" })
                }
            }
        }
    }
}