package com.yourteam.cardgacharpg.feature.collection.data

import androidx.room.Entity

// Owner: Person 1 (Leila) — shared w/ Person 2, coordinate schema
// Eine Zeile pro itemType (Singleton-Muster je Typ) -> itemType ist Primary Key.
// Person 2 nutzt diese Tabelle NICHT für Gems/Gold (das läuft über eigenes CurrencyDao),
// sondern nur für XP_POTION-Bestände. Gems/Gold-Werte hier bewusst nicht dupliziert,
// um Race Conditions mit CurrencyManager (Person 2) zu vermeiden.
@Entity(tableName = "inventory", primaryKeys = ["itemType"])
data class InventoryEntity(
    val itemType: ItemType,
    val amount: Int
)

enum class ItemType {
    XP_POTION,
    GEM,
    GOLD
}
