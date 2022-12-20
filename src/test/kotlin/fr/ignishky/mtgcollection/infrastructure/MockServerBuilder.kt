package fr.ignishky.mtgcollection.infrastructure

import fr.ignishky.mtgcollection.infrastructure.TestUtils.readFile
import org.apache.http.HttpHeaders.CONTENT_TYPE
import org.apache.http.HttpStatus.SC_OK
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

object MockServerBuilder {

    fun prepareSets(mockServer: MockServerClient) {
        mockServer
            .`when`(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/sets")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(SC_OK)
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withBody(readFile("refresh/sets.json"))
            )
    }

}
