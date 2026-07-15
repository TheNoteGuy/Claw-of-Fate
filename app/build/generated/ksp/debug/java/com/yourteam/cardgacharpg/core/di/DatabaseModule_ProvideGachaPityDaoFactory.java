package com.yourteam.cardgacharpg.core.di;

import com.yourteam.cardgacharpg.core.database.AppDatabase;
import com.yourteam.cardgacharpg.feature.gacha.data.GachaPityDao;
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
public final class DatabaseModule_ProvideGachaPityDaoFactory implements Factory<GachaPityDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideGachaPityDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public GachaPityDao get() {
    return provideGachaPityDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideGachaPityDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideGachaPityDaoFactory(dbProvider);
  }

  public static GachaPityDao provideGachaPityDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideGachaPityDao(db));
  }
}
