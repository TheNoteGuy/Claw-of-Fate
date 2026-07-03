package com.yourteam.cardgacharpg.core.database

import androidx.room.TypeConverter
import com.yourteam.cardgacharpg.core.model.Element
import com.yourteam.cardgacharpg.core.model.Rarity
import com.yourteam.cardgacharpg.core.model.Role
import com.yourteam.cardgacharpg.feature.collection.data.ItemType

// Room TypeConverters
// Speichert alle Enums als ihren Namen (String) in der DB.
class Converters {

    @TypeConverter
    fun fromRarity(value: Rarity): String = value.name

    @TypeConverter
    fun toRarity(value: String): Rarity = Rarity.valueOf(value)

    @TypeConverter
    fun fromElement(value: Element): String = value.name

    @TypeConverter
    fun toElement(value: String): Element = Element.valueOf(value)

    @TypeConverter
    fun fromRole(value: Role): String = value.name

    @TypeConverter
    fun toRole(value: String): Role = Role.valueOf(value)

    @TypeConverter
    fun fromItemType(value: ItemType): String = value.name

    @TypeConverter
    fun toItemType(value: String): ItemType = ItemType.valueOf(value)
}