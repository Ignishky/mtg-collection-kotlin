package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.card

data class ScryfallCard(
    val data: List<ScryfallCardData>
) {

    @Suppress("unused")
    constructor() : this(listOf())

    data class ScryfallCardData(
        val id: String,
        val name: String,
        val set: String
    ) {

        @Suppress("unused")
        constructor() : this("", "", "")

    }

}
