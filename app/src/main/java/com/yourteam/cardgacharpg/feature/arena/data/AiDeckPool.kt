package com.yourteam.cardgacharpg.feature.arena.data

import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.core.model.Element
import com.yourteam.cardgacharpg.core.model.Rarity
import com.yourteam.cardgacharpg.core.model.Role
import kotlin.random.Random

// Owner: Person 5 — 12 hardcoded AI decks
//
// FA07-Pflicht: Pool aus 12 handgebauten Formationen (unterschiedliche Elemente/Rollen),
// kein DB-Eintrag nötig, da im MVP unveränderlich. IDs sind bewusst negativ, damit sie nie
// mit echten Card-IDs aus der Spieler-DB kollidieren.
// Basiswerte sind Platzhalter — StatNormalization überschreibt currentHp/Atk/Def/Spd vor
// jedem Arena-Kampf ohnehin auf die feste Basislinie, nur `rarity` fließt in die Skalierung ein.
object AiDeckPool {

    private fun cat(id: Int, name: String, rarity: Rarity, element: Element, role: Role): Card = Card(
        id = id,
        heroId = id,
        name = name,
        rarity = rarity,
        element = element,
        role = role,
        level = 1,
        xp = 0,
        baseHp = 100,
        baseAtk = 20,
        baseDef = 10,
        baseSpd = 10,
        currentHp = 100,
        currentAtk = 20,
        currentDef = 10,
        currentSpd = 10,
        skill1Id = 0,
        skill2Id = 0,
        imageAssetName = "hero_ai_placeholder"
    )

