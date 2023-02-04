package fr.ignishky.mtgcollection.infrastructure

import fr.ignishky.mtgcollection.infrastructure.TestUtils.readFile
import org.apache.http.HttpHeaders.CONTENT_TYPE
import org.apache.http.HttpStatus.SC_OK
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.springframework.http.HttpMethod.GET
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class MockServerBuilder(
    private val mockServer: MockServerClient
) {

    fun prepareSets() {
        mockServer
            .`when`(
                request()
                    .withMethod(GET.name())
                    .withPath("/sets")
            )
            .respond(
                response()
                    .withStatusCode(SC_OK)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withBody(readFile("refresh/sets.json"))
            )
    }

    fun prepareCards() {
        prepareCard("khm")
        prepareCard("afr")
        prepareCard("afr_page2")
    }

    private fun prepareCard(setCode: String) {
        mockServer
            .`when`(
                request()
                    .withMethod(GET.name())
                    .withPath("/cards/search")
                    .withQueryStringParameter("order", "set")
                    .withQueryStringParameter("q", "e:$setCode")
                    .withQueryStringParameter("unique", "prints")
            )
            .respond(
                response()
                    .withStatusCode(SC_OK)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withBody(readFile("refresh/$setCode.json").replace("{baseUrl}", "http://localhost:${mockServer.port}"))
            )
    }

}
