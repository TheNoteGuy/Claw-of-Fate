package com.yourteam.cardgacharpg.core.di;

import com.yourteam.cardgacharpg.core.database.AppDatabase;
import com.yourteam.cardgacharpg.feature.arena.data.ArenaDao;
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
public final class DatabaseModule_ProvideArenaDaoFactory implements Factory<ArenaDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideArenaDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ArenaDao get() {
    return provideArenaDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideArenaDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideArenaDaoFactory(dbProvider);
  }

  public static ArenaDao provideArenaDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideArenaDao(db));
  }
}
