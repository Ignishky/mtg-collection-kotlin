package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.framework.domain.AggregateId

data class SetId(
    internal val value: String
) : AggregateId {

    override fun value(): String {
        return value
    }

}
