package com.yourteam.cardgacharpg.feature.collection.ui;

import androidx.lifecycle.SavedStateHandle;
import com.yourteam.cardgacharpg.feature.collection.data.CardRepository;
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao;
import com.yourteam.cardgacharpg.feature.collection.domain.LevelUpUseCase;
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
public final class CardDetailViewModel_Factory implements Factory<CardDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<CardRepository> cardRepositoryProvider;

  private final Provider<InventoryDao> inventoryDaoProvider;

  private final Provider<LevelUpUseCase> levelUpUseCaseProvider;

  public CardDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<CardRepository> cardRepositoryProvider, Provider<InventoryDao> inventoryDaoProvider,
      Provider<LevelUpUseCase> levelUpUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.cardRepositoryProvider = cardRepositoryProvider;
    this.inventoryDaoProvider = inventoryDaoProvider;
    this.levelUpUseCaseProvider = levelUpUseCaseProvider;
  }

  @Override
  public CardDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), cardRepositoryProvider.get(), inventoryDaoProvider.get(), levelUpUseCaseProvider.get());
  }

  public static CardDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<CardRepository> cardRepositoryProvider, Provider<InventoryDao> inventoryDaoProvider,
      Provider<LevelUpUseCase> levelUpUseCaseProvider) {
    return new CardDetailViewModel_Factory(savedStateHandleProvider, cardRepositoryProvider, inventoryDaoProvider, levelUpUseCaseProvider);
  }

  public static CardDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      CardRepository cardRepository, InventoryDao inventoryDao, LevelUpUseCase levelUpUseCase) {
    return new CardDetailViewModel(savedStateHandle, cardRepository, inventoryDao, levelUpUseCase);
  }
}
