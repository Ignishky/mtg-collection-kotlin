package fr.ignishky.framework.cqrs.command

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId

interface CommandHandler {

    fun handle(command: Command, correlationId: CorrelationId): List<Event<*, *, *>>

}
