package com.yourteam.cardgacharpg.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yourteam.cardgacharpg.feature.collection.data.CardDao
import com.yourteam.cardgacharpg.feature.collection.data.CardEntity
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao
import com.yourteam.cardgacharpg.feature.collection.data.InventoryEntity

// Room DB — referenziert ALLE Entities/DAOs aus jedem Feature
// ⚠ GETEILTE DATEI: jede Person fügt hier ihre eigenen Entities/DAOs (P2/P3/P4/P5) hinzu.
// Vor dem Mergen kurz im Team abstimmen (Merge-Konflikte + version bump koordinieren).
//
// Aktuell enthalten: Person 1 (Card, Inventory)

// TODO (Person 2): GachaPityEntity, CurrencyDao/-Entity o.ä.
// TODO (Person 3): FormationEntity, FormationDao
// TODO (Person 4): LevelProgressEntity, EnemyFormationEntity, LevelProgressDao
// TODO (Person 5): ArenaProfileEntity, ArenaDao
@Database(
    entities = [
        CardEntity::class,
        InventoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun inventoryDao(): InventoryDao
}