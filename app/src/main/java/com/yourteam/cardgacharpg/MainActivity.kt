package com.yourteam.cardgacharpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import com.yourteam.cardgacharpg.core.ui.theme.CardGachaRPGTheme
import com.yourteam.cardgacharpg.navigation.NavGraph

// Single-activity host.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardGachaRPGTheme {
                NavGraph()
            }
        }
    }
}