package fr.ignishky.framework.cqrs.command.middleware

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId
import org.slf4j.LoggerFactory

class LoggingCommandBusMiddleware(next: CommandMiddleware) : CommandMiddleware(next) {

    companion object {

        private val LOGGER = LoggerFactory.getLogger(LoggingCommandBusMiddleware::class.java)

    }

    override fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>> {
        LOGGER.info("Executing {} with parameter {}", command::class.simpleName, command)
        return try {
            val next = next(command, correlationId)
            LOGGER.info("Success on {}. Events : {}", command::class.simpleName, next)
            next
        } catch (throwable: Throwable) {
            LOGGER.error("Error on {}", command::class.simpleName, throwable)
            throw throwable
        }
    }

    class Builder : CommandMiddlewareBuilder {

        override fun chain(next: CommandMiddleware): CommandMiddleware {
            return LoggingCommandBusMiddleware(next)
        }

    }

}
