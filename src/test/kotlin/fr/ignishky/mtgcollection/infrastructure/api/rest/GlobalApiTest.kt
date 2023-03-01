package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.framework.cqrs.event.spi.postgres.EventEntity
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.framework.domain.CorrelationIdGenerator
import fr.ignishky.mtgcollection.domain.card.model.Card
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetIcon
import fr.ignishky.mtgcollection.domain.set.model.SetName
import fr.ignishky.mtgcollection.infrastructure.JdbcUtils
import fr.ignishky.mtgcollection.infrastructure.MockServerBuilder
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.afr
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.axgardBraggart
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.halvar
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.khm
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.plus2Mace
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.snc
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.valorSinger
import fr.ignishky.mtgcollection.infrastructure.TestUtils.readFile
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
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@MockServerTest("scryfall.base-url=http://localhost:\${mockServerPort}")
internal class GlobalApiTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val jdbc: JdbcUtils,
) {

    @MockBean
    private lateinit var correlationIdGenerator: CorrelationIdGenerator
    private lateinit var mockServer: MockServerClient

    private val correlationId = CorrelationId("test-correlation-id")
    private val setUpToDate = afr()
    private val setToCreate = khm()
    private val setUnmodified = snc()
    private val card1ToCreate = plus2Mace()
    private val card2ToUpdate = arboreaPegasus()
    private val card3ToCreate = valorSinger()
    private val cardUnmodified = axgardBraggart()
    private val card4ToCreate = halvar()

    @BeforeEach
    fun setUp() {
        jdbc.dropAll()
        `when`(correlationIdGenerator.generate()).thenReturn(correlationId)
    }

    @Test
    fun `Should load cards from scryfall`() {
        // GIVEN
        val mockServerBuilder = MockServerBuilder(mockServer)
        mockServerBuilder.prepareSets()
        mockServerBuilder.prepareCards()
        jdbc.save(
            listOf(setUpToDate.copy(name = SetName("Old Name"), icon = SetIcon(""))),
            listOf(card2ToUpdate.copy(images = emptyList()), cardUnmodified)
        )

        // WHEN
        val resultActions = mockMvc.perform(put("/sets"))

        // THEN
        resultActions.andExpect(status().isNoContent)

        assertThat(jdbc.getEvents()).containsOnly(
            toSetUpdatedEntity(1, setUpToDate),
            toSetCreatedEntity(2, setToCreate),
            toCardCreatedEntity(3, card1ToCreate),
            toCardUpdatedEntity(4, card2ToUpdate),
            toCardCreatedEntity(5, card3ToCreate),
            toCardCreatedEntity(6, card4ToCreate),
        )

        assertThat(jdbc.getSets()).containsOnly(
            toSetEntity(setUpToDate),
            toSetEntity(setToCreate),
        )

        assertThat(jdbc.getCards()).containsOnly(
            toCardEntity(card3ToCreate),
            toCardEntity(card1ToCreate),
            toCardEntity(card2ToUpdate),
            toCardEntity(cardUnmodified),
            toCardEntity(card4ToCreate)
        )
    }

    @Test
    fun `Should return sets`() {
        // GIVEN
        jdbc.save(listOf(setUnmodified, setUpToDate), listOf())

        // WHEN
        val resultActions = mockMvc.perform(get("/sets"))

        // THEN
        resultActions.andExpectAll(
            status().isOk,
            content().contentType(APPLICATION_JSON),
            content().json(readFile("refresh/setsResponse.json"))
        )
    }

    @Test
    fun `Should return cards from given set`() {
        // GIVEN
        jdbc.save(listOf(setUpToDate), listOf(card1ToCreate, card2ToUpdate, cardUnmodified))

        // WHEN
        val resultActions = mockMvc.perform(get("/sets/afr"))

        // THEN
        resultActions.andExpectAll(
            status().isOk,
            content().contentType(APPLICATION_JSON),
            content().json(readFile("refresh/cardsResponse.json"))
        )
    }

    fun toSetUpdatedEntity(id: Long, set: Set): EventEntity {
        return EventEntity(
            id,
            set.id.value,
            "Set",
            "SetUpdated",
            Instant.parse("1981-08-25T13:50:00Z"),
            "{\"code\":\"${set.code.value}\",\"name\":\"${set.name.value}\",\"icon\":\"${set.icon.value}\"}",
            correlationId.value
        )
    }

    fun toSetCreatedEntity(id: Long, set: Set): EventEntity {
        return EventEntity(
            id,
            set.id.value,
            "Set",
            "SetCreated",
            Instant.parse("1981-08-25T13:50:00Z"),
            "{\"code\":\"${set.code.value}\",\"name\":\"${set.name.value}\",\"icon\":\"${set.icon.value}\"}",
            correlationId.value
        )
    }

    fun toCardCreatedEntity(id: Long, card: Card): EventEntity {
        return EventEntity(
            id,
            card.id.value,
            "Card",
            "CardCreated",
            Instant.parse("1981-08-25T13:50:00Z"),
            "{\"name\":\"${card.name.value}\",\"setCode\":\"${card.setCode.value}\",\"images\":[${card.images.joinToString(",") { "\"${it.value}\"" }}]}",
            correlationId.value
        )
    }

    fun toCardUpdatedEntity(id: Long, card: Card): EventEntity {
        return EventEntity(
            id,
            card.id.value,
            "Card",
            "CardUpdated",
            Instant.parse("1981-08-25T13:50:00Z"),
            "{\"images\":[${card.images.joinToString(",") { "\"${it.value}\"" }}]}",
            correlationId.value
        )
    }

}
