package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.framework.domain.CorrelationIdGenerator
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.EventDTO
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.EventDTORowMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Clock
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
internal class GlobalApiTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val template: JdbcTemplate,
    @Autowired private val clock: Clock,
) {

    @MockBean
    private lateinit var correlationIdGenerator: CorrelationIdGenerator

    private val correlationId = CorrelationId(UUID.randomUUID())

    @Test
    fun `Should load cards from scryfall`() {

        Mockito.`when`(correlationIdGenerator.generate()).thenReturn(correlationId)
        mockMvc.perform(put("/sets"))
            .andExpect(status().isNoContent)

        assertThat(template.query("SELECT * FROM events", EventDTORowMapper())).containsOnly(
            EventDTO(1, "AggregateId1", "AggregateName", "AggregateLabel", clock.instant(), "", correlationId.toString()),
            EventDTO(2, "AggregateId2", "AggregateName", "AggregateLabel", clock.instant(), "", correlationId.toString())
        )
    }

}
