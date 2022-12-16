package fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.mapper

import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.model.SetId
import fr.ignishky.mtgcollection.domain.set.model.SetName
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model.SetEntity

object SetEntityMapper {

    fun toSetEntity(set: Set): SetEntity {
        return SetEntity(
            set.id.value,
            set.code.value,
            set.name.value
        )
    }

    fun fromSetEntity(setEntity: SetEntity): Set {
        return Set(SetId(setEntity.id), SetCode(setEntity.code), SetName(setEntity.name))
    }

}
