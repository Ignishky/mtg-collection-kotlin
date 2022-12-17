package fr.ignishky.mtgcollection.domain.card.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.card.event.CardCreated.CardCreatedPayload
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardName
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import jakarta.inject.Named
import mu.KotlinLogging
import java.time.Clock
import kotlin.reflect.KClass

class CardCreated(aggregateId: CardId, name: CardName, clock: Clock) :
    Event<CardId, Card, CardCreatedPayload>(0, aggregateId, Card::class, CardCreatedPayload(name.value), clock.instant()) {

    override fun apply(aggregate: Card): Card {
        return Card(aggregateId, CardName(payload.name))
    }

    data class CardCreatedPayload(val name: String) : Payload

    @Named
    class CardCreatedHandler(private val cardStorePort: CardStorePort) : EventHandler<CardCreated> {

        private val logger = KotlinLogging.logger {}

        override fun handle(event: CardCreated) {
            logger.info { "Creating card '${event.payload.name}'..." }
            cardStorePort.store(event.apply(Card()))
        }

        override fun listenTo(): KClass<CardCreated> {
            return CardCreated::class
        }

    }
}
