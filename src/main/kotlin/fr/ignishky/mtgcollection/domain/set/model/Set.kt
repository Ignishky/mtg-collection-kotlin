package fr.ignishky.mtgcollection.domain.set.model

import fr.ignishky.framework.domain.Aggregate

data class Set(
    internal val id: SetId,
    internal val code: SetCode,
    internal val name: SetName,
    internal val icon: SetIcon
) : Aggregate<SetId> {

    constructor() : this(SetId(""), SetCode(""), SetName(""), SetIcon(""))

    override fun id(): SetId {
        return id
    }

}
