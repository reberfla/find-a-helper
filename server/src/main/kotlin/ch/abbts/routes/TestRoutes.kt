package ch.abbts.routes

import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.github.tabilzad.ktor.annotations.Tag

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import ch.abbts.models.Test

@Tag(["All Endpoints"])
fun Application.configureTestRoute() {
    routing {
        @Tag(["Test"])
        @KtorResponds(mapping = [ResponseEntry("200", Test::class)])
        @KtorDescription(
            tags = ["Test"],
            summary = "Testing Annotations",
            description = "This is just a test for using the swagger annotations."
        )
        get("test") { call.respond(Test("Hello World")) }
    }
}
