package ch.abbts.adapter.routes

import ch.abbts.adapter.controller.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import usersInteractor
import java.lang.ModuleLayer.Controller

fun Application.configureRouting(
    usersInteractor: usersInteractor,

    ) {
    routing {
        userRoutes(usersInteractor)
    }
}
