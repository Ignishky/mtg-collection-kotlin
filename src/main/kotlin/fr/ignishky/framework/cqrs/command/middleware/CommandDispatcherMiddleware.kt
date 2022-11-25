package fr.ignishky.framework.cqrs.command.middleware

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event

class CommandDispatcherMiddleware(
    next: CommandMiddleware,
    handlers: List<CommandHandler>
) : CommandMiddleware(next) {

    private val handlersByCommand = handlers.associateBy(CommandHandler::listenTo) { it }

    override fun handle(command: Command): List<Event<*, *, *>> {
        if (handlersByCommand.containsKey(command::class)) {
            return handlersByCommand.getValue(command::class).handle(command)
        } else {
            throw IllegalArgumentException("command handler not found for " + command.javaClass)
        }
    }

    class Builder(private val handlers: List<CommandHandler>) : CommandMiddlewareBuilder {
        override fun chain(next: CommandMiddleware): CommandDispatcherMiddleware {
            return CommandDispatcherMiddleware(next, handlers)
        }
    }
}