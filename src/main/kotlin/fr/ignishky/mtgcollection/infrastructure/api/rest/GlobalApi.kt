package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.framework.domain.CorrelationId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/sets")
interface GlobalApi {

    @PutMapping
    fun loadAll(@RequestAttribute correlationId: CorrelationId): ResponseEntity<Void>

    @GetMapping
    fun getAll(@RequestAttribute correlationId: CorrelationId): SetsResponse

    @GetMapping("/{setCode}")
    fun getCards(@RequestAttribute correlationId: CorrelationId, @PathVariable setCode: String): CardsResponse

}
