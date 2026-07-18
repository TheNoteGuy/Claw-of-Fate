package com.yourteam.cardgacharpg.core.di;

import com.yourteam.cardgacharpg.core.database.AppDatabase;
import com.yourteam.cardgacharpg.feature.collection.data.CardDao;
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
public final class DatabaseModule_ProvideCardDaoFactory implements Factory<CardDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideCardDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CardDao get() {
    return provideCardDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideCardDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideCardDaoFactory(dbProvider);
  }

  public static CardDao provideCardDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCardDao(db));
  }
}
