package fr.ignishky.framework.cqrs.command.middleware

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.set.command.RefreshSet
import fr.ignishky.mtgcollection.domain.set.command.RefreshSet.RefreshSetHandler

class CommandDispatcherMiddleware(
    next: CommandMiddleware,
    refreshSetHandler: RefreshSetHandler
) : CommandMiddleware(next) {

    private val handlersByCommand = mapOf(
        Pair(RefreshSet::class, refreshSetHandler)
    )

    override fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>> {
        when(command) {
            is RefreshSet -> return handlersByCommand[RefreshSet::class]!!.handle(command, correlationId)
            else -> throw IllegalArgumentException("command handler not found for " + command.javaClass)
        }
    }

    class Builder(private val refreshSetHandler: RefreshSetHandler) : CommandMiddlewareBuilder {

        override fun chain(next: CommandMiddleware): CommandDispatcherMiddleware {
            return CommandDispatcherMiddleware(next, refreshSetHandler)
        }

    }

}
