package fr.ignishky.framework.domain

data class CorrelationId(val value: String) {

    override fun toString(): String {
        return value
    }
}
