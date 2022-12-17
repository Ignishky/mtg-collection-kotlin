package fr.ignishky.mtgcollection.infrastructure.spi.scryfall.set

import fr.ignishky.mtgcollection.configuration.ScryfallProperties
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetCode
import fr.ignishky.mtgcollection.domain.set.model.SetId
import fr.ignishky.mtgcollection.domain.set.model.SetName
import fr.ignishky.mtgcollection.domain.set.port.SetRefererPort
import jakarta.inject.Named
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Named
class ScryfallSetReferer(
    private val restTemplate: RestTemplate,
    private val properties: ScryfallProperties
) : SetRefererPort {

    override fun getAllSets(): List<Set> {
        return restTemplate.getForObject<ScryfallSet>("${properties.baseUrl}/sets")
            .data
            .map { Set(SetId(it.id), SetCode(it.code), SetName(it.name)) }
    }

}
