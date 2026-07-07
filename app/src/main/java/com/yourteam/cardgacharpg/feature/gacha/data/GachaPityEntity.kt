package com.yourteam.cardgacharpg.feature.gacha.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Owner: Person 2 (Nico)
// Singleton-Muster: es gibt immer genau EINE Zeile (id = 0). Der Pity-Zähler MUSS
// persistent sein — ein App-Neustart darf ihn nicht auf 0 setzen (GDD-Pflicht).
@Entity(tableName = "gacha_pity")
data class GachaPityEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val pityCount: Int = 0,
    val lastPullTimestamp: Long = 0L
) {
    companion object {
        const val SINGLETON_ID = 0
    }
}