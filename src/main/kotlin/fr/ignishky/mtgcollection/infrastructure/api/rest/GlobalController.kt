package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.framework.cqrs.command.CommandBus
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.command.RefreshSetCommand
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
internal class GlobalController(
    private val commandBus: CommandBus
) : GlobalApi {

    override fun loadAll(correlationId: CorrelationId): ResponseEntity<Void> {
        commandBus.dispatch(RefreshSetCommand(), correlationId)
        return ResponseEntity(NO_CONTENT)
    }

}
