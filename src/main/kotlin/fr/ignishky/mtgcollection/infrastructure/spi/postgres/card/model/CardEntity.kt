package fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "cards")
data class CardEntity(
    @Id
    internal val id: String,
    internal val name: String,
    internal val setCode: String,
    internal val scryfallEur: Long,
    internal val scryfallEurFoil: Long,
    internal val scryfallUsd: Long,
    internal val scryfallUsdFoil: Long,
    internal val images: String,
    internal val collectionNumber: String,
) {

    constructor() : this("", "", "", 0, 0, 0, 0, "", "")

}
