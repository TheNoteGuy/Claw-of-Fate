package com.yourteam.cardgacharpg.feature.campaign.ui;

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
public final class CampaignViewModel_Factory implements Factory<CampaignViewModel> {
  @Override
  public CampaignViewModel get() {
    return newInstance();
  }

  public static CampaignViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CampaignViewModel newInstance() {
    return new CampaignViewModel();
  }

  private static final class InstanceHolder {
    private static final CampaignViewModel_Factory INSTANCE = new CampaignViewModel_Factory();
  }
}
