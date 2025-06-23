package ch.abbts.adapter.controller

import ch.abbts.utils.Log
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.exposedLogger
import usersDto
import usersInteractor

fun Route.userRoutes(userInteractor: usersInteractor) {
    route("/users") {
        println("hallo" )
        get("/auth"){
            call.respond(HttpStatusCode.OK, "yea")
        }

        post("/register") {
            println("here")

            val success = userInteractor.createLocalUser(call.receive<usersDto>())
            if (success) {
                call.respond(HttpStatusCode.OK, "yea")
            } else {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }
        }

        route("/auth") {
            post("/local") {
                val success = userInteractor.authenticateLocalUser(call.receive<usersDto>())
                if (success) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                }
            }
        }
    }

}
