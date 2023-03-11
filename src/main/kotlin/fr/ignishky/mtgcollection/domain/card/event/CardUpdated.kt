package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated.CardUpdatedPayload
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardImage
import fr.ignishky.mtgcollection.domain.card.model.CardName
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import jakarta.inject.Named
import mu.KotlinLogging
import java.time.Clock
import kotlin.reflect.KClass

class CardUpdated(aggregateId: CardId, name: CardName, images: List<CardImage>, clock: Clock) :
    Event<CardId, Card, CardUpdatedPayload>(
        0,
        aggregateId,
        Card::class,
        CardUpdatedPayload(name.value, images.map { it.value }),
        clock.instant()
    ) {

    override fun apply(aggregate: Card): Card {
        return Card(aggregateId, CardName(payload.name), aggregate.setCode, payload.images.map { CardImage(it) })
    }

    data class CardUpdatedPayload(
        val name: String,
        val images: List<String>
    ) : Payload

    @Named
    class CardUpdatedHandler(private val cardStorePort: CardStorePort) : EventHandler<CardUpdated> {

        private val logger = KotlinLogging.logger {}

        override fun handle(event: CardUpdated) {
            logger.info { "Updating card '${event.aggregateId}'..." }
            cardStorePort.store(event.apply(cardStorePort.get(event.aggregateId)))
        }

        override fun listenTo(): KClass<CardUpdated> {
            return CardUpdated::class
        }

    }
}
