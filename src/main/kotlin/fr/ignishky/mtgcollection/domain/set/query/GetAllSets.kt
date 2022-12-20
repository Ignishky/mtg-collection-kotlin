package fr.ignishky.mtgcollection.domain.set.query

import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.port.SetApiPort
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import jakarta.inject.Named

@Named
class GetAllSets(private val setStorePort: SetStorePort) : SetApiPort {

    override fun getAll(): List<Set> {
        return setStorePort.getAll()
    }

}
