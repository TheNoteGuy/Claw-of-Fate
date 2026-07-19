package com.yourteam.cardgacharpg.feature.arena.ui;

import com.yourteam.cardgacharpg.feature.arena.data.ArenaDao;
import com.yourteam.cardgacharpg.feature.arena.domain.WeeklyRewardScheduler;
import com.yourteam.cardgacharpg.feature.battle.data.FormationDao;
import com.yourteam.cardgacharpg.feature.campaign.data.CampaignRepository;
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository;
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyDao;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<CardRepository> cardRepositoryProvider;

  private final Provider<CurrencyDao> currencyDaoProvider;

  private final Provider<ArenaDao> arenaDaoProvider;

  private final Provider<FormationDao> formationDaoProvider;

  private final Provider<CampaignRepository> campaignRepositoryProvider;

  private final Provider<WeeklyRewardScheduler> weeklyRewardSchedulerProvider;

  public HomeViewModel_Factory(Provider<CardRepository> cardRepositoryProvider,
      Provider<CurrencyDao> currencyDaoProvider, Provider<ArenaDao> arenaDaoProvider,
      Provider<FormationDao> formationDaoProvider,
      Provider<CampaignRepository> campaignRepositoryProvider,
      Provider<WeeklyRewardScheduler> weeklyRewardSchedulerProvider) {
    this.cardRepositoryProvider = cardRepositoryProvider;
    this.currencyDaoProvider = currencyDaoProvider;
    this.arenaDaoProvider = arenaDaoProvider;
    this.formationDaoProvider = formationDaoProvider;
    this.campaignRepositoryProvider = campaignRepositoryProvider;
    this.weeklyRewardSchedulerProvider = weeklyRewardSchedulerProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(cardRepositoryProvider.get(), currencyDaoProvider.get(), arenaDaoProvider.get(), formationDaoProvider.get(), campaignRepositoryProvider.get(), weeklyRewardSchedulerProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<CardRepository> cardRepositoryProvider,
      Provider<CurrencyDao> currencyDaoProvider, Provider<ArenaDao> arenaDaoProvider,
      Provider<FormationDao> formationDaoProvider,
      Provider<CampaignRepository> campaignRepositoryProvider,
      Provider<WeeklyRewardScheduler> weeklyRewardSchedulerProvider) {
    return new HomeViewModel_Factory(cardRepositoryProvider, currencyDaoProvider, arenaDaoProvider, formationDaoProvider, campaignRepositoryProvider, weeklyRewardSchedulerProvider);
  }

  public static HomeViewModel newInstance(CardRepository cardRepository, CurrencyDao currencyDao,
      ArenaDao arenaDao, FormationDao formationDao, CampaignRepository campaignRepository,
      WeeklyRewardScheduler weeklyRewardScheduler) {
    return new HomeViewModel(cardRepository, currencyDao, arenaDao, formationDao, campaignRepository, weeklyRewardScheduler);
  }
}
