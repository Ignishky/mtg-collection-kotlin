package fr.ignishky.mtgcollection.infrastructure.api.rest

import fr.ignishky.mtgcollection.infrastructure.spi.postgres.EventDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
internal class GlobalApiTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val template: JdbcTemplate
) {

    @Test
    fun `Should load cards from scryfall`() {
        mockMvc.perform(put("/sets"))
            .andExpect(status().isNoContent)

        assertThat(template.query("SELECT * FROM events", BeanPropertyRowMapper(EventDTO::class.java))).hasSize(2)
    }

}
