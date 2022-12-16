package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.framework.cqrs.event.spi.postgres.EventEntity
import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.framework.domain.CorrelationIdGenerator
import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetName
import fr.ignishky.mtgcollection.infrastructure.JdbcUtils
import fr.ignishky.mtgcollection.infrastructure.MockServerBuilder.prepareSets
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.afr
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.khm
import fr.ignishky.mtgcollection.infrastructure.TestFixtures.snc
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.mapper.SetEntityMapper.toSetEntity
import org.assertj.core.api.Assertions.assertThat
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
    private val setUpToDate = snc()
    private val setToCreate = khm()
    private val setUnmodified = afr()

    @Test
    fun `Should load cards from scryfall`() {
        // GIVEN
        `when`(correlationIdGenerator.generate()).thenReturn(correlationId)
        prepareSets(mockServer)
        jdbc.save(setUnmodified, setUpToDate.copy(name = SetName("Old Name")))

        // WHEN
        val resultActions = mockMvc.perform(put("/sets"))

        // THEN
        resultActions.andExpect(status().isNoContent)

        assertThat(jdbc.getEvents()).containsOnly(
            toSetUpdatedEntity(1, setUpToDate),
            toSetCreatedEntity(2, setToCreate)
        )

        assertThat(jdbc.getSets()).containsOnly(
            toSetEntity(setUpToDate),
            toSetEntity(setToCreate),
            toSetEntity(setUnmodified)
        )
    }

    fun toSetUpdatedEntity(id: Long, set: Set): EventEntity {
        return EventEntity(
            id,
            set.id.value,
            "Set",
            "SetUpdated",
            Instant.parse("1981-08-25T13:50:00Z"),
            "{\"code\":\"${set.code.value}\",\"name\":\"${set.name.value}\"}",
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
            "{\"code\":\"${set.code.value}\",\"name\":\"${set.name.value}\"}",
            correlationId.value
        )
    }

}
