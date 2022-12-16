package fr.ignishky.mtgcollection.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.framework.cqrs.command.CommandBus
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.command.DirectCommandBus
import fr.ignishky.framework.cqrs.command.middleware.CommandDispatcherMiddleware
import fr.ignishky.framework.cqrs.command.middleware.EventDispatcherMiddleware
import fr.ignishky.framework.cqrs.command.middleware.EventPersistenceMiddleware
import fr.ignishky.framework.cqrs.command.middleware.LoggingCommandBusMiddleware
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.spi.postgres.EventRepository
import fr.ignishky.framework.domain.CorrelationIdGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FrameworkConfiguration {

    @Bean
    fun correlationIdGenerator(): CorrelationIdGenerator {
        return CorrelationIdGenerator()
    }

    @Bean
    fun commandBus(
        objectMapper: ObjectMapper,
        eventRepository: EventRepository,
        eventHandlers: List<EventHandler<out Event<*, *, *>>>,
        commandHandlers: List<CommandHandler<*>>
    ): CommandBus {
        return DirectCommandBus(
            setOf(
                LoggingCommandBusMiddleware.Builder(),
                EventPersistenceMiddleware.Builder(objectMapper, eventRepository),
                EventDispatcherMiddleware.Builder(eventHandlers),
                CommandDispatcherMiddleware.Builder(commandHandlers)
            )
        )
    }

}
