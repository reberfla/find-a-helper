package ch.abbts.adapter.controller

import ch.abbts.utils.LoggerService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ch.abbts.application.dto.UsersDto
import ch.abbts.application.interactor.UsersInteractor
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

fun Route.userRoutes(userInteractor: UsersInteractor) {
    route("/v1/user") {
        post("/register") {
            val dto = call.receive<UsersDto>()
            LoggerService.debugLog(dto)

            userInteractor.createLocalUser(dto)
            call.respond(HttpStatusCode.OK, buildJsonObject {
                put("message", "success")
            })
        }
    }
}
