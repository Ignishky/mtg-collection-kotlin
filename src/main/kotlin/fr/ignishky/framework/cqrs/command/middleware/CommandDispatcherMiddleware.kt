package fr.ignishky.framework.cqrs.command.middleware

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.set.command.RefreshSet
import fr.ignishky.mtgcollection.domain.set.command.RefreshSet.RefreshSetHandler

class CommandDispatcherMiddleware(
    next: CommandMiddleware,
    handlers: List<CommandHandler<*>>
) : CommandMiddleware(next) {

    private val handlersByCommand = handlers.associateBy { it.listenTo() }

    override fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>> {
        when (command) {
            is RefreshSet -> return (handlersByCommand[command::class] as RefreshSetHandler).handle(command, correlationId)
            else -> throw IllegalArgumentException("command handler not found for ${command::class}")
        }
    }

    class Builder(
        private val handlers: List<CommandHandler<*>>
    ) : CommandMiddlewareBuilder {

        override fun chain(next: CommandMiddleware): CommandDispatcherMiddleware {
            return CommandDispatcherMiddleware(next, handlers)
        }

    }

}
