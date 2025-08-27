package ch.abbts

import ch.abbts.adapter.database.repository.AssignmentRepository
import ch.abbts.adapter.database.repository.OfferRepository
import ch.abbts.adapter.database.repository.TaskRepository
import ch.abbts.adapter.database.repository.UsersRepository
import ch.abbts.adapter.routes.configureOpenApi
import ch.abbts.adapter.routes.configureRouting
import ch.abbts.application.interactor.AssignmentInteractor
import ch.abbts.application.interactor.OfferInteractor
import ch.abbts.application.interactor.TaskInteractor
import ch.abbts.application.interactor.UserInteractor
import ch.abbts.config.DatabaseConfig
import io.github.tabilzad.ktor.annotations.GenerateOpenApi
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*

fun main() {
    embeddedServer(
            Netty,
            host = "0.0.0.0",
            port = 8080,
            module = Application::main,
            watchPaths = listOf("classes", "resources"),
        )
        .start(wait = true)
}

@GenerateOpenApi
fun Application.main() {
    install(CORS) {
        /*for developement*/
        anyHost()
        /*for produktion*/
        /*allowHost("findahelper.ch")
        allowHost("www.findahelper.ch")*/
        allowMethod(io.ktor.http.HttpMethod.Options)
        allowHeader(io.ktor.http.HttpHeaders.ContentType)
        allowHeader(io.ktor.http.HttpHeaders.Authorization)
        allowMethod(io.ktor.http.HttpMethod.Get)
        allowMethod(io.ktor.http.HttpMethod.Post)
        allowMethod(io.ktor.http.HttpMethod.Put)
        allowMethod(io.ktor.http.HttpMethod.Delete)
    }
    DatabaseConfig.init()

    val userInteractor = UserInteractor(UsersRepository())
    val offerInteractor = OfferInteractor(OfferRepository(), TaskRepository())
    val taskInteractor = TaskInteractor(TaskRepository(), UsersRepository())
    val assignmentInteractor =
        AssignmentInteractor(AssignmentRepository(), TaskRepository())

    configureRouting(
        userInteractor,
        offerInteractor,
        taskInteractor,
        assignmentInteractor,
    )
    configureOpenApi()
}
