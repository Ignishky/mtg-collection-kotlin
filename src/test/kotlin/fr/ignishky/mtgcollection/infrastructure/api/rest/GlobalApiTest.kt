package fr.ignishky.mtgcollection.infrastructure.api.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
internal class GlobalApiTest(
    @Autowired val mockMvc: MockMvc
) {

    @Test
    fun `Should load cards from scryfall`() {
        mockMvc.perform(put("/sets"))
            .andExpect(status().isNoContent)
    }

}
