package com.yourteam.cardgacharpg.feature.campaign.ui;

import com.yourteam.cardgacharpg.feature.campaign.data.CampaignRepository;
import com.yourteam.cardgacharpg.feature.campaign.domain.CompleteCampaignLevelUseCase;
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
public final class CampaignViewModel_Factory implements Factory<CampaignViewModel> {
  private final Provider<CampaignRepository> campaignRepositoryProvider;

  private final Provider<CompleteCampaignLevelUseCase> completeCampaignLevelUseCaseProvider;

  public CampaignViewModel_Factory(Provider<CampaignRepository> campaignRepositoryProvider,
      Provider<CompleteCampaignLevelUseCase> completeCampaignLevelUseCaseProvider) {
    this.campaignRepositoryProvider = campaignRepositoryProvider;
    this.completeCampaignLevelUseCaseProvider = completeCampaignLevelUseCaseProvider;
  }

  @Override
  public CampaignViewModel get() {
    return newInstance(campaignRepositoryProvider.get(), completeCampaignLevelUseCaseProvider.get());
  }

  public static CampaignViewModel_Factory create(
      Provider<CampaignRepository> campaignRepositoryProvider,
      Provider<CompleteCampaignLevelUseCase> completeCampaignLevelUseCaseProvider) {
    return new CampaignViewModel_Factory(campaignRepositoryProvider, completeCampaignLevelUseCaseProvider);
  }

  public static CampaignViewModel newInstance(CampaignRepository campaignRepository,
      CompleteCampaignLevelUseCase completeCampaignLevelUseCase) {
    return new CampaignViewModel(campaignRepository, completeCampaignLevelUseCase);
  }
}
