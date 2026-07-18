package com.yourteam.cardgacharpg.feature.battle.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Owner: Person 3 (Marc) — slot0..slot5 as Card IDs
// Singleton-Muster (eine aktive Formation), analog zu CurrencyEntity/ArenaProfileEntity.
// 3x2-Grid laut FA03: slot0-slot2 = vordere Reihe, slot3-slot5 = hintere Reihe
// (die Reihen-Zuordnung wird für die Rollen-Zielwahl im Kampf gebraucht, siehe BattleSimulator).
@Entity(tableName = "formation")
data class FormationEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val slot0: Int? = null,
    val slot1: Int? = null,
    val slot2: Int? = null,
    val slot3: Int? = null,
    val slot4: Int? = null,
    val slot5: Int? = null
) {
    companion object {
        const val SINGLETON_ID = 0
        const val FRONT_ROW_SLOTS = 3
        const val TOTAL_SLOTS = 6
    }
}
