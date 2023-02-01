package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.card

import fr.ignishky.mtgcollection.configuration.ScryfallProperties
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardId
import fr.ignishky.mtgcollection.domain.card.model.CardName
import fr.ignishky.mtgcollection.domain.card.port.CardRefererPort
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import jakarta.inject.Named
import mu.KotlinLogging.logger
import org.springframework.web.client.HttpClientErrorException.NotFound
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Named
class ScryfallCardReferer(
    private val restTemplate: RestTemplate,
    private val properties: ScryfallProperties
) : CardRefererPort {

    private val logger = logger {}

    override fun getCards(code: SetCode): List<Card> {
        return try {
            restTemplate.getForObject<ScryfallCard>("${properties.baseUrl}/cards/search?order=set&q=e:${code.value}&unique=prints")
                .data
        } catch (e: NotFound) {
            logger.warn { "Unable to get cards for ${code.value}" }
            listOf()
        }
            .map { Card(CardId(it.id), CardName(it.name), SetCode(it.set)) }
    }

}
