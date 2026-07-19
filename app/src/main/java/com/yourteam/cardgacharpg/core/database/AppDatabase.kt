package com.yourteam.cardgacharpg.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yourteam.cardgacharpg.feature.collection.data.CardDao
import com.yourteam.cardgacharpg.feature.collection.data.CardEntity
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao
import com.yourteam.cardgacharpg.feature.collection.data.InventoryEntity
import com.yourteam.cardgacharpg.feature.gacha.data.GachaPityEntity
import com.yourteam.cardgacharpg.feature.gacha.data.GachaPityDao
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyEntity
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyDao
import com.yourteam.cardgacharpg.feature.arena.data.ArenaProfileEntity
import com.yourteam.cardgacharpg.feature.arena.data.ArenaDao
import com.yourteam.cardgacharpg.feature.campaign.data.LevelProgressDao
import com.yourteam.cardgacharpg.feature.campaign.data.LevelProgressEntity
import com.yourteam.cardgacharpg.feature.battle.data.FormationEntity
import com.yourteam.cardgacharpg.feature.battle.data.FormationDao

// Room DB — referenziert ALLE Entities/DAOs aus jedem Feature
// ⚠ GETEILTE DATEI: jede Person fügt hier ihre eigenen Entities/DAOs (P2/P3/P4/P5) hinzu.
// Vor dem Mergen kurz im Team abstimmen (Merge-Konflikte + version bump koordinieren).
//
// Aktuell enthalten: Person 1 (Card, Inventory)
//                    Person 2 (GachaPity, Currency)
//                    Person 3 (Formation) — NEU
//                    Person 4 (LevelProgress)
//                    Person 5 (ArenaProfileEntity, ArenaDao)
@Database(
    entities = [
        CardEntity::class,
        InventoryEntity::class,
        GachaPityEntity::class,
        CurrencyEntity::class,
        ArenaProfileEntity::class,
        LevelProgressEntity::class,
        FormationEntity::class
    ],
    version = 5, // v5 (Person 1/2, Karten-Stacking): cards.count-Spalte, siehe MIGRATION_4_5
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun inventoryDao(): InventoryDao

    abstract fun gachaPityDao(): GachaPityDao
    abstract fun currencyDao(): CurrencyDao

    abstract fun arenaDao(): ArenaDao

    abstract fun levelProgressDao(): LevelProgressDao

    abstract fun formationDao(): FormationDao

    companion object {
        // v4 -> v5: Stack-Zaehler fuer Duplikate (Punkt "Doppelte Karten stapeln").
        // DEFAULT 1 muss zu @ColumnInfo(defaultValue = "1") in CardEntity passen, sonst
        // schlaegt Rooms Schema-Validierung nach der Migration fehl.
        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE cards ADD COLUMN count INTEGER NOT NULL DEFAULT 1")
            }
        }
    }
}
