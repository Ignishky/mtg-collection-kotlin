package fr.ignishky.mtgcollection.domain.set.command

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.spi.postgres.EventEntity
import fr.ignishky.framework.cqrs.event.spi.postgres.EventRepository
import fr.ignishky.framework.domain.CorrelationId
import jakarta.inject.Named
import java.time.Clock
import kotlin.reflect.KClass

class RefreshSet : Command {

    @Named
    class RefreshSetHandler(
        private val clock: Clock,
        private val eventRepository: EventRepository
    ) : CommandHandler<RefreshSet> {

        override fun handle(command: RefreshSet, correlationId: CorrelationId): List<Event<*, *, *>> {
            eventRepository.save(EventEntity(0, "AggregateId1", "AggregateName", "AggregateLabel", clock.instant(), "", correlationId.toString()))
            eventRepository.save(EventEntity(0, "AggregateId2", "AggregateName", "AggregateLabel", clock.instant(), "", correlationId.toString()))
            val events = eventRepository.findAll()
            println(events)
            return listOf()
        }

        override fun listenTo(): KClass<out RefreshSet> {
            return RefreshSet::class
        }

    }

}