package fr.ignishky.framework.cqrs.event

interface EventStore {
    fun saveAll(events: List<Event<*, *, *>>)
}
