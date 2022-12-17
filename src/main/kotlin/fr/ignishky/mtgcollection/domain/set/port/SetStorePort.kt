package fr.ignishky.mtgcollection.domain.set.port

import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetId

interface SetStorePort {

    fun store(set: Set)

    fun getAll(): List<Set>

    fun get(id: SetId): Set

}
