package ch.abbts

import io.github.tabilzad.ktor.annotations.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.swagger.codegen.v3.generators.html.StaticHtmlCodegen
import kotlinx.serialization.Serializable

import ch.abbts.routes.*
import ch.abbts.config.DatabaseConfig

fun main() {
    embeddedServer(Netty, host = "0.0.0.0", port = 8080, module = Application::main, watchPaths = listOf("classes", "resources"))
            .start(wait = true)
}

@GenerateOpenApi
fun Application.main() {
    install(ContentNegotiation) { json() }
    
    configureOpenApi()
    configureTestRoute()

    DatabaseConfig.init()
}
