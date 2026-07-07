package com.yourteam.cardgacharpg.feature.gacha.ui;

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
public final class GachaViewModel_Factory implements Factory<GachaViewModel> {
  @Override
  public GachaViewModel get() {
    return newInstance();
  }

  public static GachaViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GachaViewModel newInstance() {
    return new GachaViewModel();
  }

  private static final class InstanceHolder {
    private static final GachaViewModel_Factory INSTANCE = new GachaViewModel_Factory();
  }
}
