package fr.ignishky.mtgcollection.domain.command

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.EventDTO
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.EventRepository
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
class RefreshSetCommandHandler(
    private val eventRepository: EventRepository
) : CommandHandler {

    override fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>> {
        eventRepository.save(EventDTO("TEST", "", "", "", ""))
        eventRepository.save(EventDTO("TEST", "", "", "", ""))
        val events = eventRepository.findAll()
        println(events)
        return listOf()
    }

    override fun listenTo(): KClass<out Command> {
        return RefreshSetCommand::class
    }
}
