package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.set.event.SetCreated.SetCreatedPayload
import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import jakarta.inject.Named
import mu.KotlinLogging.logger
import java.time.Clock
import kotlin.reflect.KClass

class SetCreated(aggregateId: SetId, code: SetCode, name: SetName, icon: SetIcon, clock: Clock) :
    Event<SetId, Set, SetCreatedPayload>(0, aggregateId, Set::class, SetCreatedPayload(code.value, name.value, icon.value), clock.instant()) {

    override fun apply(aggregate: Set): Set {
        return Set(aggregateId, SetCode(payload.code), SetName(payload.name), SetIcon(payload.icon))
    }

    data class SetCreatedPayload(
        val code: String,
        val name: String,
        val icon: String
    ) : Payload

    @Named
    class SetCreatedHandler(private val setStorePort: SetStorePort) : EventHandler<SetCreated> {

        private val logger = logger {}

        override fun handle(event: SetCreated) {
            logger.info { "Creating set '${event.payload.name}'..." }
            setStorePort.store(event.apply(Set()))
        }

        override fun listenTo(): KClass<SetCreated> {
            return SetCreated::class
        }

    }

}
