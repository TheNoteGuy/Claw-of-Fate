package com.yourteam.cardgacharpg.feature.gacha.data;

import com.yourteam.cardgacharpg.core.database.AppDatabase;
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
public final class CurrencyManager_Factory implements Factory<CurrencyManager> {
  private final Provider<AppDatabase> dbProvider;

  private final Provider<CurrencyDao> currencyDaoProvider;

  public CurrencyManager_Factory(Provider<AppDatabase> dbProvider,
      Provider<CurrencyDao> currencyDaoProvider) {
    this.dbProvider = dbProvider;
    this.currencyDaoProvider = currencyDaoProvider;
  }

  @Override
  public CurrencyManager get() {
    return newInstance(dbProvider.get(), currencyDaoProvider.get());
  }

  public static CurrencyManager_Factory create(Provider<AppDatabase> dbProvider,
      Provider<CurrencyDao> currencyDaoProvider) {
    return new CurrencyManager_Factory(dbProvider, currencyDaoProvider);
  }

  public static CurrencyManager newInstance(AppDatabase db, CurrencyDao currencyDao) {
    return new CurrencyManager(db, currencyDao);
  }
}
