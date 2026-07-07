package com.yourteam.cardgacharpg.core.di;

import com.yourteam.cardgacharpg.core.database.AppDatabase;
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideInventoryDaoFactory implements Factory<InventoryDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideInventoryDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public InventoryDao get() {
    return provideInventoryDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideInventoryDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideInventoryDaoFactory(dbProvider);
  }

  public static InventoryDao provideInventoryDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideInventoryDao(db));
  }
}
