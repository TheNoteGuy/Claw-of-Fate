package com.yourteam.cardgacharpg.feature.battle.ui;

import com.yourteam.cardgacharpg.feature.battle.data.FormationRepository;
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository;
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
public final class FormationViewModel_Factory implements Factory<FormationViewModel> {
  private final Provider<FormationRepository> formationRepositoryProvider;

  private final Provider<CardRepository> cardRepositoryProvider;

  public FormationViewModel_Factory(Provider<FormationRepository> formationRepositoryProvider,
      Provider<CardRepository> cardRepositoryProvider) {
    this.formationRepositoryProvider = formationRepositoryProvider;
    this.cardRepositoryProvider = cardRepositoryProvider;
  }

  @Override
  public FormationViewModel get() {
    return newInstance(formationRepositoryProvider.get(), cardRepositoryProvider.get());
  }

  public static FormationViewModel_Factory create(
      Provider<FormationRepository> formationRepositoryProvider,
      Provider<CardRepository> cardRepositoryProvider) {
    return new FormationViewModel_Factory(formationRepositoryProvider, cardRepositoryProvider);
  }

  public static FormationViewModel newInstance(FormationRepository formationRepository,
      CardRepository cardRepository) {
    return new FormationViewModel(formationRepository, cardRepository);
  }
}
