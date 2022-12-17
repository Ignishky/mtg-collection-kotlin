package fr.ignishky.mtgcollection.infrastructure.spi.postgres.card

import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.mapper.CardEntityMapper.toCardEntity
import jakarta.inject.Named

@Named
class CardPostgresStore(private val cardRepository: CardRepository) : CardStorePort {

    override fun store(card: Card) {
        cardRepository.save(toCardEntity(card))
    }

}
