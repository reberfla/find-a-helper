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
            application {
                val mockUserInteractor =
                    mock<UserInteractor> {
                        on {
                            verifyLocalUser(eq("bob@builder.ch"), any())
                        } doReturn
                            UserDto(
                                email = "bob@builder.ch",
                                birthdate = "",
                                id = 53,
                            )
                        doNothing()
                            .whenever(mock)
                            .updateIssuedTime(eq("bob@builder.ch"), any())
                        given(mock.verifyLocalUser(eq("bob@builder.ch"), any()))
                            .willAnswer { throw UserNotFound() }
                    }

                val mockOfferInteractor = mock<OfferInteractor>()
                val taskInteractor =
                    TaskInteractor(TaskRepository(), UsersRepository())
                val assignmentInteractor =
                    AssignmentInteractor(
                        AssignmentRepository(),
                        TaskRepository(),
                    )

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
            verify(mock<OfferInteractor>(), never()).createOffer(any())
        }

    @Test
    fun `GET my returns 200 and calls interactor with JWT userId`() =
        testApplication {
            application {
                val mockUserInteractor =
                    mock<UserInteractor> {
                        on {
                            verifyLocalUser(eq("bob@builder.ch"), any())
                        } doReturn
                            UserDto(
                                email = "bob@builder.ch",
                                birthdate = "",
                                id = 55,
                            )
                        doNothing()
                            .whenever(mock)
                            .updateIssuedTime(eq("bob@builder.ch"), any())
                        given(mock.verifyLocalUser(eq("bob@builder.ch"), any()))
                            .willAnswer { throw UserNotFound() }
                    }

                val mockOfferInteractor =
                    mock<OfferInteractor> {
                        on { getOffersByCreator(eq(55)) } doReturn
                            listOf(
                                OfferDto(
                                    id = 11,
                                    userId = 55,
                                    taskId = 7,
                                    status = OfferStatus.SUBMITTED,
                                    active = true,
                                    text = "A",
                                    title = "A1",
                                    validUntil = null,
                                ),
                                OfferDto(
                                    id = 12,
                                    userId = 55,
                                    taskId = 9,
                                    status = OfferStatus.ACCEPTED,
                                    active = true,
                                    text = "B",
                                    title = "B1",
                                    validUntil = null,
                                ),
                            )
                    }

                val mockTaskInteractor =
                    mock<TaskInteractor> {
                        on { getTaskById(eq(7)) } doReturn
                            TaskModel(
                                id = 7,
                                userId = 55,
                                zipCode = "4545",
                                coordinates = "0,0",
                                title = "T7",
                                description = "D7",
                                category = TaskCategory.OTHERS,
                                status = TaskStatus.OPEN,
                                active = true,
                                deadline = null,
                                taskInterval = TaskInterval.CONTINUOUS,
                                weekdays = emptyList(),
                                createdAt = null,
                            )
                        on { getTaskById(eq(9)) } doReturn
                            TaskModel(
                                id = 9,
                                userId = 55,
                                zipCode = "4614",
                                coordinates = "1,1",
                                title = "T9",
                                description = "D9",
                                category = TaskCategory.OTHERS,
                                status = TaskStatus.OPEN,
                                active = true,
                                deadline = null,
                                taskInterval = TaskInterval.ONE_TIME,
                                weekdays = emptyList(),
                                createdAt = null,
                            )
                    }

                val assignmentInteractor =
                    AssignmentInteractor(
                        AssignmentRepository(),
                        TaskRepository(),
                    )

                configureRouting(
                    mockUserInteractor,
                    mockOfferInteractor,
                    mockTaskInteractor,
                    assignmentInteractor,
                )
            }

            val client = createClient { install(ContentNegotiation) { json() } }
            val jwt = fetchJwt(client)

            val res =
                client.get("/v1/offer/my") {
                    header(HttpHeaders.Authorization, "Bearer $jwt")
                }

            assertEquals(HttpStatusCode.OK, res.status)
            verify(any<OfferInteractor>(), times(1)).getOffersByCreator(eq(55))
            verify(any<TaskInteractor>(), times(1)).getTaskById(eq(7))
            verify(any<TaskInteractor>(), times(1)).getTaskById(eq(9))

            val body = res.bodyAsText()
            assertTrue(body.contains("A1"))
            assertTrue(body.contains("B1"))
        }
}
