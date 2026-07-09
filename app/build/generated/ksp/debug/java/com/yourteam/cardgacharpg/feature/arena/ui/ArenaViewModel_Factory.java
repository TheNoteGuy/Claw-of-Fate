package com.yourteam.cardgacharpg.feature.arena.ui;

import com.yourteam.cardgacharpg.feature.arena.domain.TrophyManager;
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository;
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
  private final Provider<CardRepository> cardRepositoryProvider;

  private final Provider<TrophyManager> trophyManagerProvider;

  private final Provider<CurrencyManager> currencyManagerProvider;

  public ArenaViewModel_Factory(Provider<CardRepository> cardRepositoryProvider,
      Provider<TrophyManager> trophyManagerProvider,
      Provider<CurrencyManager> currencyManagerProvider) {
    this.cardRepositoryProvider = cardRepositoryProvider;
    this.trophyManagerProvider = trophyManagerProvider;
    this.currencyManagerProvider = currencyManagerProvider;
  }

  @Override
  public ArenaViewModel get() {
    return newInstance(cardRepositoryProvider.get(), trophyManagerProvider.get(), currencyManagerProvider.get());
  }

  public static ArenaViewModel_Factory create(Provider<CardRepository> cardRepositoryProvider,
      Provider<TrophyManager> trophyManagerProvider,
      Provider<CurrencyManager> currencyManagerProvider) {
    return new ArenaViewModel_Factory(cardRepositoryProvider, trophyManagerProvider, currencyManagerProvider);
  }

  public static ArenaViewModel newInstance(CardRepository cardRepository,
      TrophyManager trophyManager, CurrencyManager currencyManager) {
    return new ArenaViewModel(cardRepository, trophyManager, currencyManager);
  }
}
