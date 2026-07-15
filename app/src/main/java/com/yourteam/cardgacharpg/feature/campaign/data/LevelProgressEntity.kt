package com.yourteam.cardgacharpg.feature.campaign.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Owner: Person 4 (Yassin)
// Speichert den Fortschritt für jedes einzelne der 10 Kampagnen-Level (Akt 1).
// Eine eine pro Level
@Entity(tableName = "level_progress")
data class LevelProgressEntity(
    @PrimaryKey
    val levelId: Int,

    // Standardmäßig sind Level erst einmal gesperrt, wenn sie in die Datenbank kommen
    val isUnlocked: Boolean = false,

    // Standardmäßig hat man in einem Level noch keine Sterne gesammelt
    val stars: Int = 0
) {
    companion object {
        // Konstanten entsprechen GDD-Vorgaben
        const val TOTAL_LEVELS = 10
        const val MAX_STARS_PER_LEVEL = 3
    }
}