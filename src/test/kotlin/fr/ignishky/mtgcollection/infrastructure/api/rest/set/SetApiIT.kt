package fr.ignishky.mtgcollection.infrastructure.api.rest.set

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.framework.domain.CorrelationIdGenerator
import fr.ignishky.mtgcollection.infrastructure.JdbcUtils
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.afr
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.arboreaPegasus
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.axgardBraggart
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.flumph
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.khm
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.plus2Mace
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.specialPegasus
import fr.ignishky.mtgcollection.infrastructure.TestUtils.readFile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockserver.springtest.MockServerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@MockServerTest("scryfall.base-url=http://localhost:\${mockServerPort}")
internal class SetApiIT(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val jdbc: JdbcUtils,
) {

    @MockBean
    private lateinit var correlationIdGenerator: CorrelationIdGenerator

    private val correlationId = CorrelationId("test-correlation-id")
    private val afr = afr()
    private val khm = khm()
    private val plus2Mace = plus2Mace()
    private val arboreaPegasus = arboreaPegasus()
    private val axgardBraggart = axgardBraggart()
    private val flumph = flumph()
    private val specialPegasus = specialPegasus()

    @BeforeEach
    fun setUp() {
        jdbc.dropAll()
        `when`(correlationIdGenerator.generate()).thenReturn(correlationId)
    }

    @Test
    fun `Should return sets`() {
        // GIVEN
        jdbc.save(listOf(khm, afr), emptyList())

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
        jdbc.save(listOf(afr), listOf(arboreaPegasus, specialPegasus, flumph, plus2Mace, axgardBraggart))

        // WHEN
        val resultActions = mockMvc.perform(get("/sets/afr"))

        // THEN
        resultActions.andExpectAll(
            status().isOk,
            content().contentType(APPLICATION_JSON),
            content().json(readFile("refresh/cardsResponse.json"), true)
        )
    }

}
