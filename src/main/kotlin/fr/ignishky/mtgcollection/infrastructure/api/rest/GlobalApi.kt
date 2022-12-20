package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.framework.domain.CorrelationId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/sets")
interface GlobalApi {

    @PutMapping
    fun loadAll(@RequestAttribute correlationId: CorrelationId): ResponseEntity<Void>

    @GetMapping
    fun getAll(@RequestAttribute correlationId: CorrelationId): SetsResponse

}
