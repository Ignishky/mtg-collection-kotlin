package fr.ignishky.mtgcollection.infrastructure.api.rest.block

data class BlocksResponse(
    val blocks: List<BlockResponse>
) {

    data class BlockResponse(
        val name: String,
        val icon: String,
    )

}
