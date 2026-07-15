package com.yourteam.cardgacharpg.feature.arena.domain;

import com.yourteam.cardgacharpg.feature.arena.data.ArenaDao;
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
public final class TrophyManager_Factory implements Factory<TrophyManager> {
  private final Provider<ArenaDao> arenaDaoProvider;

  public TrophyManager_Factory(Provider<ArenaDao> arenaDaoProvider) {
    this.arenaDaoProvider = arenaDaoProvider;
  }

  @Override
  public TrophyManager get() {
    return newInstance(arenaDaoProvider.get());
  }

  public static TrophyManager_Factory create(Provider<ArenaDao> arenaDaoProvider) {
    return new TrophyManager_Factory(arenaDaoProvider);
  }

  public static TrophyManager newInstance(ArenaDao arenaDao) {
    return new TrophyManager(arenaDao);
  }
}
