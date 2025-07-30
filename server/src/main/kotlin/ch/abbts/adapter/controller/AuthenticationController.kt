package ch.abbts.adapter.controller

import ch.abbts.application.dto.AuthResponseDto
import ch.abbts.application.dto.AuthenticationDto
import ch.abbts.application.dto.SuccessMessage
import ch.abbts.application.dto.UserDto
import ch.abbts.application.interactor.UserInteractor
import ch.abbts.domain.model.AuthProvider
import ch.abbts.domain.model.JWT
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.MissingGoogleToken
import ch.abbts.error.MissingPassword
import ch.abbts.error.WebserverError
import ch.abbts.error.WebserverErrorMessage
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
                mapping =
                    [
                        ResponseEntry("200", JWT::class),
                        ResponseEntry("401", WebserverErrorMessage::class),
                        ResponseEntry("400", WebserverErrorMessage::class),
                        ResponseEntry("500", WebserverErrorMessage::class),
                    ]
            )
            @KtorDescription(
                summary = "Authenticate to receive a JWT Token",
                description =
                    """ This api verifies a users credential or token from google and returns
                a JWT from the server for subsequent api calls."""",
            )
            post("/auth") {
                try {
                    val user = call.receive<AuthenticationDto>()
                    val verifiedUser =
                        when (user.authenticationProvider) {
                            AuthProvider.GOOGLE -> {
                                user.token?.let {
                                    userInteractor.verifyGoogleUser(it)
                                } ?: throw MissingGoogleToken()
                            }

                            AuthProvider.LOCAL -> {
                                user.password?.let {
                                    userInteractor.verifyLocalUser(
                                        user.email,
                                        it,
                                    )
                                } ?: throw MissingPassword()
                            }
                        }

                    LoggerService.debugLog(verifiedUser)

                    if (verifiedUser.id != null) {
                        val token =
                            JWebToken(verifiedUser.email, verifiedUser.id)
                        userInteractor.updateIssuedTime(
                            verifiedUser.email,
                            token.body.iat,
                        )
                        val jwt =
                            JWebToken.generateToken(token.header, token.body)

                        val response =
                            AuthResponseDto(
                                id = verifiedUser.id,
                                token = jwt,
                                email = verifiedUser.email,
                                name = verifiedUser.name,
                                imgUrl = verifiedUser.imageUrl,
                                imgBlob = verifiedUser.imgBase64,
                            )
                        call.respond(HttpStatusCode.OK, response)
                    }
                } catch (e: WebserverError) {
                    LoggerService.debugLog("❌: ${e}")
                    call.respond(e.getStatus(), e.getMessage())
                }
            }

            get("auth/{token}") {
                try {
                    val token = call.parameters["token"]
                    if (token == null) {

                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }

                    val email = JWebToken.decodeEmailFromToken(token)
                    val user = userInteractor.getUserByEmail(email)
                    if (user == null) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }

                    call.respond(HttpStatusCode.OK, UserDto.toDTO(user))
                } catch (e: WebserverError) {
                    LoggerService.debugLog("❌ GET /user/{id} error: ${e}")
                    call.respond(e.getStatus(), e.getMessage())
                }
            }

            authenticate("jwt-auth") {
                @KtorResponds(
                    mapping =
                        [
                            ResponseEntry("200", SuccessMessage::class),
                            ResponseEntry("401", WebserverErrorMessage::class),
                            ResponseEntry("400", WebserverErrorMessage::class),
                            ResponseEntry("500", WebserverErrorMessage::class),
                        ]
                )
                @KtorDescription(
                    summary = "API to verify if a JWT token is valid",
                    description =
                        "This is a debug route to verify if a token is valid",
                )
                get("/validate") {
                    try {
                        val dto = call.receive<UserDto>()
                        LoggerService.debugLog(dto)
                        dto.password?.let {
                            userInteractor.verifyLocalUser(dto.email, it)
                        }
                        call.respond(HttpStatusCode.OK)
                    } catch (e: WebserverError) {
                        LoggerService.debugLog("❌: ${e}")
                        call.respond(e.getStatus(), e.getMessage())
                    }
                }
            }
        }
    }
}
