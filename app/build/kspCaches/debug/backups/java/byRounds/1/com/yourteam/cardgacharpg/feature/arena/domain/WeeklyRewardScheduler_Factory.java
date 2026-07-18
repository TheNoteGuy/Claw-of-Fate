package com.yourteam.cardgacharpg.feature.arena.domain;

import com.yourteam.cardgacharpg.feature.arena.data.ArenaDao;
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
public final class WeeklyRewardScheduler_Factory implements Factory<WeeklyRewardScheduler> {
  private final Provider<ArenaDao> arenaDaoProvider;

  private final Provider<CurrencyManager> currencyManagerProvider;

  public WeeklyRewardScheduler_Factory(Provider<ArenaDao> arenaDaoProvider,
      Provider<CurrencyManager> currencyManagerProvider) {
    this.arenaDaoProvider = arenaDaoProvider;
    this.currencyManagerProvider = currencyManagerProvider;
  }

  @Override
  public WeeklyRewardScheduler get() {
    return newInstance(arenaDaoProvider.get(), currencyManagerProvider.get());
  }

  public static WeeklyRewardScheduler_Factory create(Provider<ArenaDao> arenaDaoProvider,
      Provider<CurrencyManager> currencyManagerProvider) {
    return new WeeklyRewardScheduler_Factory(arenaDaoProvider, currencyManagerProvider);
  }

  public static WeeklyRewardScheduler newInstance(ArenaDao arenaDao,
      CurrencyManager currencyManager) {
    return new WeeklyRewardScheduler(arenaDao, currencyManager);
  }
}
