package ch.abbts.adapter.controller

import ch.abbts.adapter.routes.configureRouting
import ch.abbts.application.dto.AuthenticationDto
import ch.abbts.application.interactor.UserInteractor
import ch.abbts.domain.model.AuthProvider
import ch.abbts.error.UserNotFound
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class AuthenticationTest() {
    @Test
    fun testUserAuthentication() = testApplication {
        application {
            val mockUserInteractor =
                mock<UserInteractor> {
                    given(mock.verifyLocalUser(eq("mailb@example.ch"), any()))
                        .willAnswer { throw UserNotFound() }
                    doNothing()
                        .whenever(mock)
                        .verifyLocalUser(eq("mail@example.ch"), any())
                    doNothing()
                        .whenever(mock)
                        .updateIssuedTime(eq("mail@example.ch"), any())
                }
            configureRouting(mockUserInteractor)
        }
        val customClient = createClient {
            install(ContentNegotiation) { json() }
        }
        val userExisting =
            AuthenticationDto(
                email = "mail@example.ch",
                password = "1234",
                authenticationProvider = AuthProvider.LOCAL,
            )
        val response =
            customClient.post("/v1/auth") {
                contentType(ContentType.Application.Json)
                setBody(userExisting)
            }
        assertEquals(HttpStatusCode.OK, response.status)
        val responseBody =
            Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assert(responseBody.containsKey("JWT"))

        val userNotExisting =
            AuthenticationDto(
                email = "mailb@example.ch",
                password = "1234",
                authenticationProvider = AuthProvider.LOCAL,
            )
        val responseNotExisting =
            customClient.post("/v1/auth") {
                contentType(ContentType.Application.Json)
                setBody(userNotExisting)
            }
        assertEquals(HttpStatusCode.NotFound, responseNotExisting.status)
        assertEquals(
            """{"message":"user not found"}""".trimIndent(),
            responseNotExisting.bodyAsText(),
        )
    }
}
