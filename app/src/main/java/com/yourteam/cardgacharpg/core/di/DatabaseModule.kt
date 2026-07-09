package com.yourteam.cardgacharpg.core.di

import android.content.Context
import androidx.room.Room
import com.yourteam.cardgacharpg.core.database.AppDatabase
import com.yourteam.cardgacharpg.feature.collection.data.CardDao
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyDao
import com.yourteam.cardgacharpg.feature.gacha.data.GachaPityDao
import com.yourteam.cardgacharpg.feature.arena.data.ArenaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// ⚠ GETEILTE DATEI — bitte im Team abstimmen.
// Beim Merge war diese Datei leer geworden; ohne sie kann Hilt weder die DB noch irgendein
// DAO bereitstellen (die ganze App startet dann nicht). Hier wieder vollständig hergestellt.
// Jede weitere Person hängt ihre DAO-Provider unten an (P3 Formation, P4 Campaign, P5 Arena).
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "claw_of_fate.db")
            .fallbackToDestructiveMigration() // MVP: bei Schema-Änderung DB neu aufbauen
            .build()

    // --- Person 1 (Karten) ---
    @Provides fun provideCardDao(db: AppDatabase): CardDao = db.cardDao()
    @Provides fun provideInventoryDao(db: AppDatabase): InventoryDao = db.inventoryDao()

    // --- Person 2 (Gacha/Währung) ---
    @Provides fun provideGachaPityDao(db: AppDatabase): GachaPityDao = db.gachaPityDao()
    @Provides fun provideCurrencyDao(db: AppDatabase): CurrencyDao = db.currencyDao()

    // --- Person 5 (Arena/Dashboard) ---
    @Provides fun provideArenaDao(db: AppDatabase): ArenaDao = db.arenaDao()
}