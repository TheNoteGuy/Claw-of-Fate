package com.yourteam.cardgacharpg.core.model

// Owner: Person 1 (Leila) — geteiltes Skill-Modell für die Detailansicht (FA09).
// Skill-IDs werden von Person 2 (HeroPool) vergeben (101, 102, ...);
// SkillCatalog löst sie in Name + Beschreibung auf.

data class Skill(
    val id: Int,
    val name: String,
    val description: String
)