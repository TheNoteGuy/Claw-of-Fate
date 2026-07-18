package com.yourteam.cardgacharpg.core.di;

import com.yourteam.cardgacharpg.core.database.AppDatabase;
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyDao;
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
public final class DatabaseModule_ProvideCurrencyDaoFactory implements Factory<CurrencyDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideCurrencyDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CurrencyDao get() {
    return provideCurrencyDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideCurrencyDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideCurrencyDaoFactory(dbProvider);
  }

  public static CurrencyDao provideCurrencyDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCurrencyDao(db));
  }
}
