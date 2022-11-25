package fr.ignishky.framework.cqrs.command

import fr.ignishky.framework.cqrs.event.Event
import kotlin.reflect.KClass

interface CommandHandler {

    fun handle(command: Command): List<Event<*, *, *>>

    fun listenTo(): KClass<out Command>

}