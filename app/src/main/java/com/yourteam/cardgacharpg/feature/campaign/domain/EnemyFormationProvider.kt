package com.yourteam.cardgacharpg.feature.campaign.domain

import com.yourteam.cardgacharpg.feature.campaign.data.EnemyFormationEntity
import javax.inject.Inject

// Owner: Person 4 (Yassin) — 10 handdesigned formations
class EnemyFormationProvider @Inject constructor() {
    // WICHTIG: Nur über das CampaignRepository aufrufen! Nicht direkt aus dem UI oder anderen Features konsumieren
    fun getFormationForLevel(levelId: Int): EnemyFormationEntity {
        return when (levelId) { // im Grunde Switch Case

            // Akt 1 "Der Dunkle Wald" (Level 1 - 10)
            1 -> EnemyFormationEntity(
                levelId = 1,
                // z.B. Level 1: 3 simple Common-Gegner in der vorderen Reihe
                frontLeftCardId = 1,   // z.B. Feuer-Krieger
                frontCenterCardId = 2, // z.B. Wasser-Tank
                frontRightCardId = 1,  // z.B. Feuer-Krieger
                // Hintere Reihe ist leer
                backLeftCardId = null,
                backCenterCardId = null,
                backRightCardId = null
            )

            // ... hier würden dann Level 2 bis 10 stehen ...

            10 -> EnemyFormationEntity(
                levelId = 10,
                // Level 10 (Boss von Akt 1): Volles Grid
                frontLeftCardId = 1,
                frontCenterCardId = 5, // z.B. Rare-Tank
                frontRightCardId = 1,
                backLeftCardId = 4,
                backCenterCardId = 6,  // z.B. Rare-Support
                backRightCardId = 4
            )

            else -> throw IllegalArgumentException("Fehler: Level $levelId existiert nicht")
        }
    }
}
