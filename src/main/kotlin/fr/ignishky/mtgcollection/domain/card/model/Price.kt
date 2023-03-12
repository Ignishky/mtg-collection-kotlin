package fr.ignishky.mtgcollection.domain.card.model

data class Price(
    internal val eur: Long,
    internal val eurFoil: Long,
    internal val usd: Long,
    internal val usdFoil: Long,
)
