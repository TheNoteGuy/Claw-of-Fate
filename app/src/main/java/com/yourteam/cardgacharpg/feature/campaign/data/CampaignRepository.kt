package com.yourteam.cardgacharpg.feature.campaign.data

import com.yourteam.cardgacharpg.feature.campaign.domain.EnemyFormationProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Owner: Person 4 (Yassin) — loads seed level data
// Das Repository ist die zentrale Anlaufstelle für das Kampagnen-Feature.
class CampaignRepository @Inject constructor(
    private val levelProgressDao: LevelProgressDao,
    private val enemyFormationProvider: EnemyFormationProvider
){
    // 1. Fortschritt als Live-Stream (Flow) für die UI bereitstellen
    fun getAllLevelProgress(): Flow<List<LevelProgressEntity>> {
        return levelProgressDao.getAllLevelProgress()
    }

    // 2. Gegnerformation für ein bestimmtes Level laden
    fun getEnemyFormation(levelId: Int): EnemyFormationEntity {
        return enemyFormationProvider.getFormationForLevel(levelId)
    }

    // 3. Beim App-Start die 10 Level in die Datenbank einfügen
    suspend fun initializeLevels() {
        // Wir generieren automatisch eine Liste mit 10 Leveln (IDs 1 bis 10)
        val initialLevels = (1..LevelProgressEntity.TOTAL_LEVELS).map { id ->
            // isUnlocked ist standardmäßig false, außer bei Level 1 (das muss offen sein!)
            LevelProgressEntity(
                levelId = id,
                isUnlocked = id == 1
            )
        }
        // ... und schicken diese Liste an den Lagerarbeiter (Dao)
        levelProgressDao.insertInitialLevels(initialLevels)
    }

    // 4. Einen neuen Fortschritt speichern (z.B. 3 Sterne nach einem Sieg)
    suspend fun updateLevelProgress(level: LevelProgressEntity) {
        levelProgressDao.updateLevel(level)
    }

}
