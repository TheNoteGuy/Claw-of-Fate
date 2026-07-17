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
// UPDATE: Alle 4 Elemente (Feuer/Natur/Wasser/Arkan) sind jetzt je Seltenheit vertreten
// (4 Elemente × 4 Seltenheiten = 16 Helden), damit eine 6er-Formation (Person 3) auch wirklich
// mit unterschiedlichen Elementen/Rollen befüllt werden kann. Assets liegen bereits vollständig
// unter res/drawable/ (siehe CardImageProvider, Person 1) — hero_firecat_*, hero_nature_*,
// hero_water_*, hero_mage_* (Arkan-Katzen nutzen das "mage"-Assetset).
//
//  - Stat-Berechnung läuft über Person 1s StatCalculator (EINE Quelle der Wahrheit): wir bauen
//    eine Level-1-Basiskarte und lassen StatCalculator.recalculate(...) die currentXxx-Felder füllen.
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

    // Pro Seltenheit ein Held je Element (4 Elemente), Rollen bewusst gemischt, damit man
    // beim Pullen realistische Formationen (Tank/Krieger/Ranger/Magier/Support) zusammenbekommt.
    private val rosterByRarity: Map<Rarity, List<Template>> = mapOf(
        Rarity.COMMON to listOf(
            Template(1, "Glutkätzchen", Element.FEUER, Role.KRIEGER,
                baseHp = 620, baseAtk = 95, baseDef = 40, baseSpd = 55,
                skill1Id = 101, skill2Id = 102, imageAssetName = "hero_firecat_common"),
            Template(5, "Blattpfote", Element.NATUR, Role.TANK,
                baseHp = 700, baseAtk = 75, baseDef = 55, baseSpd = 45,
                skill1Id = 109, skill2Id = 110, imageAssetName = "hero_nature_common"),
            Template(9, "Tropfenkatze", Element.WASSER, Role.RANGER,
                baseHp = 600, baseAtk = 100, baseDef = 35, baseSpd = 60,
                skill1Id = 117, skill2Id = 118, imageAssetName = "hero_water_common"),
            Template(13, "Fünkchenkatze", Element.ARKAN, Role.MAGIER,
                baseHp = 580, baseAtk = 105, baseDef = 30, baseSpd = 50,
                skill1Id = 125, skill2Id = 126, imageAssetName = "hero_mage_common")
        ),
        Rarity.RARE to listOf(
            Template(2, "Flammenpfote", Element.FEUER, Role.MAGIER,
                baseHp = 640, baseAtk = 120, baseDef = 38, baseSpd = 62,
                skill1Id = 103, skill2Id = 104, imageAssetName = "hero_firecat_rare"),
            Template(6, "Dornranke", Element.NATUR, Role.SUPPORT,
                baseHp = 660, baseAtk = 90, baseDef = 45, baseSpd = 58,
                skill1Id = 111, skill2Id = 112, imageAssetName = "hero_nature_rare"),
            Template(10, "Wellenläufer", Element.WASSER, Role.KRIEGER,
                baseHp = 630, baseAtk = 118, baseDef = 42, baseSpd = 65,
                skill1Id = 119, skill2Id = 120, imageAssetName = "hero_water_rare"),
            Template(14, "Runenpfote", Element.ARKAN, Role.MAGIER,
                baseHp = 610, baseAtk = 128, baseDef = 36, baseSpd = 56,
                skill1Id = 127, skill2Id = 128, imageAssetName = "hero_mage_rare")
        ),
        Rarity.EPIC to listOf(
            Template(3, "Aschekralle", Element.FEUER, Role.RANGER,
                baseHp = 700, baseAtk = 140, baseDef = 48, baseSpd = 70,
                skill1Id = 105, skill2Id = 106, imageAssetName = "hero_firecat_epic"),
            Template(7, "Waldwächterin", Element.NATUR, Role.TANK,
                baseHp = 780, baseAtk = 110, baseDef = 65, baseSpd = 60,
                skill1Id = 113, skill2Id = 114, imageAssetName = "hero_nature_epic"),
            Template(11, "Gischtklaue", Element.WASSER, Role.RANGER,
                baseHp = 690, baseAtk = 135, baseDef = 50, baseSpd = 75,
                skill1Id = 121, skill2Id = 122, imageAssetName = "hero_water_epic"),
            Template(15, "Sternenschimmer", Element.ARKAN, Role.MAGIER,
                baseHp = 670, baseAtk = 150, baseDef = 44, baseSpd = 64,
                skill1Id = 129, skill2Id = 130, imageAssetName = "hero_mage_epic")
        ),
        Rarity.LEGENDARY to listOf(
            Template(4, "Infernokönig", Element.FEUER, Role.KRIEGER,
                baseHp = 900, baseAtk = 170, baseDef = 60, baseSpd = 80,
                skill1Id = 107, skill2Id = 108, imageAssetName = "hero_firecat_legendary"),
            Template(8, "Weltenbaumherz", Element.NATUR, Role.SUPPORT,
                baseHp = 950, baseAtk = 130, baseDef = 75, baseSpd = 68,
                skill1Id = 115, skill2Id = 116, imageAssetName = "hero_nature_legendary"),
            Template(12, "Tsunamikönigin", Element.WASSER, Role.KRIEGER,
                baseHp = 880, baseAtk = 165, baseDef = 58, baseSpd = 85,
                skill1Id = 123, skill2Id = 124, imageAssetName = "hero_water_legendary"),
            Template(16, "Arkanmonarchin", Element.ARKAN, Role.MAGIER,
                baseHp = 850, baseAtk = 180, baseDef = 52, baseSpd = 72,
                skill1Id = 131, skill2Id = 132, imageAssetName = "hero_mage_legendary")
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