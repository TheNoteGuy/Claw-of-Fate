package com.yourteam.cardgacharpg.feature.arena.ui;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ArenaViewModel_Factory implements Factory<ArenaViewModel> {
  @Override
  public ArenaViewModel get() {
    return newInstance();
  }

  public static ArenaViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ArenaViewModel newInstance() {
    return new ArenaViewModel();
  }

  private static final class InstanceHolder {
    private static final ArenaViewModel_Factory INSTANCE = new ArenaViewModel_Factory();
  }
}
