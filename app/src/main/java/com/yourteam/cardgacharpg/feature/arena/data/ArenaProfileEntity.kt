package com.yourteam.cardgacharpg.feature.arena.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Owner: Person 5 (Robin)
@Entity(tableName = "arena_profile")
data class ArenaProfileEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val trophies: Int = STARTING_TROPHIES,
    val weeklyArenaCount: Int = 0,
    val lastRewardTimestamp: Long = 0L
) {
    companion object {
        const val SINGLETON_ID = 0
        const val STARTING_TROPHIES = 50
    }
}