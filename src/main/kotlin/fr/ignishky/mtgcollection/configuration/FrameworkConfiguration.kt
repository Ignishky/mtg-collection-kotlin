package fr.ignishky.mtgcollection.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.framework.cqrs.command.CommandBus
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.command.DirectCommandBus
import fr.ignishky.framework.cqrs.command.middleware.CommandDispatcherMiddleware
import fr.ignishky.framework.cqrs.command.middleware.EventPersistenceMiddleware
import fr.ignishky.framework.cqrs.command.middleware.LoggingCommandBusMiddleware
import fr.ignishky.framework.domain.CorrelationIdGenerator
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.EventRepository
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
        commandHandlers: List<CommandHandler>,
        eventRepository: EventRepository,
        objectMapper: ObjectMapper
    ): CommandBus {
        return DirectCommandBus(
            setOf(
                LoggingCommandBusMiddleware.Builder(),
                EventPersistenceMiddleware.Builder(objectMapper, eventRepository),
                CommandDispatcherMiddleware.Builder(commandHandlers)
            )
        )
    }
}
