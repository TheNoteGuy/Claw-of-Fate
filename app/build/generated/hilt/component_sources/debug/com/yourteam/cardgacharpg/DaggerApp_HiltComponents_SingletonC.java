package com.yourteam.cardgacharpg;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.yourteam.cardgacharpg.core.database.AppDatabase;
import com.yourteam.cardgacharpg.core.di.DatabaseModule_ProvideArenaDaoFactory;
import com.yourteam.cardgacharpg.core.di.DatabaseModule_ProvideCardDaoFactory;
import com.yourteam.cardgacharpg.core.di.DatabaseModule_ProvideCurrencyDaoFactory;
import com.yourteam.cardgacharpg.core.di.DatabaseModule_ProvideDatabaseFactory;
import com.yourteam.cardgacharpg.core.di.DatabaseModule_ProvideGachaPityDaoFactory;
import com.yourteam.cardgacharpg.core.di.DatabaseModule_ProvideInventoryDaoFactory;
import com.yourteam.cardgacharpg.core.di.DatabaseModule_ProvideLevelProgressDaoFactory;
import com.yourteam.cardgacharpg.core.di.RepositoryModule_ProvideHeroPoolFactory;
import com.yourteam.cardgacharpg.feature.arena.data.ArenaDao;
import com.yourteam.cardgacharpg.feature.arena.domain.TrophyManager;
import com.yourteam.cardgacharpg.feature.arena.domain.WeeklyRewardScheduler;
import com.yourteam.cardgacharpg.feature.arena.ui.ArenaViewModel;
import com.yourteam.cardgacharpg.feature.arena.ui.ArenaViewModel_HiltModules;
import com.yourteam.cardgacharpg.feature.arena.ui.HomeViewModel;
import com.yourteam.cardgacharpg.feature.arena.ui.HomeViewModel_HiltModules;
import com.yourteam.cardgacharpg.feature.battle.ui.BattleViewModel;
import com.yourteam.cardgacharpg.feature.battle.ui.BattleViewModel_HiltModules;
import com.yourteam.cardgacharpg.feature.battle.ui.FormationViewModel;
import com.yourteam.cardgacharpg.feature.battle.ui.FormationViewModel_HiltModules;
import com.yourteam.cardgacharpg.feature.campaign.data.CampaignRepository;
import com.yourteam.cardgacharpg.feature.campaign.data.LevelProgressDao;
import com.yourteam.cardgacharpg.feature.campaign.domain.EnemyFormationProvider;
import com.yourteam.cardgacharpg.feature.campaign.domain.StarRatingUseCase;
import com.yourteam.cardgacharpg.feature.campaign.ui.CampaignViewModel;
import com.yourteam.cardgacharpg.feature.campaign.ui.CampaignViewModel_HiltModules;
import com.yourteam.cardgacharpg.feature.collection.data.CardDao;
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository;
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao;
import com.yourteam.cardgacharpg.feature.collection.domain.LevelUpUseCase;
import com.yourteam.cardgacharpg.feature.collection.ui.CardDetailViewModel;
import com.yourteam.cardgacharpg.feature.collection.ui.CardDetailViewModel_HiltModules;
import com.yourteam.cardgacharpg.feature.collection.ui.CollectionViewModel;
import com.yourteam.cardgacharpg.feature.collection.ui.CollectionViewModel_HiltModules;
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyDao;
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyManager;
import com.yourteam.cardgacharpg.feature.gacha.data.GachaPityDao;
import com.yourteam.cardgacharpg.feature.gacha.ui.GachaViewModel;
import com.yourteam.cardgacharpg.feature.gacha.ui.GachaViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerApp_HiltComponents_SingletonC {
  private DaggerApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public App_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements App_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public App_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements App_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public App_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements App_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public App_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements App_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public App_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements App_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public App_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements App_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public App_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements App_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public App_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends App_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends App_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends App_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends App_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(8).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_arena_ui_ArenaViewModel, ArenaViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_battle_ui_BattleViewModel, BattleViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_campaign_ui_CampaignViewModel, CampaignViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_collection_ui_CardDetailViewModel, CardDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_collection_ui_CollectionViewModel, CollectionViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_battle_ui_FormationViewModel, FormationViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_gacha_ui_GachaViewModel, GachaViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_arena_ui_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_yourteam_cardgacharpg_feature_battle_ui_BattleViewModel = "com.yourteam.cardgacharpg.feature.battle.ui.BattleViewModel";

      static String com_yourteam_cardgacharpg_feature_arena_ui_ArenaViewModel = "com.yourteam.cardgacharpg.feature.arena.ui.ArenaViewModel";

      static String com_yourteam_cardgacharpg_feature_collection_ui_CardDetailViewModel = "com.yourteam.cardgacharpg.feature.collection.ui.CardDetailViewModel";

      static String com_yourteam_cardgacharpg_feature_battle_ui_FormationViewModel = "com.yourteam.cardgacharpg.feature.battle.ui.FormationViewModel";

      static String com_yourteam_cardgacharpg_feature_gacha_ui_GachaViewModel = "com.yourteam.cardgacharpg.feature.gacha.ui.GachaViewModel";

      static String com_yourteam_cardgacharpg_feature_arena_ui_HomeViewModel = "com.yourteam.cardgacharpg.feature.arena.ui.HomeViewModel";

      static String com_yourteam_cardgacharpg_feature_collection_ui_CollectionViewModel = "com.yourteam.cardgacharpg.feature.collection.ui.CollectionViewModel";

      static String com_yourteam_cardgacharpg_feature_campaign_ui_CampaignViewModel = "com.yourteam.cardgacharpg.feature.campaign.ui.CampaignViewModel";

      @KeepFieldType
      BattleViewModel com_yourteam_cardgacharpg_feature_battle_ui_BattleViewModel2;

      @KeepFieldType
      ArenaViewModel com_yourteam_cardgacharpg_feature_arena_ui_ArenaViewModel2;

      @KeepFieldType
      CardDetailViewModel com_yourteam_cardgacharpg_feature_collection_ui_CardDetailViewModel2;

      @KeepFieldType
      FormationViewModel com_yourteam_cardgacharpg_feature_battle_ui_FormationViewModel2;

      @KeepFieldType
      GachaViewModel com_yourteam_cardgacharpg_feature_gacha_ui_GachaViewModel2;

      @KeepFieldType
      HomeViewModel com_yourteam_cardgacharpg_feature_arena_ui_HomeViewModel2;

      @KeepFieldType
      CollectionViewModel com_yourteam_cardgacharpg_feature_collection_ui_CollectionViewModel2;

      @KeepFieldType
      CampaignViewModel com_yourteam_cardgacharpg_feature_campaign_ui_CampaignViewModel2;
    }
  }

  private static final class ViewModelCImpl extends App_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<ArenaViewModel> arenaViewModelProvider;

    private Provider<BattleViewModel> battleViewModelProvider;

    private Provider<CampaignViewModel> campaignViewModelProvider;

    private Provider<CardDetailViewModel> cardDetailViewModelProvider;

    private Provider<CollectionViewModel> collectionViewModelProvider;

    private Provider<FormationViewModel> formationViewModelProvider;

    private Provider<GachaViewModel> gachaViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private CardRepository cardRepository() {
      return new CardRepository(singletonCImpl.cardDao());
    }

    private TrophyManager trophyManager() {
      return new TrophyManager(singletonCImpl.arenaDao());
    }

    private CurrencyManager currencyManager() {
      return new CurrencyManager(singletonCImpl.provideDatabaseProvider.get(), singletonCImpl.currencyDao());
    }

    private CampaignRepository campaignRepository() {
      return new CampaignRepository(singletonCImpl.levelProgressDao(), new EnemyFormationProvider());
    }

    private LevelUpUseCase levelUpUseCase() {
      return new LevelUpUseCase(cardRepository(), singletonCImpl.inventoryDao());
    }

    private WeeklyRewardScheduler weeklyRewardScheduler() {
      return new WeeklyRewardScheduler(singletonCImpl.arenaDao(), currencyManager());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.arenaViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.battleViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.campaignViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.cardDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.collectionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.formationViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.gachaViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(8).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_arena_ui_ArenaViewModel, ((Provider) arenaViewModelProvider)).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_battle_ui_BattleViewModel, ((Provider) battleViewModelProvider)).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_campaign_ui_CampaignViewModel, ((Provider) campaignViewModelProvider)).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_collection_ui_CardDetailViewModel, ((Provider) cardDetailViewModelProvider)).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_collection_ui_CollectionViewModel, ((Provider) collectionViewModelProvider)).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_battle_ui_FormationViewModel, ((Provider) formationViewModelProvider)).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_gacha_ui_GachaViewModel, ((Provider) gachaViewModelProvider)).put(LazyClassKeyProvider.com_yourteam_cardgacharpg_feature_arena_ui_HomeViewModel, ((Provider) homeViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_yourteam_cardgacharpg_feature_arena_ui_ArenaViewModel = "com.yourteam.cardgacharpg.feature.arena.ui.ArenaViewModel";

      static String com_yourteam_cardgacharpg_feature_collection_ui_CollectionViewModel = "com.yourteam.cardgacharpg.feature.collection.ui.CollectionViewModel";

      static String com_yourteam_cardgacharpg_feature_collection_ui_CardDetailViewModel = "com.yourteam.cardgacharpg.feature.collection.ui.CardDetailViewModel";

      static String com_yourteam_cardgacharpg_feature_battle_ui_BattleViewModel = "com.yourteam.cardgacharpg.feature.battle.ui.BattleViewModel";

      static String com_yourteam_cardgacharpg_feature_battle_ui_FormationViewModel = "com.yourteam.cardgacharpg.feature.battle.ui.FormationViewModel";

      static String com_yourteam_cardgacharpg_feature_gacha_ui_GachaViewModel = "com.yourteam.cardgacharpg.feature.gacha.ui.GachaViewModel";

      static String com_yourteam_cardgacharpg_feature_arena_ui_HomeViewModel = "com.yourteam.cardgacharpg.feature.arena.ui.HomeViewModel";

      static String com_yourteam_cardgacharpg_feature_campaign_ui_CampaignViewModel = "com.yourteam.cardgacharpg.feature.campaign.ui.CampaignViewModel";

      @KeepFieldType
      ArenaViewModel com_yourteam_cardgacharpg_feature_arena_ui_ArenaViewModel2;

      @KeepFieldType
      CollectionViewModel com_yourteam_cardgacharpg_feature_collection_ui_CollectionViewModel2;

      @KeepFieldType
      CardDetailViewModel com_yourteam_cardgacharpg_feature_collection_ui_CardDetailViewModel2;

      @KeepFieldType
      BattleViewModel com_yourteam_cardgacharpg_feature_battle_ui_BattleViewModel2;

      @KeepFieldType
      FormationViewModel com_yourteam_cardgacharpg_feature_battle_ui_FormationViewModel2;

      @KeepFieldType
      GachaViewModel com_yourteam_cardgacharpg_feature_gacha_ui_GachaViewModel2;

      @KeepFieldType
      HomeViewModel com_yourteam_cardgacharpg_feature_arena_ui_HomeViewModel2;

      @KeepFieldType
      CampaignViewModel com_yourteam_cardgacharpg_feature_campaign_ui_CampaignViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.yourteam.cardgacharpg.feature.arena.ui.ArenaViewModel 
          return (T) new ArenaViewModel(viewModelCImpl.cardRepository(), viewModelCImpl.trophyManager(), viewModelCImpl.currencyManager());

          case 1: // com.yourteam.cardgacharpg.feature.battle.ui.BattleViewModel 
          return (T) new BattleViewModel();

          case 2: // com.yourteam.cardgacharpg.feature.campaign.ui.CampaignViewModel 
          return (T) new CampaignViewModel(viewModelCImpl.campaignRepository(), new StarRatingUseCase());

          case 3: // com.yourteam.cardgacharpg.feature.collection.ui.CardDetailViewModel 
          return (T) new CardDetailViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.cardRepository(), singletonCImpl.inventoryDao(), viewModelCImpl.levelUpUseCase());

          case 4: // com.yourteam.cardgacharpg.feature.collection.ui.CollectionViewModel 
          return (T) new CollectionViewModel(viewModelCImpl.cardRepository());

          case 5: // com.yourteam.cardgacharpg.feature.battle.ui.FormationViewModel 
          return (T) new FormationViewModel();

          case 6: // com.yourteam.cardgacharpg.feature.gacha.ui.GachaViewModel 
          return (T) new GachaViewModel(viewModelCImpl.currencyManager(), singletonCImpl.gachaPityDao(), viewModelCImpl.cardRepository(), RepositoryModule_ProvideHeroPoolFactory.provideHeroPool());

          case 7: // com.yourteam.cardgacharpg.feature.arena.ui.HomeViewModel 
          return (T) new HomeViewModel(viewModelCImpl.cardRepository(), singletonCImpl.currencyDao(), singletonCImpl.arenaDao(), viewModelCImpl.weeklyRewardScheduler());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends App_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends App_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends App_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AppDatabase> provideDatabaseProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private CardDao cardDao() {
      return DatabaseModule_ProvideCardDaoFactory.provideCardDao(provideDatabaseProvider.get());
    }

    private ArenaDao arenaDao() {
      return DatabaseModule_ProvideArenaDaoFactory.provideArenaDao(provideDatabaseProvider.get());
    }

    private CurrencyDao currencyDao() {
      return DatabaseModule_ProvideCurrencyDaoFactory.provideCurrencyDao(provideDatabaseProvider.get());
    }

    private LevelProgressDao levelProgressDao() {
      return DatabaseModule_ProvideLevelProgressDaoFactory.provideLevelProgressDao(provideDatabaseProvider.get());
    }

    private InventoryDao inventoryDao() {
      return DatabaseModule_ProvideInventoryDaoFactory.provideInventoryDao(provideDatabaseProvider.get());
    }

    private GachaPityDao gachaPityDao() {
      return DatabaseModule_ProvideGachaPityDaoFactory.provideGachaPityDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 0));
    }

    @Override
    public void injectApp(App app) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.yourteam.cardgacharpg.core.database.AppDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
