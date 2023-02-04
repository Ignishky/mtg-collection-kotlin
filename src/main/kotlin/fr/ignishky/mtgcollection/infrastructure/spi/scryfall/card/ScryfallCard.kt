package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.card

data class ScryfallCard(
    val has_more: Boolean,
    val next_page: String,
    val data: List<ScryfallCardData>
) {

    @Suppress("unused")
    constructor() : this(false, "", listOf())

    data class ScryfallCardData(
        val id: String,
        val name: String,
        val set: String
    ) {

        @Suppress("unused")
        constructor() : this("", "", "")

    }

}
