package com.yourteam.cardgacharpg.feature.campaign.domain

import javax.inject.Inject

// Owner: Person 4 (Yassin) — star award logic
class StarRatingUseCase @Inject constructor() {

    fun calculateStars(isVictory: Boolean, survivingUnits: Int, totalUnits: Int): Int {
        // Wenn das Level nicht geschafft wurde, gibt es strikt 0 Sterne
        if (!isVictory || totalUnits == 0) return 0

        // Berechne die Verluste
        val lostUnits = totalUnits - survivingUnits

        // Werte die Verluste aus und gib die Sterne zurück
        return when {
            lostUnits == 0 -> 3
            lostUnits <= 2 -> 2
            else -> 1
        }
    }
}
