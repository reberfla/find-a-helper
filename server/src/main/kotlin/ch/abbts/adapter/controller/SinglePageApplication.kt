package ch.abbts.adapter.controller

import io.ktor.server.application.Application
import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.http.content.vue
import io.ktor.server.routing.routing

fun Application.serveSinglePageApplication() {
    routing { singlePageApplication { vue("src/main/vue/dist") } }
}
