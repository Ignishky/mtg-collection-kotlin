package fr.ignishky.mtgcollection.domain.set.event

import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.cqrs.event.EventHandler
import fr.ignishky.framework.cqrs.event.Payload
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated.SetUpdatedPayload
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.model.SetId
import fr.ignishky.mtgcollection.domain.set.model.SetName
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import jakarta.inject.Named
import java.time.Clock
import kotlin.reflect.KClass

class SetUpdated(aggregateId: SetId, code: SetCode, name: SetName, clock: Clock) :
    Event<SetId, Set, SetUpdatedPayload>(0, aggregateId, Set::class, SetUpdatedPayload(code.value, name.value), clock.instant()) {

    override fun apply(aggregate: Set): Set {
        return Set(aggregate.id, SetCode(payload.code), SetName(payload.name))
    }

    data class SetUpdatedPayload(
        val code: String,
        val name: String
    ) : Payload

    @Named
    class SetUpdatedHandler(private val setStorePort: SetStorePort) : EventHandler<SetUpdated> {

        override fun handle(event: SetUpdated) {
            val existingSet = setStorePort.get(event.aggregateId)
            setStorePort.store(event.apply(existingSet))
        }

        override fun listenTo(): KClass<SetUpdated> {
            return SetUpdated::class
        }

    }

}
