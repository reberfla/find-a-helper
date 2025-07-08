package ch.abbts.adapter.controller

import ch.abbts.utils.LoggerService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ch.abbts.application.dto.UsersDto
import ch.abbts.application.dto.SuccessMessage
import ch.abbts.error.WebserverErrorMessage
import ch.abbts.application.interactor.UsersInteractor
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.github.tabilzad.ktor.annotations.Tag

@Tag(["User"])
fun Route.userRoutes(userInteractor: UsersInteractor) {
    route("/v1/user") {
        @KtorResponds(mapping = [
        ResponseEntry("200", SuccessMessage::class),
        ResponseEntry("400", WebserverErrorMessage::class),
        ResponseEntry("500", WebserverErrorMessage::class)
        ])
        @KtorDescription(
            summary = "Creates a new user in the database.",
            description =
                """ This API creates a new user if it doesn't already exists.
                When creating, a new user id is given by an integer counter of the db""""
        )
        post("/register") {
            val dto = call.receive<UsersDto>()
            LoggerService.debugLog(dto)

            userInteractor.createLocalUser(dto)
            call.respond(SuccessMessage())
        }
    }
}
