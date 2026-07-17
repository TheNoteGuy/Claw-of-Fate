package com.yourteam.cardgacharpg.feature.collection.domain

import com.yourteam.cardgacharpg.core.model.Skill

// Owner: Person 1 (Leila) — löst Card.skill1Id/skill2Id (vergeben in HeroPool, Person 2) in
// echte Namen/Beschreibungen auf. Reines Kotlin, statische Tabelle reicht fürs MVP.
//
// WICHTIG: Für jede neue Skill-ID, die Person 2 in HeroPool vergibt, hier einen Eintrag
// ergänzen. Bis dahin zeigt CardDetailScreen automatisch einen Platzhalter statt abzustürzen.
object SkillCatalog {

    private val skills: Map<Int, Skill> = listOf(
        Skill(101, "Kratzhieb", "Einfacher Nahkampfangriff auf das aktuelle Ziel."),
        Skill(102, "Funkenwut", "Passiv: erhöht ATK leicht, wenn die eigene HP unter 50% fällt."),
        Skill(103, "Flammenspitze", "Fernangriff mit kleiner Chance auf zusätzlichen Schaden über Zeit."),
        Skill(104, "Aschewall", "Passiv: reduziert erlittenen Schaden leicht bei aktivem Feuer-Element."),
        Skill(105, "Präzisionsschuss", "Gezielter Angriff mit erhöhter Trefferchance auf hinten stehende Ziele."),
        Skill(106, "Wachsame Pfote", "Passiv: erhöht SPD leicht zu Rundenbeginn."),
        Skill(107, "Infernoschlag", "Starker Flächenangriff mit Bonusschaden bei vollem Ladungsstand."),
        Skill(108, "Königsmähne", "Passiv: erhöht maximale HP und Verteidigung dauerhaft.")
    ).associateBy { it.id }

    /** Liefert den Skill für [skillId], oder einen Platzhalter, falls noch keiner hinterlegt ist. */
    fun get(skillId: Int): Skill =
        skills[skillId] ?: Skill(
            id = skillId,
            name = "Unbekannte Fähigkeit",
            description = "Noch keine Beschreibung hinterlegt (#$skillId)."
        )
}