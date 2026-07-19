package com.yourteam.cardgacharpg.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Shared Compose theme wrapper — von MainActivity/allen Screens genutzt.
//
// UI-Polish (Abgabe-Sprint): Die App erzwingt jetzt IMMER das dunkle Game-Theme.
// Vorher: DayNight + Dynamic Color -> im Hellmodus weiße Hintergründe, weiße
// System-/AppBar-Flächen ("White Bar oben") und helle Dropdowns, außerdem
// gerätespezifische Zufallsfarben durch Material You. Ein Fantasy-Game hat eine
// feste Art-Direction — deshalb: ein festes, dunkles Schema für alle Geräte.

private val GameDarkColors = darkColorScheme(
    primary = Color(0xFFFFC107),          // Gold — Hauptaktionen (Pull, Antreten, ...)
    onPrimary = Color(0xFF3A2A00),
    secondary = Color(0xFF7B2CBF),        // Gacha-Violett
    onSecondary = Color.White,
    tertiary = Color(0xFF3E7C4A),         // Waldgrün (Kampagne)
    onTertiary = Color.White,
    background = Color(0xFF0C1018),       // fast schwarz, leicht blaustichig
    onBackground = Color(0xFFE8EAF0),
    surface = Color(0xFF131926),          // Karten, AppBars
    onSurface = Color(0xFFE8EAF0),
    surfaceVariant = Color(0xFF1E2636),   // Slots, Chips, Balken-Tracks
    onSurfaceVariant = Color(0xFFB9C0CF),
    outline = Color(0xFF3A4356),
    outlineVariant = Color(0xFF2A3244),
    error = Color(0xFFFF6E6E),
    onError = Color(0xFF3A0000)
)

@Composable
fun CardGachaRPGTheme(
    // Parameter bleiben aus API-Kompatibilität erhalten (Previews rufen sie teils auf),
    // werden aber bewusst ignoriert: das Game-Theme ist immer dunkel, nie dynamisch.
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GameDarkColors,
        typography = AppTypography,
        content = content
    )
}
