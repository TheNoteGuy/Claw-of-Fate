package com.yourteam.cardgacharpg.feature.gacha.ui

import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.feature.gacha.domain.GachaEngine

// Owner: Person 2 (Nico)
// Ein einziger, unveränderlicher State-Container fürs GachaScreen (MVVM: ViewModel hält State,
// Composable ist stateless und rendert nur diesen State).
data class GachaUiState(
    val gems: Int = 0,
    val pityCount: Int = 0,
    val isPulling: Boolean = false,
    // Ergebnis des letzten Pulls -> triggert die PullResult-Anzeige. null = kein offenes Ergebnis.
    val lastPull: List<Card>? = null,
    // Einmalige Meldung (z.B. "Nicht genug Gems") für eine Snackbar. null = nichts anzuzeigen.
    val message: String? = null
) {
    val canSinglePull: Boolean get() = !isPulling && gems >= GachaEngine.SINGLE_PULL_COST
    val canTenPull: Boolean get() = !isPulling && gems >= GachaEngine.TEN_PULL_COST

    // Soft-Pity-Warnung ab Pull 70 (der nächste Pull ist pityCount + 1).
    val isSoftPity: Boolean get() = (pityCount + 1) >= GachaEngine.SOFT_PITY_START
}