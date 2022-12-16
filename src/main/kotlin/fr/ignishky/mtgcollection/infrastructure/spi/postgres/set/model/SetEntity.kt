package fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "sets")
data class SetEntity(
    @Id
    internal val id: String,
    internal val code: String,
    internal val name: String
) {

    constructor() : this("", "", "")

}
