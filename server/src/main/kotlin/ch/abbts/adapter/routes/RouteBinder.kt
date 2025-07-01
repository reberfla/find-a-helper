package ch.abbts.adapter.routes

import ch.abbts.adapter.controller.authenticationRoutes
import ch.abbts.application.interactor.usersInteractor
import ch.abbts.adapter.controller.userRoutes
import ch.abbts.error.AuthenticationException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.*

fun Application.configureRouting(
        usersInteractor: usersInteractor,
) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is AuthenticationException ->
                        call.respond(status = HttpStatusCode.Unauthorized, buildJsonObject{
                            put("message", cause.message)
                        })
                else ->
                        call.respond(
                            status = HttpStatusCode.InternalServerError,
                                buildJsonObject {
                                put("message", "something went wrong")}
                        )
            }
        }
    }
    routing { userRoutes(usersInteractor) }
    routing { authenticationRoutes(usersInteractor) }
}
