package adapter.routes

import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.JWebToken

@Tag(["Authentication"])
fun Application.configureAuthenticationRoutes() {
    routing {
        route("/v1"){
            @Tag(["Auth"])
            @KtorResponds(mapping = [ResponseEntry("200", JWebToken::class)])
            @KtorDescription(
                tags = ["Auth"],
                summary = "Authenticate to receive a JWT Token",
                description =
                """ This api verifies a users credential or token from google and returns
                a JWT from the server for subsequent api calls.""""
            )
            post("/auth") { call.respond("Hello") }
        }
    }
}
