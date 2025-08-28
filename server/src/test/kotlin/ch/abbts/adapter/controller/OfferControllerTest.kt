package ch.abbts.adapter.controller

import ch.abbts.adapter.database.repository.AssignmentRepository
import ch.abbts.adapter.database.repository.TaskRepository
import ch.abbts.adapter.database.repository.UsersRepository
import ch.abbts.adapter.routes.configureRouting
import ch.abbts.application.dto.*
import ch.abbts.application.interactor.AssignmentInteractor
import ch.abbts.application.interactor.OfferInteractor
import ch.abbts.application.interactor.TaskInteractor
import ch.abbts.application.interactor.UserInteractor
import ch.abbts.domain.model.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.gradle.internal.impldep.junit.framework.TestCase.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class OfferControllerTest {

    private suspend fun fetchJwt(client: io.ktor.client.HttpClient): String {
        val userExisting =
            AuthenticationDto(
                email = "bob@builder.ch",
                password = "1234",
                authProvider = AuthProvider.LOCAL,
            )
        val response =
            client.post("/v1/auth") {
                contentType(ContentType.Application.Json)
                setBody(userExisting)
            }
        assertEquals(HttpStatusCode.OK, response.status)
        val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertTrue(json.containsKey("jwt"))
        return json["jwt"]!!.toString().trim('"')
    }

    @Test
    fun `POST create offer returns 403 when dto_userId != JWT userId`() =
        testApplication {
            lateinit var mockUserInteractor: UserInteractor
            lateinit var mockOfferInteractor: OfferInteractor
            val taskInteractor =
                TaskInteractor(TaskRepository(), UsersRepository())
            val assignmentInteractor =
                AssignmentInteractor(AssignmentRepository(), TaskRepository())

            application {
                mockUserInteractor = mock {
                    on { verifyLocalUser(eq("bob@builder.ch"), any()) } doReturn
                        UserDto(
                            email = "bob@builder.ch",
                            birthdate = "",
                            id = 53,
                        )
                }
                doNothing()
                    .whenever(mockUserInteractor)
                    .updateIssuedTime(eq("bob@builder.ch"), any())

                mockOfferInteractor = mock()

                configureRouting(
                    mockUserInteractor,
                    mockOfferInteractor,
                    taskInteractor,
                    assignmentInteractor,
                )
            }

            val client = createClient { install(ContentNegotiation) { json() } }
            val jwt = fetchJwt(client)

            val payload =
                OfferDto(
                    id = null,
                    userId = 999,
                    taskId = 7,
                    status = OfferStatus.SUBMITTED,
                    active = true,
                    text = "hello",
                    title = "test",
                    validUntil = null,
                )

            val res =
                client.post("/v1/offer") {
                    header(HttpHeaders.Authorization, "Bearer $jwt")
                    contentType(ContentType.Application.Json)
                    setBody(payload)
                }

            assertEquals(HttpStatusCode.Forbidden, res.status)
            verify(mockOfferInteractor, never()).createOffer(any())
        }
}
