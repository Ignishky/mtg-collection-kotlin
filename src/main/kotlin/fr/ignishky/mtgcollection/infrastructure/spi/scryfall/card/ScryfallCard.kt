package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.card

import com.fasterxml.jackson.annotation.JsonProperty

data class ScryfallCard(
    @JsonProperty("has_more")
    val hasMore: Boolean,
    @JsonProperty("next_page")
    val nextPage: String?,
    val data: List<ScryfallCardData>
) {

    @Suppress("unused")
    constructor() : this(false, "", listOf())

    data class ScryfallCardData(
        val id: String,
        val name: String,
        val set: String,
        @JsonProperty("image_uris")
        val imageUris: ImageUris?,
        @JsonProperty("card_faces")
        val cardFaces: List<CardFaces>?,
        @JsonProperty("collector_number")
        val collectionNumber: String,
    ) {

        @Suppress("unused")
        constructor() : this("", "", "", ImageUris(""), emptyList(), "")

    }

    data class ImageUris(
        @JsonProperty("border_crop")
        val borderCrop: String
    ) {

        @Suppress("unused")
        constructor() : this("")

    }

    data class CardFaces(
        @JsonProperty("image_uris")
        val imageUris: ImageUris?
    ) {

        @Suppress("unused")
        constructor() : this(ImageUris(""))

    }

}
