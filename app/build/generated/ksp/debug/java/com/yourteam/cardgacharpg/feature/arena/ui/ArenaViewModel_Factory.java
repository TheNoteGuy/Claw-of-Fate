package com.yourteam.cardgacharpg.feature.arena.ui;

import com.yourteam.cardgacharpg.feature.arena.domain.TrophyManager;
import com.yourteam.cardgacharpg.feature.battle.data.FormationRepository;
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class ArenaViewModel_Factory implements Factory<ArenaViewModel> {
  private final Provider<FormationRepository> formationRepositoryProvider;

  private final Provider<TrophyManager> trophyManagerProvider;

  private final Provider<CurrencyManager> currencyManagerProvider;

  public ArenaViewModel_Factory(Provider<FormationRepository> formationRepositoryProvider,
      Provider<TrophyManager> trophyManagerProvider,
      Provider<CurrencyManager> currencyManagerProvider) {
    this.formationRepositoryProvider = formationRepositoryProvider;
    this.trophyManagerProvider = trophyManagerProvider;
    this.currencyManagerProvider = currencyManagerProvider;
  }

  @Override
  public ArenaViewModel get() {
    return newInstance(formationRepositoryProvider.get(), trophyManagerProvider.get(), currencyManagerProvider.get());
  }

  public static ArenaViewModel_Factory create(
      Provider<FormationRepository> formationRepositoryProvider,
      Provider<TrophyManager> trophyManagerProvider,
      Provider<CurrencyManager> currencyManagerProvider) {
    return new ArenaViewModel_Factory(formationRepositoryProvider, trophyManagerProvider, currencyManagerProvider);
  }

  public static ArenaViewModel newInstance(FormationRepository formationRepository,
      TrophyManager trophyManager, CurrencyManager currencyManager) {
    return new ArenaViewModel(formationRepository, trophyManager, currencyManager);
  }
}
