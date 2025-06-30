package ch.abbts

import adapter.database.repository.usersRepository
import config.DatabaseConfig
import adapter.routes.configureRouting
import adapter.routes.configureOpenApi
import io.github.tabilzad.ktor.annotations.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import application.interactor.usersInteractor


fun main() {
    embeddedServer(
        Netty,
        host = "0.0.0.0",
        port = 8080,
        module = Application::main,
        watchPaths = listOf("classes", "resources")
    )
        .start(wait = true)
}

@GenerateOpenApi
fun Application.main() {
    install(ContentNegotiation) { json() }
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

    val userInteractor = usersInteractor(usersRepository())

    configureRouting(userInteractor)
    configureOpenApi()

}
