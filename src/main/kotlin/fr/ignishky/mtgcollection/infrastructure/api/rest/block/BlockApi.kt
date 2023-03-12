package fr.ignishky.mtgcollection.infrastructure.api.rest.block;

import fr.ignishky.framework.domain.CorrelationId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/blocks")
interface BlockApi {

    @GetMapping
    fun getAll(@RequestAttribute correlationId: CorrelationId): BlocksResponse
}
