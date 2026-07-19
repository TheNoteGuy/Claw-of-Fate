package com.yourteam.cardgacharpg.core.model

// Owner: Person 1 (Leila) — geteiltes Card-Modell
// Wird konsumiert von: Gacha (P2), Formation/Kampf (P3), Arena/Dashboard (P5)
//
// Enthält BEREITS berechnete/aktuelle Stats (nicht nur Basiswerte) — das Neuberechnen
// nach Level-Up passiert über StatCalculator und landet dann in diesen Feldern.

data class Card(
    val id: Int = 0,
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
    // Referenz auf deine Katzen-PNGs je Rarity-Stufe, z.B. "hero_firecat_legendary"
    val imageAssetName: String,
    // Anzahl identischer Exemplare (heroId + rarity) auf diesem Stapel — siehe CardRepository.insertAll():
    // Duplikate aus Gacha-Pulls erhoehen count, statt eine neue Zeile zu erzeugen.
    val count: Int = 1
)
