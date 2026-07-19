package com.yourteam.cardgacharpg.feature.campaign.domain

import com.yourteam.cardgacharpg.core.model.BattleFormationInput
import com.yourteam.cardgacharpg.core.model.BattleParticipant
import com.yourteam.cardgacharpg.core.model.Element
import com.yourteam.cardgacharpg.core.model.Position
import com.yourteam.cardgacharpg.core.model.Role
import kotlin.math.roundToInt

// Owner: Person 4 (Yassin) — Gegner-Definitionen fuer Akt 1 "Der Dunkle Wald".
//
// Bewusst KEIN Card-Entity (Projektplan: "EnemyFormation ... KEIN Card-Entity, eigene
// Struktur"): EnemyFormationEntity referenziert nur Katalog-IDs, dieser Katalog loest sie in
// fertige BattleParticipants fuer den BattleSimulator (Person 3) auf.
//
// Akt 1 laut GDD-Gegnerskalierung: Common/Rare-Gegner, alle 5 Rollen und 3 Elemente
// (Feuer/Natur/Wasser), Team-Level-Bereich 1–15 -> Stats skalieren pro Level leicht hoch.
object EnemyCatalog {

    // Pro Level +6% auf alle Stats — Level 10 (Boss) liegt damit ~54% ueber Level 1.
    private const val STAT_GROWTH_PER_LEVEL = 0.06

    private data class EnemyTemplate(
        val id: Int,
        val name: String,
        val element: Element,
        val role: Role,
        val baseHp: Int,
        val baseAtk: Int,
        val baseDef: Int,
        val baseSpd: Int
    )

    // IDs werden von EnemyFormationProvider referenziert. 1-4 = Common, 5-8 = Rare.
    private val templates: Map<Int, EnemyTemplate> = listOf(
        EnemyTemplate(1, "Aschewolf", Element.FEUER, Role.KRIEGER, baseHp = 560, baseAtk = 88, baseDef = 34, baseSpd = 52),
        EnemyTemplate(2, "Flusskröte", Element.WASSER, Role.TANK, baseHp = 680, baseAtk = 62, baseDef = 50, baseSpd = 40),
        EnemyTemplate(3, "Dornenluchs", Element.NATUR, Role.RANGER, baseHp = 520, baseAtk = 92, baseDef = 28, baseSpd = 60),
        EnemyTemplate(4, "Nebelirrlicht", Element.WASSER, Role.MAGIER, baseHp = 500, baseAtk = 98, baseDef = 26, baseSpd = 48),
        EnemyTemplate(5, "Borkenbär", Element.NATUR, Role.TANK, baseHp = 820, baseAtk = 78, baseDef = 62, baseSpd = 44),
        EnemyTemplate(6, "Glutschamane", Element.FEUER, Role.SUPPORT, baseHp = 640, baseAtk = 84, baseDef = 42, baseSpd = 56),
        EnemyTemplate(7, "Sturmkrähe", Element.WASSER, Role.RANGER, baseHp = 600, baseAtk = 108, baseDef = 34, baseSpd = 70),
        EnemyTemplate(8, "Waldwächter", Element.NATUR, Role.KRIEGER, baseHp = 760, baseAtk = 112, baseDef = 48, baseSpd = 58)
    ).associateBy { it.id }

    /**
     * Loest eine EnemyFormationEntity (Person 4, Katalog-IDs je Slot) in ein
     * [BattleFormationInput] fuer BattleSimulator.simulate(...) auf.
     *
     * Die Teilnehmer-IDs werden pro Slot eindeutig vergeben (BattleSimulator verlangt
     * eindeutige cardIds je Formation — dieselbe Katalog-ID darf aber mehrfach im Grid
     * stehen, z.B. zwei Aschewölfe in Level 1). Negative IDs < -1000 kollidieren weder mit
     * echten Spielerkarten (positive Room-IDs) noch mit dem AiDeckPool (-1..-60).
     */
    fun toBattleFormation(
        formation: com.yourteam.cardgacharpg.feature.campaign.data.EnemyFormationEntity
    ): BattleFormationInput {
        val levelFactor = 1.0 + (formation.levelId - 1) * STAT_GROWTH_PER_LEVEL

        fun participant(templateId: Int?, slotIndex: Int, position: Position): BattleParticipant? {
            val t = templates[templateId ?: return null]
                ?: error("Unbekannte Gegner-ID $templateId in Level ${formation.levelId} (EnemyCatalog).")
            fun scaled(stat: Int) = (stat * levelFactor).roundToInt().coerceAtLeast(1)
            return BattleParticipant(
                cardId = -(1000 + formation.levelId * 10 + slotIndex),
                name = t.name,
                element = t.element,
                role = t.role,
                maxHp = scaled(t.baseHp),
                atk = scaled(t.baseAtk),
                def = scaled(t.baseDef),
                spd = scaled(t.baseSpd),
                position = position
            )
        }

        return BattleFormationInput(
            front = listOfNotNull(
                participant(formation.frontLeftCardId, 0, Position.FRONT),
                participant(formation.frontCenterCardId, 1, Position.FRONT),
                participant(formation.frontRightCardId, 2, Position.FRONT)
            ),
            back = listOfNotNull(
                participant(formation.backLeftCardId, 3, Position.BACK),
                participant(formation.backCenterCardId, 4, Position.BACK),
                participant(formation.backRightCardId, 5, Position.BACK)
            )
        )
    }
}
