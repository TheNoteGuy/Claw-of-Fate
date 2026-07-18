package com.yourteam.cardgacharpg.feature.battle.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yourteam.cardgacharpg.core.model.Card

// Owner: Person 3 (Marc) — slot0..slot5 as Card IDs
@Entity(tableName = "formations")
data class FormationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var name: String,

    var slot0: Int,
    var slot1: Int,
    var slot2: Int,
    var slot3: Int,
    var slot4: Int,
    var slot5: Int,
) {
    constructor(name: String) : this(0, name, -1, -1, -1, -1, -1, -1)
}

@Entity(tableName = "active_formation")
data class ActiveFormationEntity(
    @PrimaryKey
    val id: Long,
)