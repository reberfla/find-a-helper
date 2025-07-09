package ch.abbts.adapter.controller

import ch.abbts.application.dto.AuthenticationDto
import ch.abbts.application.dto.SuccessMessage
import ch.abbts.application.interactor.UserInteractor
import ch.abbts.domain.model.AuthProvider
import ch.abbts.domain.model.JWT
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.MissingGoogleToken
import ch.abbts.error.MissingPassword
import ch.abbts.error.WebserverErrorMessage
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.github.tabilzad.ktor.annotations.Tag
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
                call.respond(JWebToken.generateToken(token.header, token.body))
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
                    call.respond(SuccessMessage())
                }
            }
        }

    }
}
