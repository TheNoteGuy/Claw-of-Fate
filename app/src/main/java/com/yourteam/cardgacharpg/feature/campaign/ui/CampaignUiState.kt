package com.yourteam.cardgacharpg.feature.campaign.ui

import com.yourteam.cardgacharpg.feature.campaign.data.LevelProgressEntity

// Owner: Yassin
// Ein einziger, unveränderlicher State-Container für dein Kampagnen-Feature (MVVM-Standard).
// Die UI (CampaignMapScreen) beobachtet diesen Zustand und zeichnet sich bei Änderungen neu.
data class CampaignUiState(
    val levels: List<LevelProgressEntity> = emptyList(),

    // 2. Flüchtiger Zustand: Zeigt an, ob die Karte gerade lädt (z. B. beim ersten DB-Setup)
    val isLoading: Boolean = false,

    // 3. Flüchtiger Zustand: Welches Level hat der Spieler angeklickt?
    // null bedeutet: Es ist kein Level ausgewählt (das Detail-Popup/Sheet ist geschlossen).
    val selectedLevelId: Int? = null
) {
    // Hilfsfunktionen für deine UI (CampaignMapScreen) ---

    /**
     * Sucht automatisch das komplette Level-Objekt heraus, das gerade ausgewählt ist.
     * Wenn kein Level ausgewählt ist, gibt es null zurück.
     * Extrem praktisch, um es direkt an dein "LevelDetailSheet" zu übergeben!
     */
    val selectedLevel: LevelProgressEntity?
        get() = levels.find { it.levelId == selectedLevelId }

    /**
     * Berechnet live die Gesamtzahl aller Sterne, die der Spieler bisher gesammelt hat.
     * Perfekt, um es oben auf der Kampagnenkarte als Fortschritt anzuzeigen (z. B. "⭐ 12 / 30").
     */
    val totalStarsCollected: Int
        get() = levels.sumOf { it.stars }
}