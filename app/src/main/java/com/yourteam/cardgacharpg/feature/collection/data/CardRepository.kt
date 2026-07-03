package com.yourteam.cardgacharpg.feature.collection.data

import com.yourteam.cardgacharpg.core.model.Card
import com.yourteam.cardgacharpg.core.model.Element
import com.yourteam.cardgacharpg.core.model.Rarity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// ⚠ PUBLIC CONTRACT — Owner: Person 1 (Leila). Consumed by Person 3 & Person 5
//
// Schnittstellenvertrag (ab Woche 2 stabil zu halten, siehe Projektplan Abschnitt 5):
// - CardRepository.getAll(): Flow<List<Card>>  -> gibt alle Karten inkl. Level und Stats zurück
// - CardRepository.insertAll(cards)             -> Bulk-Insert nach Gacha-Pull (Person 2)


class CardRepository @Inject constructor(
    private val cardDao: CardDao
) {

    fun getAll(): Flow<List<Card>> =
        cardDao.getAll().map { entities -> entities.map { it.toDomain() } }

    fun getByElement(element: Element): Flow<List<Card>> =
        cardDao.getByElement(element).map { entities -> entities.map { it.toDomain() } }

    fun getByRarity(rarity: Rarity): Flow<List<Card>> =
        cardDao.getByRarity(rarity).map { entities -> entities.map { it.toDomain() } }

    fun getById(id: Int): Flow<Card?> =
        cardDao.getById(id).map { it?.toDomain() }

    suspend fun update(card: Card) {
        cardDao.update(card.toEntity())
    }

    suspend fun insert(card: Card): Long =
        cardDao.insert(card.toEntity())

    // Wird von Person 2 (GachaViewModel) nach jedem Pull aufgerufen
    suspend fun insertAll(cards: List<Card>): List<Long> =
        cardDao.insertAll(cards.map { it.toEntity() })
}

// --- Mapper: Entity (Persistenz) <-> Domain-Modell (App-weit geteilt) ---

private fun CardEntity.toDomain(): Card = Card(
    id = id,
    heroId = heroId,
    name = name,
    rarity = rarity,
    element = element,
    role = role,
    level = level,
    xp = xp,
    baseHp = baseHp,
    baseAtk = baseAtk,
    baseDef = baseDef,
    baseSpd = baseSpd,
    currentHp = currentHp,
    currentAtk = currentAtk,
    currentDef = currentDef,
    currentSpd = currentSpd,
    skill1Id = skill1Id,
    skill2Id = skill2Id,
    imageAssetName = imageAssetName
)

private fun Card.toEntity(): CardEntity = CardEntity(
    id = id,
    heroId = heroId,
    name = name,
    rarity = rarity,
    element = element,
    role = role,
    level = level,
    xp = xp,
    baseHp = baseHp,
    baseAtk = baseAtk,
    baseDef = baseDef,
    baseSpd = baseSpd,
    currentHp = currentHp,
    currentAtk = currentAtk,
    currentDef = currentDef,
    currentSpd = currentSpd,
    skill1Id = skill1Id,
    skill2Id = skill2Id,
    imageAssetName = imageAssetName
)