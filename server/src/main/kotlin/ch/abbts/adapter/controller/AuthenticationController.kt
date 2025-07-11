package ch.abbts.adapter.controller

import ch.abbts.application.dto.*
import ch.abbts.application.interactor.UserInteractor
import ch.abbts.domain.model.AuthProvider
import ch.abbts.domain.model.JWT
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.*
import ch.abbts.utils.LoggerService
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Tag(["Authentication"])
fun Application.authenticationRoutes(userInteractor: UserInteractor) {
    routing {
        route("/v1") {
            @KtorResponds(
                mapping = [
                    ResponseEntry("200", JWT::class),
                    ResponseEntry("401", WebserverErrorMessage::class),
                    ResponseEntry("400", WebserverErrorMessage::class),
                    ResponseEntry("500", WebserverErrorMessage::class)
                ]
            )
            @KtorDescription(
                summary = "Authenticate to receive a JWT Token",
                description =
                    """ This api verifies a users credential or token from google and returns
                a JWT from the server for subsequent api calls.""""
            )
            post("/auth") {
                try{
                    val user = call.receive<AuthenticationDto>()
                    log.debug("verifying local user with email: ${user.email}")
                    when (user.authenticationProvider) {
                        AuthProvider.GOOGLE -> if (user.token != null) {
                            userInteractor.verifyGoogleUser(user.token)
                        } else {
                            throw MissingGoogleToken()
                        }

                        AuthProvider.LOCAL -> if (user.password != null) {
                            userInteractor.verifyLocalUser(user.email, user.password)
                        } else {
                            throw MissingPassword()
                        }
                    }
                    val token = JWebToken(user.email)
                    userInteractor.updateIssuedTime(user.email, token.body.iat)
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse.from(ApiResponseMessage.LOGIN_SUCCESS, JWebToken.generateToken(token.header, token.body))
                    )

                }catch (e: Throwable) {
                    LoggerService.debugLog("❌: ${e.message}")

                    val (status, response) = when (e) {
                        is InvalidCredentials -> HttpStatusCode.BadRequest to ApiResponse.from(ApiResponseMessage.INVALID_CREDENTIALS, null, listOf(e.message ?: "Ungültig"))
                        is UserNotFound -> HttpStatusCode.NotFound to ApiResponse.from(ApiResponseMessage.NOT_FOUND, null, listOf(e.message ?: "Benutzer nicht gefunden"))
                        is UserIsLocked -> HttpStatusCode.Unauthorized to ApiResponse.from(ApiResponseMessage.AUTH_FAILED, null, listOf("Benutzer ist gesperrt"))
                        is MissingPassword, is MissingGoogleToken -> HttpStatusCode.BadRequest to ApiResponse.from(ApiResponseMessage.INVALID_CREDENTIALS, null, listOf("Fehlende Zugangsdaten"))
                        else -> HttpStatusCode.InternalServerError to ApiResponse.from(ApiResponseMessage.INTERNAL_ERROR, null, listOf(e.message ?: "Unbekannter Fehler"))
                    }

                    call.respond(status, response)
                }


            }
            authenticate("jwt-auth") {
                @KtorResponds(
                    mapping = [
                        ResponseEntry("200", SuccessMessage::class),
                        ResponseEntry("401", WebserverErrorMessage::class),
                        ResponseEntry("400", WebserverErrorMessage::class),
                        ResponseEntry("500", WebserverErrorMessage::class)
                    ]
                )
                @KtorDescription(
                    summary = "API to verify if a JWT token is valid",
                    description = "This is a debug route to verify if a token is valid"
                )
                get("/validate") {
                    try {
                        val dto = call.receive<UserDto>()
                        LoggerService.debugLog(dto)
                        dto.password?.let { userInteractor.verifyLocalUser(dto.email, it) }

                        call.respond(
                            HttpStatusCode.fromValue(ApiResponseMessage.LOGIN_SUCCESS.status),
                            ApiResponse.from(ApiResponseMessage.LOGIN_SUCCESS, null)
                        )
                    } catch (e: Exception) {
                        LoggerService.debugLog("❌: ${e.message}")
                    }

                }
            }
        }

    }
}
