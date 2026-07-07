package com.yourteam.cardgacharpg.core.di

import com.yourteam.cardgacharpg.feature.gacha.domain.CatHeroPool
import com.yourteam.cardgacharpg.feature.gacha.domain.HeroPool
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// ⚠ GETEILTE DATEI — bitte im Team abstimmen.
// GachaViewModel braucht ein HeroPool; ohne Binding kann Hilt es nicht bauen (Compile-/Startfehler).
// CardRepository & CurrencyManager haben @Inject constructor -> die findet Hilt automatisch,
// hier ist nur das Interface->Impl-Binding für HeroPool nötig.
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideHeroPool(): HeroPool = CatHeroPool()
}