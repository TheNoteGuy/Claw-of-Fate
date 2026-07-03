package com.yourteam.cardgacharpg.core.model


// Common, Rare, Epic, Legendary
// Reihenfolge ist bewusst aufsteigend gewählt (ordinal wird u.a. für Sortierung genutzt)
enum class Rarity(
    val statMultiplier: Double,
    val maxLevel: Int
)
{
    COMMON(statMultiplier = 1.0, maxLevel = 20),
    RARE(statMultiplier = 1.2, maxLevel = 30),
    EPIC(statMultiplier = 1.45, maxLevel = 40),
    LEGENDARY(statMultiplier = 1.75, maxLevel = 50)
}