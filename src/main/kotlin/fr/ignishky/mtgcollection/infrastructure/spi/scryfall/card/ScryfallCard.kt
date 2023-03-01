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
        val set: String,
        val image_uris: ImageUris,
        val card_faces: List<CardFaces>
    ) {

        @Suppress("unused")
        constructor() : this("", "", "", ImageUris(""), emptyList())

    }

    data class ImageUris(
        val border_crop: String
    ) {

        @Suppress("unused")
        constructor() : this("")

    }

    data class CardFaces(
        val image_uris: ImageUris
    ) {

        @Suppress("unused")
        constructor() : this(ImageUris(""))

    }

}
