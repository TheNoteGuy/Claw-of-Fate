package com.yourteam.cardgacharpg.feature.campaign.domain

import com.yourteam.cardgacharpg.feature.campaign.data.CampaignRepository
import com.yourteam.cardgacharpg.feature.campaign.data.LevelProgressEntity
import com.yourteam.cardgacharpg.feature.collection.data.InventoryDao
import com.yourteam.cardgacharpg.feature.collection.data.ItemType
import com.yourteam.cardgacharpg.feature.gacha.data.CurrencyManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

// Owner: Person 4 (Yassin) — schliesst ein Kampagnen-Level nach dem Kampf ab:
//   1. Sterne berechnen (StarRatingUseCase) und Bestwert persistieren
//   2. Naechstes Level freischalten (Progression: Level N+1 entsperrt bei >= 1 Stern)
//   3. Belohnungen gutschreiben:
//        Gold/Gems -> CurrencyManager (Person 2, atomare Transaktionen)
//        XP-Traenke -> InventoryDao.addAmount() (Person 1; kapselt adjustAmount() inkl.
//                      Anlage der Zeile beim allerersten Trank)
//
// Wird vom Kampagnen-Kampf-Flow (CampaignBattleViewModel) genau EINMAL pro Kampf aufgerufen;
// CampaignViewModel.onBattleFinished delegiert ebenfalls hierher (eine Quelle der Wahrheit).

data class CampaignBattleOutcome(
    val levelId: Int,
    val isVictory: Boolean,
    val starsEarned: Int,
    val firstClear: Boolean,
    val rewards: CampaignRewards
)

class CompleteCampaignLevelUseCase @Inject constructor(
    private val campaignRepository: CampaignRepository,
    private val starRatingUseCase: StarRatingUseCase,
    private val currencyManager: CurrencyManager,
    private val inventoryDao: InventoryDao
) {

    suspend operator fun invoke(
        levelId: Int,
        isVictory: Boolean,
        survivingUnits: Int,
        totalUnits: Int
    ): CampaignBattleOutcome {
        val earnedStars = starRatingUseCase.calculateStars(isVictory, survivingUnits, totalUnits)

        // Direkt aus dem Repository lesen (nicht aus einem UI-State-Snapshot), damit der
        // Abschluss auch funktioniert, wenn die Kampagnen-Map gerade nicht subscribed ist.
        val levels = campaignRepository.getAllLevelProgress().first()
        val playedLevel = levels.find { it.levelId == levelId }
            ?: return CampaignBattleOutcome(levelId, isVictory, earnedStars, firstClear = false, rewards = CampaignRewards.NONE)

        val firstClear = isVictory && playedLevel.stars == 0

        if (earnedStars > 0) {
            // Sterne nur verbessern, nie verschlechtern
            if (earnedStars > playedLevel.stars) {
                campaignRepository.updateLevelProgress(playedLevel.copy(stars = earnedStars))
            }
            // Naechstes Level freischalten
            if (levelId < LevelProgressEntity.TOTAL_LEVELS) {
                val nextLevel = levels.find { it.levelId == levelId + 1 }
                if (nextLevel != null && !nextLevel.isUnlocked) {
                    campaignRepository.updateLevelProgress(nextLevel.copy(isUnlocked = true))
                }
            }
        }

        val rewards = if (isVictory) CampaignRewardTable.rewardFor(levelId, firstClear) else CampaignRewards.NONE
        if (rewards.gold > 0) currencyManager.addGold(rewards.gold)
        if (rewards.gems > 0) currencyManager.addGems(rewards.gems)
        if (rewards.xpPotions > 0) inventoryDao.addAmount(ItemType.XP_POTION, rewards.xpPotions)

        return CampaignBattleOutcome(
            levelId = levelId,
            isVictory = isVictory,
            starsEarned = earnedStars,
            firstClear = firstClear,
            rewards = rewards
        )
    }
}
