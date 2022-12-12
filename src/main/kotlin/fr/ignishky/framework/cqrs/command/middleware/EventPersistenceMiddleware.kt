package fr.ignishky.framework.cqrs.command.middleware

import com.fasterxml.jackson.databind.ObjectMapper
import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.EventEntity
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.EventRepository

class EventPersistenceMiddleware(
    next: CommandMiddleware,
    private val objectMapper: ObjectMapper,
    private val eventRepository: EventRepository
) : CommandMiddleware(next) {

    override fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>> {
        val events = next(command, correlationId)
        eventRepository.saveAll(events.map {
            EventEntity(
                0,
                it.aggregateId.toString(),
                it.aggregateClass.simpleName.toString(),
                it::class.simpleName.toString(),
                it.instant,
                objectMapper.writeValueAsString(it.payload),
                correlationId.toString()
            )
        })
        return events
    }

    class Builder(private val objectMapper: ObjectMapper, private val eventRepository: EventRepository) : CommandMiddlewareBuilder {

        override fun chain(next: CommandMiddleware): CommandMiddleware {
            return EventPersistenceMiddleware(next, objectMapper, eventRepository)
        }
    }

}
