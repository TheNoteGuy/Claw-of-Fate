package com.yourteam.cardgacharpg.feature.collection.ui

import androidx.annotation.DrawableRes
import com.yourteam.cardgacharpg.R

// Owner: Person 1 (Leila)
// Mapping von Card.imageAssetName (String, siehe Card.kt) auf echte Drawable-Ressourcen.
//
// Bewusst eine explizite Tabelle statt resources.getIdentifier() (Reflection):
// - Compile-Time-sicher: Tippfehler im Namen fallen sofort beim Bauen auf
// - kein Reflection-Overhead beim Rendern von Grids mit vielen Karten
//
// WICHTIG: Für jeden neuen imageAssetName-Wert (Person 2 vergibt ihn beim Pull, siehe
// HeroPool.kt) muss hier ein Eintrag ergänzt werden, SOBALD die passende PNG unter
// res/drawable/ liegt. Bis dahin greift automatisch der Text-Platzhalter in CardImage.kt.

object CardImageProvider {

    private val assetMap: Map<String, Int> = mapOf(
        //Typ: FIRE
        "hero_firecat_common" to R.drawable.hero_firecat_common,
        "hero_firecat_rare" to R.drawable.hero_firecat_rare,
        "hero_firecat_epic" to R.drawable.hero_firecat_epic,
        "hero_firecat_legendary" to R.drawable.hero_firecat_legendary,
        //Typ: MAGE
        "hero_mage_common" to R.drawable.hero_mage_common,
        "hero_mage_rare" to R.drawable.hero_mage_rare,
        "hero_mage_epic" to R.drawable.hero_mage_epic,
        "hero_mage_legendary" to R.drawable.hero_mage_legendary,
        //Typ: NATURE
        "hero_nature_common" to R.drawable.hero_nature_common,
        "hero_nature_rare" to R.drawable.hero_nature_rare,
        "hero_nature_epic" to R.drawable.hero_nature_epic,
        "hero_nature_legendary" to R.drawable.hero_nature_legendary,
        //Typ: WATER
        "hero_water_common" to R.drawable.hero_water_common,
        "hero_water_rare" to R.drawable.hero_water_rare,
        "hero_water_epic" to R.drawable.hero_water_epic,
        "hero_water_legendary" to R.drawable.hero_water_legendary,


        )

    /** Liefert die Drawable-Resource-ID für [assetName], oder null wenn (noch) kein Asset existiert. */
    @DrawableRes
    fun resFor(assetName: String): Int? = assetMap[assetName]
}