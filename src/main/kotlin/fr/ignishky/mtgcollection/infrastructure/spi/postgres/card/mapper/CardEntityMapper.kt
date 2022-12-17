package fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.mapper

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.model.CardEntity

object CardEntityMapper {

    fun toCardEntity(card: Card): CardEntity {
        return CardEntity(
            card.id.value,
            card.name.value
        )
    }

}
