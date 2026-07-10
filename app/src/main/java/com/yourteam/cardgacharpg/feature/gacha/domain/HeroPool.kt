package com.yourteam.cardgacharpg.feature.gacha.domain

import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.core.model.Element
import com.yourteam.cardgacharpg.core.model.Rarity
import com.yourteam.cardgacharpg.core.model.Role
import com.yourteam.cardgacharpg.feature.collection.domain.StatCalculator
import kotlin.random.Random

// Owner: Person 2 (Nico)
// GachaEngine liefert nur eine *Seltenheit*. HeroPool macht daraus eine konkrete Level-1-Karte.
//
// UPDATE (P1 hat "card stuff" fertig):
//  - Die Stat-Berechnung läuft jetzt über Person 1s StatCalculator (EINE Quelle der Wahrheit),
//    statt sie hier selbst zu rechnen. Wir bauen eine Level-1-Basiskarte und lassen
//    StatCalculator.recalculate(...) die currentXxx-Felder füllen.
//  - imageAssetName zeigt auf die real vorhandenen Drawables (hero_firecat_common/rare/epic).
//    ⚠ Für LEGENDARY gibt es noch kein Asset -> wir nutzen vorerst das Epic-Bild.
//      Bei Person 1 ein hero_firecat_legendary (o.ä.) nachfordern.
interface HeroPool {
    /** Liefert eine frische Level-1-Karte eines zufälligen Helden der gegebenen [rarity]. */
    fun randomHeroOf(rarity: Rarity, rng: Random): Card
}

class CatHeroPool : HeroPool {

    private data class Template(
        val heroId: Int,
        val name: String,
        val element: Element,
        val role: Role,
        val baseHp: Int,
        val baseAtk: Int,
        val baseDef: Int,
        val baseSpd: Int,
        val skill1Id: Int,
        val skill2Id: Int,
        val imageAssetName: String
    )

    // Nur EIN Katzen-Art-Set existiert bisher (firecat in common/rare/epic).
    // Deshalb aktuell ein Held je Seltenheit. Sobald Person 1 mehr Assets liefert,
    // hier einfach weitere Templates ergänzen (Liste je Rarity ist bereits vorbereitet).
    private val rosterByRarity: Map<Rarity, List<Template>> = mapOf(
        Rarity.COMMON to listOf(
            Template(1, "Glutkätzchen", Element.FEUER, Role.KRIEGER,
                baseHp = 620, baseAtk = 95, baseDef = 40, baseSpd = 55,
                skill1Id = 101, skill2Id = 102, imageAssetName = "hero_firecat_common")
        ),
        Rarity.RARE to listOf(
            Template(2, "Flammenpfote", Element.FEUER, Role.MAGIER,
                baseHp = 640, baseAtk = 120, baseDef = 38, baseSpd = 62,
                skill1Id = 103, skill2Id = 104, imageAssetName = "hero_firecat_rare")
        ),
        Rarity.EPIC to listOf(
            Template(3, "Aschekralle", Element.FEUER, Role.RANGER,
                baseHp = 700, baseAtk = 140, baseDef = 48, baseSpd = 70,
                skill1Id = 105, skill2Id = 106, imageAssetName = "hero_firecat_epic")
        ),
        Rarity.LEGENDARY to listOf(
            // TODO: eigenes Legendary-Asset von Person 1 -> vorerst Epic-Bild als Platzhalter.
            Template(4, "Infernokönig", Element.FEUER, Role.KRIEGER,
                baseHp = 900, baseAtk = 170, baseDef = 60, baseSpd = 80,
                skill1Id = 107, skill2Id = 108, imageAssetName = "hero_firecat_legendary")
        )
    )

    override fun randomHeroOf(rarity: Rarity, rng: Random): Card {
        val pool = rosterByRarity[rarity] ?: error("Kein Held für $rarity im Pool")
        val t = pool[rng.nextInt(pool.size)]

        // Level-1-Basiskarte bauen; currentXxx werden gleich von StatCalculator überschrieben.
        val base = Card(
            id = 0,                 // Room vergibt die ID beim Insert
            heroId = t.heroId,
            name = t.name,
            rarity = rarity,
            element = t.element,
            role = t.role,
            level = 1,
            xp = 0,
            baseHp = t.baseHp,
            baseAtk = t.baseAtk,
            baseDef = t.baseDef,
            baseSpd = t.baseSpd,
            currentHp = 0,
            currentAtk = 0,
            currentDef = 0,
            currentSpd = 0,
            skill1Id = t.skill1Id,
            skill2Id = t.skill2Id,
            imageAssetName = t.imageAssetName
        )

        // Person 1s StatCalculator füllt currentHp/Atk/Def/Spd (base * Rarity * Level-Faktor).
        return StatCalculator.recalculate(base)
    }
}