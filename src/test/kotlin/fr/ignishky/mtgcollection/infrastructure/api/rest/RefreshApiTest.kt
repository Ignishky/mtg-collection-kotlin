package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.framework.cqrs.event.spi.postgres.EventEntity
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.framework.domain.CorrelationIdGenerator
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.card.model.CardName
import fr.ignishky.mtgcollection.domain.card.model.CollectionNumber
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetIcon
import fr.ignishky.mtgcollection.infrastructure.JdbcUtils
import fr.ignishky.mtgcollection.infrastructure.MockServerBuilder
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.afr
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.axgardBraggart
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.halvar
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.khm
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.plus2Mace
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.valorSinger
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.card.mapper.CardEntityMapper.toCardEntity
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.mapper.SetEntityMapper.toSetEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockserver.client.MockServerClient
import org.mockserver.springtest.MockServerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant.parse

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@MockServerTest("scryfall.base-url=http://localhost:\${mockServerPort}")
class RefreshApiTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val jdbc: JdbcUtils,
) {

    @MockBean
    private lateinit var correlationIdGenerator: CorrelationIdGenerator
    private lateinit var mockServer: MockServerClient

    private val correlationId = CorrelationId("test-correlation-id")
    private val khm = khm()
    private val axgardBraggart = axgardBraggart()
    private val halvar = halvar()
    private val afr = afr()
    private val plus2mace = plus2Mace()
    private val arboreaPegasus = arboreaPegasus()
    private val valor = valorSinger()

    @BeforeEach
    fun setUp() {
        jdbc.dropAll()
        `when`(correlationIdGenerator.generate()).thenReturn(correlationId)
    }

    @Test
    fun `Should create new set and cards`() {
        val mockServerBuilder = MockServerBuilder(mockServer)
        mockServerBuilder.prepareSets("scryfall_set_khm.json")
        mockServerBuilder.prepareCards("khm")

        val resultActions = mockMvc.perform(put("/sets"))

        resultActions.andExpect(status().isNoContent)
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsOnly(
                toSetCreatedEntity(khm),
                toCardCreatedEntity(axgardBraggart),
                toCardCreatedEntity(halvar),
            )
        assertThat(jdbc.getSets()).containsOnly(toSetEntity(khm))
        assertThat(jdbc.getCards()).containsOnly(toCardEntity(axgardBraggart), toCardEntity(halvar))
    }

    @Test
    fun `Should skip update of unmodified set and cards`() {
        val mockServerBuilder = MockServerBuilder(mockServer)
        mockServerBuilder.prepareSets("scryfall_set_khm.json")
        mockServerBuilder.prepareCards("khm")
        jdbc.save(listOf(khm), listOf(axgardBraggart, halvar))

        val resultActions = mockMvc.perform(put("/sets"))

        resultActions.andExpect(status().isNoContent)
        assertThat(jdbc.getEvents()).isEmpty()
        assertThat(jdbc.getSets()).containsOnly(toSetEntity(khm))
        assertThat(jdbc.getCards()).containsOnly(toCardEntity(axgardBraggart), toCardEntity(halvar))
    }

    @Test
    fun `Should update modified set and cards`() {
        val mockServerBuilder = MockServerBuilder(mockServer)
        mockServerBuilder.prepareSets("scryfall_set_khm.json")
        mockServerBuilder.prepareCards("khm")
        jdbc.save(
            listOf(khm.copy(icon = SetIcon("Old Icon"))),
            listOf(
                axgardBraggart.copy(name = CardName("Old Name")),
                halvar.copy(images = emptyList(), collectionNumber = CollectionNumber(-1))
            )
        )

        val resultActions = mockMvc.perform(put("/sets"))

        resultActions.andExpect(status().isNoContent)
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsOnly(toSetUpdatedEntity(khm), toCardUpdatedEntity(axgardBraggart), toCardUpdatedEntity(halvar))
        assertThat(jdbc.getSets()).containsOnly(toSetEntity(khm))
        assertThat(jdbc.getCards()).containsOnly(toCardEntity(axgardBraggart), toCardEntity(halvar))
    }

    @Test
    fun `Should handle multiple pages of cards`() {
        val mockServerBuilder = MockServerBuilder(mockServer)
        mockServerBuilder.prepareSets("scryfall_set_afr.json")
        mockServerBuilder.prepareCards("afr", "afr_page2")

        val resultActions = mockMvc.perform(put("/sets"))

        resultActions.andExpect(status().isNoContent)
        assertThat(jdbc.getEvents())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsOnly(
                toSetCreatedEntity(afr),
                toCardCreatedEntity(plus2mace),
                toCardCreatedEntity(arboreaPegasus),
                toCardCreatedEntity(valor),
            )
        assertThat(jdbc.getSets()).containsOnly(toSetEntity(afr))
        assertThat(jdbc.getCards()).containsOnly(toCardEntity(plus2mace), toCardEntity(arboreaPegasus), toCardEntity(valor))
    }

    fun toSetCreatedEntity(set: Set): EventEntity {
        return EventEntity(
            0,
            set.id.value,
            "Set",
            "SetCreated",
            parse("1981-08-25T13:50:00Z"),
            "{\"code\":\"${set.code.value}\",\"name\":\"${set.name.value}\",\"icon\":\"${set.icon.value}\"}",
            correlationId.value
        )
    }

    fun toSetUpdatedEntity(set: Set): EventEntity {
        return EventEntity(
            0,
            set.id.value,
            "Set",
            "SetUpdated",
            parse("1981-08-25T13:50:00Z"),
            "{\"code\":\"${set.code.value}\",\"name\":\"${set.name.value}\",\"icon\":\"${set.icon.value}\"}",
            correlationId.value
        )
    }

    fun toCardCreatedEntity(card: Card): EventEntity {
        return EventEntity(
            0,
            card.id.value,
            "Card",
            "CardCreated",
            parse("1981-08-25T13:50:00Z"),
            "{\"name\":\"${card.name.value}\",\"setCode\":\"${card.setCode.value}\",\"images\":[${card.images.joinToString(",") { "\"${it.value}\"" }}],\"collectionNumber\":${card.collectionNumber.value}}",
            correlationId.value
        )
    }

    fun toCardUpdatedEntity(card: Card): EventEntity {
        return EventEntity(
            0,
            card.id.value,
            "Card",
            "CardUpdated",
            parse("1981-08-25T13:50:00Z"),
            "{\"name\":\"${card.name.value}\",\"images\":[${card.images.joinToString(",") { "\"${it.value}\"" }}],\"collectionNumber\":${card.collectionNumber.value}}",
            correlationId.value
        )
    }

}
