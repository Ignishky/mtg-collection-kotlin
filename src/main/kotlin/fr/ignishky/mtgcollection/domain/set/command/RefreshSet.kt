package fr.ignishky.mtgcollection.domain.set.command

import fr.ignishky.framework.cqrs.command.Command
import fr.ignishky.framework.cqrs.command.CommandHandler
import fr.ignishky.framework.cqrs.event.Event
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.set.event.SetCreated
import fr.ignishky.mtgcollection.domain.set.event.SetUpdated
import fr.ignishky.mtgcollection.domain.set.port.SetRefererPort
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import jakarta.inject.Named
import mu.KotlinLogging.logger
import java.time.Clock
import kotlin.reflect.KClass

class RefreshSet : Command {

    override fun toString(): String {
        return "''"
    }

    @Named
    class RefreshSetHandler(
        private val clock: Clock,
        private val setRefererPort: SetRefererPort,
        private val setStorePort: SetStorePort
    ) : CommandHandler<RefreshSet> {

        private val logger = logger {}

        override fun handle(command: RefreshSet, correlationId: CorrelationId): List<Event<*, *, *>> {

            val knownSetsById = setStorePort.getAll().associateBy { it.id }
            logger.info { "Refreshing ${knownSetsById.size} sets..." }

            return setRefererPort.getAllSets()
                .mapNotNull {
                    if (knownSetsById[it.id] == null) {
                        SetCreated(it.id, it.code, it.name, clock)
                    } else if (knownSetsById[it.id] != it) {
                        SetUpdated(it.id, it.code, it.name, clock)
                    } else {
                        null
                    }
                }
        }

        override fun listenTo(): KClass<RefreshSet> {
            return RefreshSet::class
        }

    }

}
