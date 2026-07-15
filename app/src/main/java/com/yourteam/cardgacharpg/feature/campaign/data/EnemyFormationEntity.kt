package com.yourteam.cardgacharpg.feature.campaign.data

// Owner: Person 4 (Yassin) — own struct, not Card entity
data class EnemyFormationEntity(
    val levelId: Int, // Welches Level ist das?

    // Die VORDERE Reihe (nimmt Nahkampfschaden zuerst auf)
    val frontLeftCardId: Int?,
    val frontCenterCardId: Int?,
    val frontRightCardId: Int?,

    // Die HINTERE Reihe (geschützt, solange vorne jemand steht)
    val backLeftCardId: Int?,
    val backCenterCardId: Int?,
    val backRightCardId: Int?
)
