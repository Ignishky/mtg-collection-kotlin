package fr.ignishky.mtgcollection.infrastructure.api.rest.set

data class CardsResponse(
    val cards: List<CardResponse>
) {

    data class CardResponse(
        val name: String,
        val image: String
    )

}
