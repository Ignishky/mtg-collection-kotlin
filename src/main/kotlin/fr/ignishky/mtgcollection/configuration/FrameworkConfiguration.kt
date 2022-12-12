package fr.ignishky.mtgcollection.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.framework.cqrs.command.CommandBus
import fr.ignishky.framework.cqrs.command.DirectCommandBus
import fr.ignishky.framework.cqrs.command.middleware.CommandDispatcherMiddleware
import fr.ignishky.framework.cqrs.command.middleware.EventPersistenceMiddleware
import fr.ignishky.framework.cqrs.command.middleware.LoggingCommandBusMiddleware
import fr.ignishky.framework.cqrs.event.spi.postgres.EventRepository
import fr.ignishky.framework.domain.CorrelationIdGenerator
import fr.ignishky.mtgcollection.domain.set.command.RefreshSet
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
        refreshSetHandler: RefreshSet.RefreshSetHandler
    ): CommandBus {
        return DirectCommandBus(
            setOf(
                LoggingCommandBusMiddleware.Builder(),
                EventPersistenceMiddleware.Builder(objectMapper, eventRepository),
                CommandDispatcherMiddleware.Builder(refreshSetHandler)
            )
        )
    }
}
