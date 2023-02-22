package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.framework.cqrs.command.CommandBus
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.command.RefreshCard
import fr.ignishky.mtgcollection.domain.card.port.CardApiPort
import fr.ignishky.mtgcollection.domain.set.command.RefreshSet
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.port.SetApiPort
import fr.ignishky.mtgcollection.infrastructure.api.rest.CardsResponse.CardResponse
import fr.ignishky.mtgcollection.infrastructure.api.rest.SetsResponse.SetResponse
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
internal class GlobalController(
    private val commandBus: CommandBus,
    private val setApiPort: SetApiPort,
    private val cardApiPort: CardApiPort
) : GlobalApi {

    override fun loadAll(correlationId: CorrelationId): ResponseEntity<Void> {
        commandBus.dispatch(RefreshSet(), correlationId)
        commandBus.dispatch(RefreshCard(), correlationId)
        return ResponseEntity(NO_CONTENT)
    }

    override fun getAll(correlationId: CorrelationId): SetsResponse {
        val sets = setApiPort.getAll()
        return SetsResponse(
            sets.map { SetResponse(it.code.value, it.name.value, it.icon.value) }
        )
    }

    override fun getCards(correlationId: CorrelationId, setCode: String): CardsResponse {
        val cards = cardApiPort.getAll(SetCode(setCode))
        return CardsResponse(
            cards.map { CardResponse(it.name.value) }
        )
    }

}
