package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated.CardUpdatedPayload
import fr.ignishky.mtgcollection.domain.card.model.*
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import jakarta.inject.Named
import mu.KotlinLogging
import java.time.Clock
import kotlin.reflect.KClass

class CardUpdated(aggregateId: CardId, name: CardName, prices: Prices, images: List<CardImage>, collectionNumber: CollectionNumber, clock: Clock) :
    Event<CardId, Card, CardUpdatedPayload>(
        0,
        aggregateId,
        Card::class,
        CardUpdatedPayload(
            name.value,
            prices.scryfall.eur,
            prices.scryfall.eurFoil,
            prices.scryfall.usd,
            prices.scryfall.usdFoil,
            images.map { it.value },
            collectionNumber.value,
        ),
        clock.instant(),
    ) {

    override fun apply(aggregate: Card): Card {
        return Card(
            aggregateId,
            CardName(payload.name),
            aggregate.setCode,
            Prices(Price(payload.scryfallEur, payload.scryfallEurFoil, payload.scryfallUsd, payload.scryfallUsdFoil)),
            payload.images.map { CardImage(it) },
            CollectionNumber(payload.collectionNumber),
        )
    }

    data class CardUpdatedPayload(
        val name: String,
        val scryfallEur: Long,
        val scryfallEurFoil: Long,
        val scryfallUsd: Long,
        val scryfallUsdFoil: Long,
        val images: List<String>,
        val collectionNumber: String,
    ) : Payload

    @Named
    class CardUpdatedHandler(private val cardStorePort: CardStorePort) : EventHandler<CardUpdated> {

        private val logger = KotlinLogging.logger {}

        override fun handle(event: CardUpdated) {
            logger.info { "Updating card '${event.payload.name}'..." }
            cardStorePort.store(event.apply(cardStorePort.get(event.aggregateId)))
        }

        override fun listenTo(): KClass<CardUpdated> {
            return CardUpdated::class
        }

    }

}
