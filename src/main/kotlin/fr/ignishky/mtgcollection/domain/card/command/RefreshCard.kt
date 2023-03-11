package fr.ignishky.mtgcollection.domain.card.command

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.event.CardCreated
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated
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
                .map { set ->
                    logger.info { "Refreshing cards from ${set.code.value}" }
                    Pair(cardRefererPort.getCards(set.code), cardStorePort.get(set.code).associateBy { it.id })
                }
                .flatMap { (refererCards, knownCards) ->
                    refererCards.mapNotNull { (id, name, setCode, images) ->
                        if (!knownCards.contains(id)) {
                            CardCreated(id, name, setCode, images, clock)
                        } else if (knownCards[id]!!.name != name || !knownCards[id]!!.images.containsAll(images)) {
                            CardUpdated(id, name, images, clock)
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
