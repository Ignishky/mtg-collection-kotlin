package fr.ignishky.framework.cqrs.command.middleware

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.event.CardCreated
import fr.ignishky.mtgcollection.domain.card.event.CardCreated.CardCreatedHandler
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated
import fr.ignishky.mtgcollection.domain.card.event.CardUpdated.CardUpdatedHandler
import fr.ignishky.mtgcollection.domain.set.event.SetCreated
import fr.ignishky.mtgcollection.domain.set.event.SetCreated.SetCreatedHandler
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated.SetUpdatedHandler

class EventDispatcherMiddleware(
    next: CommandMiddleware,
    handlers: List<EventHandler<out Event<*, *, *>>>
) : CommandMiddleware(next) {

    private val handlersByEvent = handlers.associateBy { it.listenTo() }

    override fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>> {
        val events = next(command, correlationId)

        events.parallelStream().forEach {
            when (it) {
                is SetCreated -> (handlersByEvent[it::class] as SetCreatedHandler).handle(it)
                is SetUpdated -> (handlersByEvent[it::class] as SetUpdatedHandler).handle(it)
                is CardCreated -> (handlersByEvent[it::class] as CardCreatedHandler).handle(it)
                is CardUpdated -> (handlersByEvent[it::class] as CardUpdatedHandler).handle(it)
                else -> throw IllegalArgumentException("event handler not found for ${it::class}")
            }
        }

        return events
    }

    class Builder(
        private val handlers: List<EventHandler<out Event<*, *, out Payload>>>
    ) : CommandMiddlewareBuilder {

        override fun chain(next: CommandMiddleware): EventDispatcherMiddleware {
            return EventDispatcherMiddleware(next, handlers)
        }

    }

}
