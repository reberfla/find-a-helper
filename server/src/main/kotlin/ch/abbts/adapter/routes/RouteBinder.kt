package ch.abbts.adapter.routes

import ch.abbts.adapter.controller.authenticationRoutes
import ch.abbts.adapter.controller.userRoutes
import ch.abbts.application.interactor.UsersInteractor
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.AuthenticationException
import ch.abbts.error.MissingGoogleToken
import ch.abbts.error.MissingPassword
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.*

fun Application.configureRouting(
    usersInteractor: UsersInteractor,
) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is MissingPassword -> call.respond(status = HttpStatusCode.BadRequest, buildJsonObject { put("message", cause.message) })
                is MissingGoogleToken -> call.respond(status = HttpStatusCode.BadRequest, buildJsonObject { put("message", cause.message) })
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
