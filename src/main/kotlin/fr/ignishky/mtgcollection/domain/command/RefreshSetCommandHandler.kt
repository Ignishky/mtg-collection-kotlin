package fr.ignishky.mtgcollection.domain.command

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class RefreshSetCommandHandler : CommandHandler {

    override fun handle(command: Command): List<Event<*, *, *>> {
        return listOf()
    }

    override fun listenTo(): KClass<out Command> {
        return RefreshSetCommand::class
    }
}
