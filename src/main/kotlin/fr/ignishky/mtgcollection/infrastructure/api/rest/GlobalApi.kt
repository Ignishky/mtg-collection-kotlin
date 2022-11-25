package fr.ignishky.mtgcollection.infrastructure.api.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/sets")
interface GlobalApi {

    @PutMapping
    fun loadAll(): ResponseEntity<Void>

}