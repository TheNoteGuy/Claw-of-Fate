package com.yourteam.cardgacharpg.feature.battle.ui;

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
public final class FormationViewModel_Factory implements Factory<FormationViewModel> {
  @Override
  public FormationViewModel get() {
    return newInstance();
  }

  public static FormationViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FormationViewModel newInstance() {
    return new FormationViewModel();
  }

  private static final class InstanceHolder {
    private static final FormationViewModel_Factory INSTANCE = new FormationViewModel_Factory();
  }
}
