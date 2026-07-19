package com.yourteam.cardgacharpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.yourteam.cardgacharpg.core.ui.theme.CardGachaRPGTheme
import com.yourteam.cardgacharpg.navigation.NavGraph
import android.graphics.Color as AndroidColor
import androidx.activity.SystemBarStyle

// Single-activity host.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-Edge: App zeichnet ihren eigenen Hintergrund bis unter Status-/Navigationsleiste,
        // statt dass das System dort die Default-Fensterfarbe (weißer Balken!) zeigt.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        )

        setContent {
            CardGachaRPGTheme {
                NavGraph()
            }
        }
    }
}