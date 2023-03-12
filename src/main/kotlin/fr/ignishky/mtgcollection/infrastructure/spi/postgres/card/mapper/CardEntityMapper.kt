package fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.mapper

import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.model.CardEntity

object CardEntityMapper {

    fun toCardEntity(card: Card): CardEntity {
        return CardEntity(
            card.id.value,
            card.name.value,
            card.setCode.value,
            card.prices.scryfall.eur,
            card.prices.scryfall.eurFoil,
            card.prices.scryfall.usd,
            card.prices.scryfall.usdFoil,
            card.images.joinToString { it.value },
            card.collectionNumber.value,
        )
    }

    fun toCard(entity: CardEntity): Card {
        return Card(
            CardId(entity.id),
            CardName(entity.name),
            SetCode(entity.setCode),
            Prices(Price(entity.scryfallEur, entity.scryfallEurFoil, entity.scryfallUsd, entity.scryfallUsdFoil)),
            entity.images.split(", ").map { CardImage(it) },
            CollectionNumber(entity.collectionNumber),
        )
    }

}
