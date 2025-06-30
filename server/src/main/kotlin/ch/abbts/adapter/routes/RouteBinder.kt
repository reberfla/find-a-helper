package adapter.routes

import ch.abbts.adapter.controller.userRoutes
import adapter.controller.authenticationRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import application.interactor.usersInteractor
import java.lang.ModuleLayer.Controller

fun Application.configureRouting(
    usersInteractor: usersInteractor,

    ) {
    routing {
        userRoutes(usersInteractor)
    }
    routing {
        authenticationRoutes(usersInteractor)
    }
}
