package ch.abbts.adapter.controller

import ch.abbts.application.dto.ApiResponse
import ch.abbts.application.dto.ApiResponseMessage
import ch.abbts.application.dto.SuccessMessage
import ch.abbts.application.dto.UserDto
import ch.abbts.application.interactor.UserInteractor
import ch.abbts.domain.model.JWebToken
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
import kotlinx.serialization.json.Json

@Tag(["User"])
fun Application.userRoutes(userInteractor: UserInteractor) {
    routing {
        route("/v1/user") {
            @KtorResponds(
                mapping = [
                    ResponseEntry("200", SuccessMessage::class),
                    ResponseEntry("400", WebserverErrorMessage::class),
                    ResponseEntry("500", WebserverErrorMessage::class)
                ]
            )
            @KtorDescription(
                summary = "Creates a new user in the database.",
                description =
                    """ This API creates a new user if it doesn't already exists.
                When creating, a new user id is given by an integer counter of the db""""
            )
            post("/register") {
                try {
                    val dto = call.receive<UserDto>()
                    LoggerService.debugLog("DTO OK: $dto")
                    val user = userInteractor.createLocalUser(dto)
                    if(user != null){
                        val token = JWebToken(user.email)
                        userInteractor.updateIssuedTime(user.email, token.body.iat)
                        val jwt = JWebToken.generateToken(token.header, token.body)
                        LoggerService.debugLog(jwt)

                        call.respond(
                            HttpStatusCode.OK,
                            ApiResponse.from(
                                ApiResponseMessage.REGISTER_SUCCESS,jwt)
                        )
                    } else {
                            call.respond(
                                HttpStatusCode.BadRequest,
                                ApiResponse.from(ApiResponseMessage.INTERNAL_ERROR, null, listOf("Could not create user"))
                            )
                    }

                } catch (e: Exception) {
                    LoggerService.debugLog("❌: ${e.message}")
                }
            }

        }

    }
}
