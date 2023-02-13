package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated.SetUpdatedPayload
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import jakarta.inject.Named
import mu.KotlinLogging.logger
import java.time.Clock
import kotlin.reflect.KClass

class SetUpdated(aggregateId: SetId, code: SetCode, name: SetName, icon: SetIcon, clock: Clock) :
    Event<SetId, Set, SetUpdatedPayload>(0, aggregateId, Set::class, SetUpdatedPayload(code.value, name.value, icon.value), clock.instant()) {

    override fun apply(aggregate: Set): Set {
        return Set(aggregate.id, SetCode(payload.code), SetName(payload.name), SetIcon(payload.icon))
    }

    data class SetUpdatedPayload(
        val code: String,
        val name: String,
        val icon: String
    ) : Payload

    @Named
    class SetUpdatedHandler(private val setStorePort: SetStorePort) : EventHandler<SetUpdated> {

        private val logger = logger {}

        override fun handle(event: SetUpdated) {
            logger.info { "Updating set '${event.payload.name}'..." }
            val existingSet = setStorePort.get(event.aggregateId)
            setStorePort.store(event.apply(existingSet))
        }

        override fun listenTo(): KClass<SetUpdated> {
            return SetUpdated::class
        }

    }

}
