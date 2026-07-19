package com.yourteam.cardgacharpg.feature.collection.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yourteam.cardgacharpg.core.model.Element
import com.yourteam.cardgacharpg.core.model.Rarity
import com.yourteam.cardgacharpg.core.model.Role

// Owner: Person 1 (Leila) — Room entity
// currentHp/Atk/Def/Spd werden von StatCalculator geschrieben (Basis * Rarity-Multiplikator * Level-Faktor)

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val heroId: Int,
    val name: String,
    val rarity: Rarity,
    val element: Element,
    val role: Role,
    val level: Int,
    val xp: Int,
    val baseHp: Int,
    val baseAtk: Int,
    val baseDef: Int,
    val baseSpd: Int,
    val currentHp: Int,
    val currentAtk: Int,
    val currentDef: Int,
    val currentSpd: Int,
    val skill1Id: Int,
    val skill2Id: Int,
    val imageAssetName: String,
    // Stack-Zaehler fuer Duplikate (gleicher heroId + rarity). defaultValue MUSS zur
    // Migration 4->5 in AppDatabase passen (ALTER TABLE ... DEFAULT 1).
    @ColumnInfo(defaultValue = "1") val count: Int = 1
)