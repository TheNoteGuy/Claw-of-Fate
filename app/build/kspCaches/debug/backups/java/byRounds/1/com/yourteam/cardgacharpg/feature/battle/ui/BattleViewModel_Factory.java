package com.yourteam.cardgacharpg.feature.battle.ui;

import com.yourteam.cardgacharpg.feature.battle.data.FormationRepository;
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
public final class BattleViewModel_Factory implements Factory<BattleViewModel> {
  private final Provider<FormationRepository> formationRepositoryProvider;

  public BattleViewModel_Factory(Provider<FormationRepository> formationRepositoryProvider) {
    this.formationRepositoryProvider = formationRepositoryProvider;
  }

  @Override
  public BattleViewModel get() {
    return newInstance(formationRepositoryProvider.get());
  }

  public static BattleViewModel_Factory create(
      Provider<FormationRepository> formationRepositoryProvider) {
    return new BattleViewModel_Factory(formationRepositoryProvider);
  }

  public static BattleViewModel newInstance(FormationRepository formationRepository) {
    return new BattleViewModel(formationRepository);
  }
}