    // Jedes Deck hat 5 Katzen mit gemischten Elementen/Rollen. Seltenheit steigt mit dem
    // Deck-Index leicht an, damit spätere Decks im Pool spürbar stärker sind.
    val decks: List<List<Card>> = listOf(
        listOf(
            cat(-1, "Ruß", Rarity.COMMON, Element.FEUER, Role.TANK),
            cat(-2, "Blattklaue", Rarity.COMMON, Element.NATUR, Role.KRIEGER),
            cat(-3, "Tropfen", Rarity.COMMON, Element.WASSER, Role.RANGER),
            cat(-4, "Fünkchen", Rarity.COMMON, Element.ARKAN, Role.MAGIER),
            cat(-5, "Pfötchen", Rarity.COMMON, Element.FEUER, Role.SUPPORT)
        ),
        listOf(
            cat(-6, "Glutmähne", Rarity.COMMON, Element.FEUER, Role.KRIEGER),
            cat(-7, "Moosbart", Rarity.COMMON, Element.NATUR, Role.TANK),
            cat(-8, "Welle", Rarity.RARE, Element.WASSER, Role.MAGIER),
            cat(-9, "Schattenpfote", Rarity.COMMON, Element.ARKAN, Role.RANGER),
            cat(-10, "Distel", Rarity.COMMON, Element.NATUR, Role.SUPPORT)
        ),
        listOf(
            cat(-11, "Aschekralle", Rarity.RARE, Element.FEUER, Role.TANK),
            cat(-12, "Ranke", Rarity.COMMON, Element.NATUR, Role.RANGER),
            cat(-13, "Gischt", Rarity.RARE, Element.WASSER, Role.KRIEGER),
            cat(-14, "Nebelfell", Rarity.COMMON, Element.ARKAN, Role.SUPPORT),
            cat(-15, "Dornrose", Rarity.RARE, Element.NATUR, Role.MAGIER)
        ),
        listOf(
            cat(-16, "Feuerdorn", Rarity.RARE, Element.FEUER, Role.MAGIER),
            cat(-17, "Sumpfpfote", Rarity.RARE, Element.NATUR, Role.TANK),
            cat(-18, "Sturzbach", Rarity.COMMON, Element.WASSER, Role.RANGER),
            cat(-19, "Mondschatten", Rarity.RARE, Element.ARKAN, Role.KRIEGER),
            cat(-20, "Kiesel", Rarity.COMMON, Element.WASSER, Role.SUPPORT)
        ),
        listOf(
            cat(-21, "Lohbrand", Rarity.RARE, Element.FEUER, Role.KRIEGER),
            cat(-22, "Efeukralle", Rarity.RARE, Element.NATUR, Role.SUPPORT),
            cat(-23, "Strömung", Rarity.RARE, Element.WASSER, Role.TANK),
            cat(-24, "Runenpfote", Rarity.EPIC, Element.ARKAN, Role.MAGIER),
            cat(-25, "Aschentropfen", Rarity.RARE, Element.FEUER, Role.RANGER)
        ),
        listOf(
            cat(-26, "Flammenwirbel", Rarity.EPIC, Element.FEUER, Role.TANK),
            cat(-27, "Wurzelherz", Rarity.RARE, Element.NATUR, Role.MAGIER),
            cat(-28, "Tiefenstrudel", Rarity.RARE, Element.WASSER, Role.RANGER),
            cat(-29, "Zwielichtfell", Rarity.EPIC, Element.ARKAN, Role.SUPPORT),
            cat(-30, "Rankenwip", Rarity.RARE, Element.NATUR, Role.KRIEGER)
        ),
        listOf(
            cat(-31, "Glutsturm", Rarity.EPIC, Element.FEUER, Role.MAGIER),
            cat(-32, "Baumriese", Rarity.EPIC, Element.NATUR, Role.TANK),
            cat(-33, "Regenschauer", Rarity.RARE, Element.WASSER, Role.SUPPORT),
            cat(-34, "Sternenpfote", Rarity.EPIC, Element.ARKAN, Role.KRIEGER),
            cat(-35, "Funkenschweif", Rarity.RARE, Element.FEUER, Role.RANGER)
        ),
        listOf(
            cat(-36, "Eisenkralle", Rarity.EPIC, Element.NATUR, Role.KRIEGER),
            cat(-37, "Flutbringer", Rarity.EPIC, Element.WASSER, Role.MAGIER),
            cat(-38, "Nachtglanz", Rarity.EPIC, Element.ARKAN, Role.RANGER),
            cat(-39, "Aschewolke", Rarity.RARE, Element.FEUER, Role.SUPPORT),
            cat(-40, "Dickicht", Rarity.EPIC, Element.NATUR, Role.TANK)
        ),
        listOf(
            cat(-41, "Vulkanherz", Rarity.EPIC, Element.FEUER, Role.TANK),
            cat(-42, "Sturzflut", Rarity.EPIC, Element.WASSER, Role.KRIEGER),
            cat(-43, "Traumfell", Rarity.EPIC, Element.ARKAN, Role.SUPPORT),
            cat(-44, "Blattsturm", Rarity.EPIC, Element.NATUR, Role.RANGER),
            cat(-45, "Glutkralle", Rarity.LEGENDARY, Element.FEUER, Role.MAGIER)
        ),
        listOf(
            cat(-46, "Urwaldwächter", Rarity.LEGENDARY, Element.NATUR, Role.TANK),
            cat(-47, "Wogenbrecher", Rarity.EPIC, Element.WASSER, Role.RANGER),
            cat(-48, "Sternenschatten", Rarity.EPIC, Element.ARKAN, Role.MAGIER),
            cat(-49, "Feuersbrunst", Rarity.EPIC, Element.FEUER, Role.KRIEGER),
            cat(-50, "Rankenheiler", Rarity.RARE, Element.NATUR, Role.SUPPORT)
        ),
        listOf(
            cat(-51, "Tsunamikralle", Rarity.LEGENDARY, Element.WASSER, Role.TANK),
            cat(-52, "Arkanschimmer", Rarity.LEGENDARY, Element.ARKAN, Role.MAGIER),
            cat(-53, "Waldkoloss", Rarity.EPIC, Element.NATUR, Role.KRIEGER),
            cat(-54, "Feuerspringer", Rarity.EPIC, Element.FEUER, Role.RANGER),
            cat(-55, "Nebeltropfen", Rarity.EPIC, Element.WASSER, Role.SUPPORT)
        ),
        listOf(
            cat(-56, "Phönixkralle", Rarity.LEGENDARY, Element.FEUER, Role.KRIEGER),
            cat(-57, "Weltenwurzel", Rarity.LEGENDARY, Element.NATUR, Role.MAGIER),
            cat(-58, "Sturmflut", Rarity.LEGENDARY, Element.WASSER, Role.RANGER),
            cat(-59, "Leerenschatten", Rarity.LEGENDARY, Element.ARKAN, Role.TANK),
            cat(-60, "Sternenheiler", Rarity.EPIC, Element.ARKAN, Role.SUPPORT)
        )
    )

    init {
        check(decks.size == 12) { "AiDeckPool muss laut GDD genau 12 Decks enthalten." }
    }

    fun randomDeck(rng: Random = Random.Default): List<Card> = decks.random(rng)
}