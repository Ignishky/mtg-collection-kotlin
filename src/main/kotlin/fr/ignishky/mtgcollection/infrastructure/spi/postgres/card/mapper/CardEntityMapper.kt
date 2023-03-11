package fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.mapper

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardImage
import fr.ignishky.mtgcollection.domain.card.model.CardName
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.model.CardEntity

object CardEntityMapper {

    fun toCardEntity(card: Card): CardEntity {
        return CardEntity(
            card.id.value,
            card.name.value,
            card.setCode.value,
            card.images.joinToString { it.value }
        )
    }

    fun toCard(entity: CardEntity): Card {
        return Card(
            CardId(entity.id),
            CardName(entity.name),
            SetCode(entity.setCode),
            entity.images.split(", ").map { CardImage(it) }
        )
    }

}
