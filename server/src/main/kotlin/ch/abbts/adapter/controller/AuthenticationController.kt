package adapter.controller

import application.dto.AuthenticationDto
import application.interactor.usersInteractor
import domain.model.AuthProvider
import domain.model.JWebToken
import domain.model.JWT
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Tag(["Authentication"])
fun Route.authenticationRoutes(usersInteractor: usersInteractor) {
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
            val success = when (user.authenticationProvider) {
                AuthProvider.GOOGLE -> usersInteractor.verifyGoogleUser(user.token!!)
                AuthProvider.LOCAL -> usersInteractor.verifyLocalUser(user.email, user.passwordHash!!)
            }
            if (success) {
                val token = JWebToken(user.email)
                println("updating timestamp to ${token.body.iat}")
                usersInteractor.updateIssuedTime(user.email, token.body.iat)
                call.respond(JWebToken.generateToken(token.header, token.body))
            }
            call.respond(HttpStatusCode.Unauthorized, "authorization failed")
            // }
        }
        post("/validate"){
            val token = call.receive<JWT>()
            JWebToken.verifyToken(token.JWT)
            JWebToken.validateToken(token.JWT)
            call.respond("validation Successful")
        }
    }
}
