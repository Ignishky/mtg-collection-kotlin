package fr.ignishky.mtgcollection.infrastructure.api.rest.block

import fr.ignishky.framework.domain.CorrelationId
import org.springframework.web.bind.annotation.RestController

@RestController
class BlockController : BlockApi {

    override fun getAll(correlationId: CorrelationId): BlocksResponse {
        TODO("Not yet implemented")
    }
}
