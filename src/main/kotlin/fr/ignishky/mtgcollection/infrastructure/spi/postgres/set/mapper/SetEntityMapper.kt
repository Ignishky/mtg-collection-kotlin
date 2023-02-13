package fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.mapper

import fr.ignishky.mtgcollection.domain.set.model.*
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model.SetEntity

object SetEntityMapper {

    fun toSetEntity(set: Set): SetEntity {
        return SetEntity(
            set.id.value,
            set.code.value,
            set.name.value,
            set.icon.value
        )
    }

    fun fromSetEntity(setEntity: SetEntity): Set {
        return Set(SetId(setEntity.id), SetCode(setEntity.code), SetName(setEntity.name), SetIcon(setEntity.icon))
    }

}
