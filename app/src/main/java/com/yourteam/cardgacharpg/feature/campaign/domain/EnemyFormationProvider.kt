package com.yourteam.cardgacharpg.feature.campaign.domain

import com.yourteam.cardgacharpg.feature.campaign.data.EnemyFormationEntity
import javax.inject.Inject

// Owner: Person 4 (Yassin) — 10 handdesignte Gegnerformationen fuer Akt 1 "Der Dunkle Wald".
// Die IDs referenzieren den EnemyCatalog (1-4 Common, 5-8 Rare); Aufloesung in
// BattleParticipants passiert dort (EnemyCatalog.toBattleFormation).
//
// Kurve: erst wenige Common-Gegner nur vorne, dann kommt die hintere Reihe dazu,
// ab Level 5 tauchen Rare-Gegner auf, Level 10 ist das volle Boss-Grid.
class EnemyFormationProvider @Inject constructor() {
    // WICHTIG: Nur über das CampaignRepository aufrufen! Nicht direkt aus dem UI oder anderen Features konsumieren
    fun getFormationForLevel(levelId: Int): EnemyFormationEntity {
        return when (levelId) {

            // Akt 1 "Der Dunkle Wald" (Level 1 - 10)
            1 -> formation(levelId, front = Triple(1, 2, null), back = Triple(null, null, null))
            2 -> formation(levelId, front = Triple(1, 2, 1), back = Triple(null, null, null))
            3 -> formation(levelId, front = Triple(2, 1, null), back = Triple(null, 3, null))
            4 -> formation(levelId, front = Triple(1, 2, 1), back = Triple(3, null, 4))
            5 -> formation(levelId, front = Triple(5, 1, null), back = Triple(3, 4, null))
            6 -> formation(levelId, front = Triple(2, 5, 1), back = Triple(4, null, 3))
            7 -> formation(levelId, front = Triple(5, 8, null), back = Triple(3, 6, 4))
            8 -> formation(levelId, front = Triple(5, 2, 8), back = Triple(7, 6, null))
            9 -> formation(levelId, front = Triple(8, 5, 8), back = Triple(7, 4, 6))

            // BOSS "Der Waldwächter": volles Grid, Rare-lastig
            10 -> formation(levelId, front = Triple(5, 8, 5), back = Triple(7, 6, 7))

            else -> throw IllegalArgumentException("Fehler: Level $levelId existiert nicht")
        }
    }

    private fun formation(
        levelId: Int,
        front: Triple<Int?, Int?, Int?>,
        back: Triple<Int?, Int?, Int?>
    ) = EnemyFormationEntity(
        levelId = levelId,
        frontLeftCardId = front.first,
        frontCenterCardId = front.second,
        frontRightCardId = front.third,
        backLeftCardId = back.first,
        backCenterCardId = back.second,
        backRightCardId = back.third
    )
}
