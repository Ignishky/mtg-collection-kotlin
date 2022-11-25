package fr.ignishky.framework.cqrs.command.middleware

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.event.Event

abstract class CommandMiddleware internal constructor(private val next: CommandMiddleware?) {

    abstract fun handle(command: Command): List<Event<*, *, *>>

    protected fun next(command: Command): List<Event<*, *, *>> {
        return next!!.handle(command)
    }

}