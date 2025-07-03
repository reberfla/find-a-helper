package ch.abbts.adapter.controller

import ch.abbts.utils.LoggerService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ch.abbts.application.dto.UsersDto
import ch.abbts.application.interactor.UsersInteractor

fun Route.userRoutes(userInteractor: UsersInteractor) {
    route("/users") {
        post("/register") {
            println("in the register api")
            val dto = call.receive<UsersDto>()
            LoggerService.debugLog(dto)

            val success = userInteractor.createLocalUser(dto)
            if (success) {
                call.respond(HttpStatusCode.OK, "yea")
            } else {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }
        }

        route("/auth") {
            post("/local") {
                val success = userInteractor.authenticateLocalUser(call.receive<UsersDto>())
                if (success) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                }
            }
        }
    }
}
