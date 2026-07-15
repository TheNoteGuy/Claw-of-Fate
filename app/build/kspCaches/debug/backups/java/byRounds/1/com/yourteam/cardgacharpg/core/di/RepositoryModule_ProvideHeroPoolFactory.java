package com.yourteam.cardgacharpg.core.di;

import com.yourteam.cardgacharpg.feature.gacha.domain.HeroPool;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class RepositoryModule_ProvideHeroPoolFactory implements Factory<HeroPool> {
  @Override
  public HeroPool get() {
    return provideHeroPool();
  }

  public static RepositoryModule_ProvideHeroPoolFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static HeroPool provideHeroPool() {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideHeroPool());
  }

  private static final class InstanceHolder {
    private static final RepositoryModule_ProvideHeroPoolFactory INSTANCE = new RepositoryModule_ProvideHeroPoolFactory();
  }
}
