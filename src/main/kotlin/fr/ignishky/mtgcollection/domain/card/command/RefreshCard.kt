package fr.ignishky.mtgcollection.domain.card.command

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.event.CardCreated
import fr.ignishky.mtgcollection.domain.card.port.CardRefererPort
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
        private val clock: Clock
    ) : CommandHandler<RefreshCard> {

        private val logger = logger {}

        override fun handle(command: RefreshCard, correlationId: CorrelationId): List<Event<*, *, *>> {
            return setStorePort.getAll()
                .flatMap {
                    logger.info { "Refreshing cards from ${it.code.value}" }
                    cardRefererPort.getCards(it.code)
                }
                .map { CardCreated(it.id, it.name, clock) }
        }

        override fun listenTo(): KClass<out RefreshCard> {
            return RefreshCard::class
        }

    }
}
