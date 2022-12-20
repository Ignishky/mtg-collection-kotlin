package fr.ignishky.mtgcollection.infrastructure.api.rest

data class SetsResponse(
    val sets: List<SetResponse>
) {

    data class SetResponse(
        val code: String,
        val name: String
    )

}
