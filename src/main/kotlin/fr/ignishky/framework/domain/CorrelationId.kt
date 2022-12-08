package fr.ignishky.framework.domain

import java.util.*

data class CorrelationId(val value: UUID) {

    override fun toString(): String {
        return value.toString()
    }
}
