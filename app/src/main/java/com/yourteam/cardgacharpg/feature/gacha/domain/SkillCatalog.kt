package com.yourteam.cardgacharpg.feature.collection.domain

import com.yourteam.cardgacharpg.core.model.Skill

// Owner: Person 1 (Leila) — löst Card.skill1Id/skill2Id (vergeben in HeroPool, Person 2) in
// echte Namen/Beschreibungen auf. Reines Kotlin, statische Tabelle reicht fürs MVP.
//
// WICHTIG: Für jede neue Skill-ID, die Person 2 in HeroPool vergibt, hier einen Eintrag ergänzen.
// Bis dahin zeigt CardDetailScreen automatisch einen Platzhalter statt abzustürzen.
object SkillCatalog {

    private val skills: Map<Int, Skill> = listOf(
        // Feuer (Firecat-Roster)
        Skill(101, "Kratzhieb", "Einfacher Nahkampfangriff auf das aktuelle Ziel."),
        Skill(102, "Funkenwut", "Passiv: erhöht ATK leicht, wenn die eigene HP unter 50% fällt."),
        Skill(103, "Flammenspitze", "Fernangriff mit kleiner Chance auf zusätzlichen Feuerschaden über Zeit."),
        Skill(104, "Aschewall", "Passiv: reduziert erlittenen Schaden leicht, solange Feuer-Element aktiv ist."),
        Skill(105, "Präzisionsschuss", "Gezielter Angriff mit erhöhter Trefferchance auf hinten stehende Ziele."),
        Skill(106, "Wachsame Pfote", "Passiv: erhöht SPD leicht zu Rundenbeginn."),
        Skill(107, "Infernoschlag", "Starker Flächenangriff mit Bonusschaden bei vollem Ladungsstand."),
        Skill(108, "Königsmähne", "Passiv: erhöht maximale HP und Verteidigung dauerhaft."),

        // Natur (Nature-Roster)
        Skill(109, "Wurzelgriff", "Verlangsamt das Ziel kurzzeitig und bindet es an den Platz."),
        Skill(110, "Dickes Fell", "Passiv: erhöht DEF leicht, solange die eigene HP über 75% liegt."),
        Skill(111, "Heilranke", "Heilt die eigene Einheit mit der niedrigsten HP."),
        Skill(112, "Blütenschild", "Passiv: reduziert erlittenen Schaden fürs gesamte Team leicht."),
        Skill(113, "Waldwall", "Erhöht die eigene DEF für mehrere Runden deutlich."),
        Skill(114, "Regeneration", "Passiv: heilt am Rundenende einen kleinen Anteil der eigenen HP."),
        Skill(115, "Blätterwirbel", "Flächenschaden auf die gesamte gegnerische Formation."),
        Skill(116, "Ewiger Wald", "Passiv: heilt das ganze Team leicht am Rundenbeginn."),

        // Wasser (Water-Roster)
        Skill(117, "Wellenschlag", "Fernangriff mit Chance, das Ziel kurz zu verlangsamen."),
        Skill(118, "Kühle Strömung", "Passiv: erhöht SPD leicht, solange die eigene HP über 50% liegt."),
        Skill(119, "Flutklinge", "Starker Nahkampfangriff mit erhöhtem Schaden gegen Feuer-Ziele."),
        Skill(120, "Gezeitenwall", "Passiv: reduziert erlittenen Schaden bei niedriger eigener HP."),
        Skill(121, "Sturzregen", "Mehrfachtreffer auf zufällige gegnerische Ziele."),
        Skill(122, "Klare Sicht", "Passiv: erhöht die eigene Trefferchance dauerhaft."),
        Skill(123, "Tsunamischlag", "Starker Flächenangriff, der gegnerische DEF kurzzeitig senkt."),
        Skill(124, "Strömungsherrin", "Passiv: erhöht SPD des gesamten Teams leicht."),

        // Arkan (Mage-Roster)
        Skill(125, "Arkanblitz", "Einzelziel-Fernangriff mit erhöhtem Basisschaden."),
        Skill(126, "Mana-Schild", "Passiv: absorbiert einmalig einen kleinen Teil eingehenden Schadens."),
        Skill(127, "Runenfessel", "Reduziert kurzzeitig die ATK des Ziels."),
        Skill(128, "Arkane Weisheit", "Passiv: erhöht ATK leicht, solange die Ladung nicht voll ist."),
        Skill(129, "Sternenregen", "Flächenschaden mit Bonus gegen andere Arkan-Ziele (Arkan vs. Arkan)."),
        Skill(130, "Leerenblick", "Passiv: erhöht die kritische Trefferchance dauerhaft."),
        Skill(131, "Kosmische Detonation", "Sehr starker Flächenangriff bei vollem Ladungsstand."),
        Skill(132, "Arkanmonarchie", "Passiv: erhöht ATK des gesamten Teams leicht.")
    ).associateBy { it.id }

    /** Liefert den Skill für [skillId], oder einen Platzhalter, falls noch keiner hinterlegt ist. */
    fun get(skillId: Int): Skill =
        skills[skillId] ?: Skill(
            id = skillId,
            name = "Unbekannte Fähigkeit",
            description = "Noch keine Beschreibung hinterlegt (#$skillId)."
        )
}