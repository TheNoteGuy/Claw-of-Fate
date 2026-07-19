package com.yourteam.cardgacharpg.feature.campaign.domain

// Owner: Person 4 (Yassin) — BelohnungsTabelle laut Projektplan ("Level 1–10 je Gem-/Gold-/XP-Wert").
//
// XP-Traenke sind hier die ZENTRALE Quelle fuers Helden-Leveling (FA06): LevelUpUseCase und
// StatCalculator (Person 1) waren fertig, aber es gab bisher keinerlei Zufluss von
// ItemType.XP_POTION — der Spieler konnte also nie leveln. Kampagnen-Siege schuetten jetzt
// Traenke aus (Erstclear deutlich mehr, Wiederholungen ein Grundeinkommen zum Farmen).
//
// Gems-Erstclear bewegt sich im GDD-Rahmen "50–200 Gems pro Level" (F2P-Pfad, GDD 4.4).

data class CampaignRewards(
    val gold: Int,
    val gems: Int,
    val xpPotions: Int
) {
    companion object {
        val NONE = CampaignRewards(gold = 0, gems = 0, xpPotions = 0)
    }
}

object CampaignRewardTable {

    /**
     * Belohnung fuer einen SIEG in [levelId] (1..10).
     * [firstClear] = das Level wurde zum ersten Mal erfolgreich abgeschlossen.
     */
    fun rewardFor(levelId: Int, firstClear: Boolean): CampaignRewards {
        require(levelId in 1..10) { "levelId muss zwischen 1 und 10 liegen, war $levelId" }
        return if (firstClear) {
            CampaignRewards(
                gold = 40 + levelId * 15,             // 55 .. 190
                gems = 50 + (levelId - 1) * 15,       // 50 .. 185 (GDD: 50–200 Erstclear)
                xpPotions = 2 + (levelId + 1) / 3     // 2 .. 5
            )
        } else {
            // Wiederholtes Farmen: kleiner, aber nie leer ausgehen (mind. 1 Trank).
            CampaignRewards(
                gold = 20 + levelId * 8,              // 28 .. 100
                gems = 0,
                xpPotions = 1 + levelId / 5           // 1 .. 3
            )
        }
    }
}
