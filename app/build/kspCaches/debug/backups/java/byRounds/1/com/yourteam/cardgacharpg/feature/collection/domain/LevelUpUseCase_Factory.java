package com.yourteam.cardgacharpg.feature.collection.domain;

import com.yourteam.cardgacharpg.feature.collection.data.CardRepository;
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao;
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
public final class LevelUpUseCase_Factory implements Factory<LevelUpUseCase> {
  private final Provider<CardRepository> cardRepositoryProvider;

  private final Provider<InventoryDao> inventoryDaoProvider;

  public LevelUpUseCase_Factory(Provider<CardRepository> cardRepositoryProvider,
      Provider<InventoryDao> inventoryDaoProvider) {
    this.cardRepositoryProvider = cardRepositoryProvider;
    this.inventoryDaoProvider = inventoryDaoProvider;
  }

  @Override
  public LevelUpUseCase get() {
    return newInstance(cardRepositoryProvider.get(), inventoryDaoProvider.get());
  }

  public static LevelUpUseCase_Factory create(Provider<CardRepository> cardRepositoryProvider,
      Provider<InventoryDao> inventoryDaoProvider) {
    return new LevelUpUseCase_Factory(cardRepositoryProvider, inventoryDaoProvider);
  }

  public static LevelUpUseCase newInstance(CardRepository cardRepository,
      InventoryDao inventoryDao) {
    return new LevelUpUseCase(cardRepository, inventoryDao);
  }
}
