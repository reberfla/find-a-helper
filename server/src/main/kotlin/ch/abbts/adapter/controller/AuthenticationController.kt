package ch.abbts.adapter.controller

import ch.abbts.application.dto.AuthenticationDto
import ch.abbts.application.interactor.UsersInteractor
import ch.abbts.domain.model.AuthProvider
import ch.abbts.domain.model.JWT
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.MissingGoogleToken
import ch.abbts.error.MissingPassword
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.JsonObject

@Tag(["Authentication"])
fun Route.authenticationRoutes(usersInteractor: UsersInteractor) {
    route("/v1") {
        @Tag(["Auth"])
        @KtorResponds(mapping = [ResponseEntry("200", JWT::class)])
        @KtorDescription(
            tags = ["Auth"],
            summary = "Authenticate to receive a JWT Token",
            description =
                """ This api verifies a users credential or token from google and returns
                a JWT from the server for subsequent api calls.""""
        )
        post("/auth") {
            val user = call.receive<AuthenticationDto>()
            val success =
                when (user.authenticationProvider) {
                    AuthProvider.GOOGLE -> if (user.token != null) {
                        usersInteractor.verifyGoogleUser(user.token)
                    } else {
                        throw MissingGoogleToken()
                    }

                    AuthProvider.LOCAL -> if (user.password != null) {
                        usersInteractor.verifyLocalUser(user.email, user.password)
                    } else {
                        throw MissingPassword()
                    }
                }
            if (success) {
                val token = JWebToken(user.email)
                println("updating timestamp to ${token.body.iat}")
                usersInteractor.updateIssuedTime(user.email, token.body.iat)
                call.respond(JWebToken.generateToken(token.header, token.body))
            }
            call.respond(HttpStatusCode.Unauthorized, buildJsonObject {
                put("message", "authorization failed")
            })
        }
        authenticate("jwt-auth") {
            @Tag(["Auth"])
            @KtorResponds(mapping = [ResponseEntry("200", JsonObject::class)])
            @KtorDescription(
                tags = ["Auth"],
                summary = "API to verify if a JWT token is valid",
                description = "This is a debug route to verify if a token is valid"
            )
            get("/validate") {
                call.respond(buildJsonObject { put("message", "validation successful") })
            }
        }
    }
}
