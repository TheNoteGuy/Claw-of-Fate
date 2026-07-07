package com.yourteam.cardgacharpg.feature.gacha.ui

import androidx.compose.ui.graphics.Color
import com.yourteam.cardgacharpg.core.model.Rarity

// Owner: Person 2 (Nico)
// Farbcodierung der Seltenheiten laut GDD: Grau / Blau / Lila / Gold.
// (Accessibility: im finalen Polish zusätzlich Form/Icon je Seltenheit, nicht nur Farbe.)
fun Rarity.color(): Color = when (this) {
    Rarity.COMMON -> Color(0xFF9E9E9E)     // Grau
    Rarity.RARE -> Color(0xFF2196F3)       // Blau
    Rarity.EPIC -> Color(0xFF9C27B0)       // Lila
    Rarity.LEGENDARY -> Color(0xFFFFC107)  // Gold
}

fun Rarity.label(): String = when (this) {
    Rarity.COMMON -> "Common"
    Rarity.RARE -> "Rare"
    Rarity.EPIC -> "Epic"
    Rarity.LEGENDARY -> "Legendary"
}