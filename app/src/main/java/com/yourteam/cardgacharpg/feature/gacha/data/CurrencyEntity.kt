package com.yourteam.cardgacharpg.feature.gacha.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Owner: Person 2 (Nico)
// Eigene Currency-Tabelle (NICHT die Inventory-Tabelle von Person 1). Grund laut Plan:
// Gems/Gold werden hier atomar verwaltet, um Race Conditions zu vermeiden. Singleton-Zeile.
@Entity(tableName = "currency")
data class CurrencyEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val gems: Int = 0,
    val gold: Int = 0
) {
    companion object {
        const val SINGLETON_ID = 0
        // Start-Guthaben fürs MVP, damit man sofort pullen kann (~11 Pulls wert).
        const val STARTING_GEMS = 1200
        const val STARTING_GOLD = 0
    }
}