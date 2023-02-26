package fr.ignishky.mtgcollection.infrastructure.api.rest

data class CardsResponse(
    val cards: List<CardResponse>
) {

    data class CardResponse(
        val name: String,
    )

}
