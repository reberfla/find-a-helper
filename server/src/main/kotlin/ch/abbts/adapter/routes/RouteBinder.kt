package ch.abbts.adapter.routes

import ch.abbts.adapter.controller.authenticationRoutes
import ch.abbts.adapter.controller.userRoutes
import ch.abbts.application.interactor.usersInteractor
import ch.abbts.domain.model.JWT
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.AuthenticationException
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

fun Application.configureRouting(
    usersInteractor: usersInteractor,
) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is AuthenticationException ->
                    call.respond(status = HttpStatusCode.Unauthorized, buildJsonObject {
                        put("message", cause.message)
                    })

                else ->
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        buildJsonObject {
                            put("message", "something went wrong")
                        }
                    )
            }
        }
    }
    authentication {
        bearer("jwt-auth") {
            realm = "Access to protected routes"
            authenticate { jwt ->
                JWebToken.validateToken(jwt.token)
                JWebToken.verifyToken(jwt.token)
            }
        }
    }
    routing { userRoutes(usersInteractor) }
    routing { authenticationRoutes(usersInteractor) }
}
