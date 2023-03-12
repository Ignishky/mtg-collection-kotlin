package fr.ignishky.mtgcollection.infrastructure.api.rest.set

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.mtgcollection.domain.card.port.CardApiPort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.port.SetApiPort
import org.springframework.web.bind.annotation.RestController

@RestController
class SetController(
    private val setApiPort: SetApiPort,
    private val cardApiPort: CardApiPort,
) : SetApi {

    override fun getAll(correlationId: CorrelationId): SetsResponse {
        val sets = setApiPort.getAll()
        return SetsResponse(
            sets.map { SetsResponse.SetResponse(it.code.value, it.name.value, it.icon.value) }
        )
    }

    override fun getCards(correlationId: CorrelationId, setCode: String): CardsResponse {
        val cards = cardApiPort.getAll(SetCode(setCode))
        return CardsResponse(
            cards.map { CardsResponse.CardResponse(it.name.value, it.images[0].value) }
        )
    }

}
