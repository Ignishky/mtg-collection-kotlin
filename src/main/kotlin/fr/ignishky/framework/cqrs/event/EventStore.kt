package fr.ignishky.framework.cqrs.event

interface EventStore {
    fun store(events: List<Event<*, *, *>>)
}