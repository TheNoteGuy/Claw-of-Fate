package com.yourteam.cardgacharpg.feature.gacha.ui;

import com.yourteam.cardgacharpg.feature.collection.data.CardRepository;
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyManager;
import com.yourteam.cardgacharpg.feature.gacha.data.GachaPityDao;
import com.yourteam.cardgacharpg.feature.gacha.domain.HeroPool;
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
public final class GachaViewModel_Factory implements Factory<GachaViewModel> {
  private final Provider<CurrencyManager> currencyManagerProvider;

  private final Provider<GachaPityDao> pityDaoProvider;

  private final Provider<CardRepository> cardRepositoryProvider;

  private final Provider<HeroPool> heroPoolProvider;

  public GachaViewModel_Factory(Provider<CurrencyManager> currencyManagerProvider,
      Provider<GachaPityDao> pityDaoProvider, Provider<CardRepository> cardRepositoryProvider,
      Provider<HeroPool> heroPoolProvider) {
    this.currencyManagerProvider = currencyManagerProvider;
    this.pityDaoProvider = pityDaoProvider;
    this.cardRepositoryProvider = cardRepositoryProvider;
    this.heroPoolProvider = heroPoolProvider;
  }

  @Override
  public GachaViewModel get() {
    return newInstance(currencyManagerProvider.get(), pityDaoProvider.get(), cardRepositoryProvider.get(), heroPoolProvider.get());
  }

  public static GachaViewModel_Factory create(Provider<CurrencyManager> currencyManagerProvider,
      Provider<GachaPityDao> pityDaoProvider, Provider<CardRepository> cardRepositoryProvider,
      Provider<HeroPool> heroPoolProvider) {
    return new GachaViewModel_Factory(currencyManagerProvider, pityDaoProvider, cardRepositoryProvider, heroPoolProvider);
  }

  public static GachaViewModel newInstance(CurrencyManager currencyManager, GachaPityDao pityDao,
      CardRepository cardRepository, HeroPool heroPool) {
    return new GachaViewModel(currencyManager, pityDao, cardRepository, heroPool);
  }
}
