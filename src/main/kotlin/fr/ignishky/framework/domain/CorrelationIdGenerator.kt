package fr.ignishky.framework.domain

import java.util.UUID.randomUUID

open class CorrelationIdGenerator {

    open fun generate(): CorrelationId {
        return CorrelationId(randomUUID())
    }

}
