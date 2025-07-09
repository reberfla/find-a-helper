package ch.abbts.adapter.controller

import ch.abbts.application.dto.SuccessMessage
import ch.abbts.application.dto.UserDto
import ch.abbts.application.interactor.UserInteractor
import ch.abbts.error.WebserverErrorMessage
import ch.abbts.utils.LoggerService
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
                val dto = call.receive<UserDto>()
                LoggerService.debugLog(dto)

                userInteractor.createLocalUser(dto)
                call.respond(SuccessMessage())
            }
        }

    }
}
