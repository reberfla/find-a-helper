package ch.abbts.adapter.controller

import ch.abbts.application.dto.AuthResponseDto
import ch.abbts.application.dto.AuthenticationDto
import ch.abbts.application.dto.SuccessMessage
import ch.abbts.application.dto.UserDto
import ch.abbts.application.interactor.UserInteractor
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.UserAlreadyExists
import ch.abbts.error.WebserverError
import ch.abbts.error.WebserverErrorMessage
import ch.abbts.utils.LoggerService
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

@Tag(["User"])
fun Application.userRoutes(userInteractor: UserInteractor) {
    routing {
        route("/v1/user") {
            @KtorResponds(
                mapping =
                    [
                        ResponseEntry("200", SuccessMessage::class),
                        ResponseEntry("400", WebserverErrorMessage::class),
                        ResponseEntry("500", WebserverErrorMessage::class),
                    ]
            )
            @KtorDescription(
                summary = "Creates a new user in the database.",
                description =
                    """ This API creates a new user if it doesn't already exists.
                When creating, a new user id is given by an integer counter of the db"""",
            )

            post("/register") {
                LoggerService.debugLog("here")
                try {
                    val dto = call.receive<AuthenticationDto>()
                    LoggerService.debugLog("DTO OK: $dto")
                    val newUser = userInteractor.createLocalUser(dto)
                    LoggerService.debugLog(newUser.toString())
                    if (newUser?.id != null) {
                        val token = JWebToken(newUser.email)
                        userInteractor.updateIssuedTime(
                            newUser.email,
                            token.body.iat,
                        )
                        val jwt =
                            JWebToken.generateToken(token.header, token.body)
                        val response =
                            AuthResponseDto(
                                id = newUser.id,
                                token = jwt,
                                email = newUser.email,
                                name = newUser.name,
                                imgUrl = newUser.imageUrl,
                                imgBlob = newUser.imgBase64,
                            )
                        call.respond(HttpStatusCode.OK, response)
                    }
                } catch (e: WebserverError) {
                    val (status, response) =
                        when (e) {
                            is UserAlreadyExists ->
                                HttpStatusCode.Conflict to e.getMessage()
                            else -> e.getStatus() to e.getMessage()
                        }
                    call.respond(status, response)
                }
            }

            put("/{token}") {
                try {
                    val token = call.parameters["token"]
                    if (token == null) {
                        call.respond(HttpStatusCode.BadRequest, "Missing token")
                        return@put
                    }

                    val email = JWebToken.decodeEmailFromToken(token)
                    val user = userInteractor.getUserByEmail(email)
                    LoggerService.debugLog(user.toString())
                    if (user == null) {
                        call.respond(HttpStatusCode.NotFound, "User not found.")
                        return@put
                    }
                    val json = call.receive<JsonObject>()
                    LoggerService.debugLog("Received JSON: $json")

                    val birthdate = json["birthdate"]?.jsonPrimitive?.content
                    val name = json["name"]?.jsonPrimitive?.content ?: user.name
                    val password = json["password"]?.jsonPrimitive?.contentOrNull
                    val zipCode = json["zipCode"]?.jsonPrimitive?.intOrNull ?: user.zipCode
                    val imgBase64 = json["imgBase64"]?.jsonPrimitive?.contentOrNull

                    val updatedDto = UserDto(
                        email = email,
                        name = name,
                        password = password,
                        zipCode = zipCode,
                        birthdate = birthdate ?: user.birthdate.toString(),
                        authProvider = user.authProvider,
                        idToken = token,
                        imgBase64 = imgBase64
                    )

                    val updatedUserDto = userInteractor.updateUser(user.id!!, updatedDto)

                    val response = AuthResponseDto(
                        id = updatedUserDto?.id,
                        token = null,
                        email = updatedUserDto?.email ?: "",
                        name = updatedUserDto?.name,
                        imgUrl = updatedUserDto?.imageUrl,
                        imgBlob = updatedUserDto?.imgBase64
                    )

                    call.respond(HttpStatusCode.OK, response)

                } catch (e: WebserverError) {
                    LoggerService.debugLog("❌ PUT /v1/user/{token} error: $e")
                    call.respond(e.getStatus(), e.getMessage())
                }
            }

        }
    }
}
