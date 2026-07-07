package com.yourteam.cardgacharpg.core.di

import android.content.Context
import androidx.room.Room
import com.yourteam.cardgacharpg.core.database.AppDatabase
import com.yourteam.cardgacharpg.feature.collection.data.CardDao
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Hilt module — provides Room DB & DAOs.
// ⚠ GETEILTE DATEI: Falls P2/P3/P4/P5 eigene DAOs zu AppDatabase hinzufügen,
// hier jeweils eine passende provide-Funktion ergänzen (siehe Kommentare unten).
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cardgacharpg.db"
        )
            // .fallbackToDestructiveMigration() // nur für Entwicklung, vor Release entfernen
            .build()

    @Provides
    fun provideCardDao(db: AppDatabase): CardDao = db.cardDao()

    @Provides
    fun provideInventoryDao(db: AppDatabase): InventoryDao = db.inventoryDao()

    // TODO (Person 2): fun provideGachaPityDao(db: AppDatabase): GachaPityDao = db.gachaPityDao()
    // TODO (Person 2): fun provideCurrencyDao(db: AppDatabase): CurrencyDao = db.currencyDao()
    // TODO (Person 3): fun provideFormationDao(db: AppDatabase): FormationDao = db.formationDao()
    // TODO (Person 4): fun provideLevelProgressDao(db: AppDatabase): LevelProgressDao = db.levelProgressDao()
    // TODO (Person 5): fun provideArenaDao(db: AppDatabase): ArenaDao = db.arenaDao()
}
