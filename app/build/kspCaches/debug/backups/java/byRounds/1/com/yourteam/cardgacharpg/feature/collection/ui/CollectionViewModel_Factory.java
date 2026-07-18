package com.yourteam.cardgacharpg.feature.collection.ui;

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
public final class CollectionViewModel_Factory implements Factory<CollectionViewModel> {
  private final Provider<CardRepository> cardRepositoryProvider;

  public CollectionViewModel_Factory(Provider<CardRepository> cardRepositoryProvider) {
    this.cardRepositoryProvider = cardRepositoryProvider;
  }

  @Override
  public CollectionViewModel get() {
    return newInstance(cardRepositoryProvider.get());
  }

  public static CollectionViewModel_Factory create(
      Provider<CardRepository> cardRepositoryProvider) {
    return new CollectionViewModel_Factory(cardRepositoryProvider);
  }

  public static CollectionViewModel newInstance(CardRepository cardRepository) {
    return new CollectionViewModel(cardRepository);
  }
}
