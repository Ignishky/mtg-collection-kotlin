package fr.ignishky.mtgcollection.domain.card.command

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.event.CardCreated
import fr.ignishky.mtgcollection.domain.card.port.CardRefererPort
import fr.ignishky.mtgcollection.domain.card.port.CardStorePort
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import jakarta.inject.Named
import mu.KotlinLogging.logger
import java.time.Clock
import kotlin.reflect.KClass

class RefreshCard : Command {

    @Named
    class RefreshCardHandler(
        private val setStorePort: SetStorePort,
        private val cardRefererPort: CardRefererPort,
        private val cardStorePort: CardStorePort,
        private val clock: Clock
    ) : CommandHandler<RefreshCard> {

        private val logger = logger {}

        override fun handle(command: RefreshCard, correlationId: CorrelationId): List<Event<*, *, *>> {
            return setStorePort.getAll()
                .map {
                    logger.info { "Refreshing cards from ${it.code.value}" }
                    Pair(cardRefererPort.getCards(it.code), cardStorePort.get(it.code).map { it.id })
                }
                .flatMap { pair ->
                    pair.first.mapNotNull { card ->
                        if (!pair.second.contains(card.id)) {
                            CardCreated(card.id, card.name, card.setCode, clock)
                        } else {
                            null
                        }
                    }
                }
        }

        override fun listenTo(): KClass<out RefreshCard> {
            return RefreshCard::class
        }

    }
}
