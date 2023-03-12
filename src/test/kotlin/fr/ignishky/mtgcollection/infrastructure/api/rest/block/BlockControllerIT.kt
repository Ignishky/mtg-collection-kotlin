package fr.ignishky.mtgcollection.infrastructure.api.rest.block

import fr.ignishky.framework.domain.CorrelationId
import fr.ignishky.framework.domain.CorrelationIdGenerator
import fr.ignishky.mtgcollection.infrastructure.JdbcUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockserver.springtest.MockServerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@MockServerTest("scryfall.base-url=http://localhost:\${mockServerPort}")
class BlockControllerIT(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val jdbc: JdbcUtils,
) {

    @MockBean
    private lateinit var correlationIdGenerator: CorrelationIdGenerator

    private val correlationId = CorrelationId("test-correlation-id")

    @BeforeEach
    fun setUp() {
        jdbc.dropAll()
        Mockito.`when`(correlationIdGenerator.generate()).thenReturn(correlationId)
    }

    @Test
    fun `Should return blocks`() {
        val result = mockMvc.perform(get("/blocks"))
    }

}
