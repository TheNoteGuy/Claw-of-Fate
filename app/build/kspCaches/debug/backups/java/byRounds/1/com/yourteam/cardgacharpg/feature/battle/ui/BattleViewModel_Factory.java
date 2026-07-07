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
public final class BattleViewModel_Factory implements Factory<BattleViewModel> {
  @Override
  public BattleViewModel get() {
    return newInstance();
  }

  public static BattleViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static BattleViewModel newInstance() {
    return new BattleViewModel();
  }

  private static final class InstanceHolder {
    private static final BattleViewModel_Factory INSTANCE = new BattleViewModel_Factory();
  }
}
