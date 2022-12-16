package fr.ignishky.mtgcollection.infrastructure

import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.springframework.http.MediaType

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
                    .withStatusCode(HttpStatus.SC_OK)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBody(TestUtils.readFile("refresh/sets.json"))
            )
    }

}
