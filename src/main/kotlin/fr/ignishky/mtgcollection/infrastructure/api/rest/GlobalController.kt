package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.framework.cqrs.command.CommandBus
import fr.ignishky.mtgcollection.domain.command.RefreshSetCommand
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
internal class GlobalController(val commandBus: CommandBus) : GlobalApi {

    override fun loadAll(): ResponseEntity<Void> {
        commandBus.dispatch(RefreshSetCommand())
        return ResponseEntity(NO_CONTENT)
    }

}
