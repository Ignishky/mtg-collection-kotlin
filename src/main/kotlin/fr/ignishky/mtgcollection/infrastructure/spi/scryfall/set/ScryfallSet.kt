package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.set

data class ScryfallSet(
    val data: List<ScryfallSetData>
) {

    @Suppress("unused")
    constructor() : this(listOf())

    data class ScryfallSetData(
        val id: String,
        val code: String,
        val name: String,
        val icon_svg_uri: String
    ) {

        @Suppress("unused")
        constructor() : this("", "", "", "")

    }

}
