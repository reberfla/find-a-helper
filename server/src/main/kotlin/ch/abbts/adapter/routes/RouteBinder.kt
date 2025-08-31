package ch.abbts.adapter.routes

import ch.abbts.adapter.controller.assignmentRoutes
import ch.abbts.adapter.controller.authenticationRoutes
import ch.abbts.adapter.controller.offerRoutes
import ch.abbts.adapter.controller.taskRoutes
import ch.abbts.adapter.controller.userRoutes
import ch.abbts.application.interactor.AssignmentInteractor
import ch.abbts.application.interactor.OfferInteractor
import ch.abbts.application.interactor.TaskInteractor
import ch.abbts.application.interactor.UserInteractor
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.WebserverError
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.slf4j.LoggerFactory

fun Application.configureRouting(
    userInteractor: UserInteractor,
    offerInteractor: OfferInteractor,
    taskInteractor: TaskInteractor,
    assignmentInteractor: AssignmentInteractor,
) {
    val log = LoggerFactory.getLogger(object {}::class.java.`package`.name)
    install(StatusPages) {
        exception<Throwable> { call, error ->
            when (error) {
                is WebserverError -> {
                    log.info("${error.message}")
                    call.respond(status = error.getStatus(), error.getMessage())
                }
                else -> {
                    log.error("${error.message}")
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message =
                            buildJsonObject {
                                put("message", "something went wrong")
                            },
                    )
                }
            }
        }
    }
    install(ContentNegotiation) { json() }
    authentication {
        bearer("jwt-auth") {
            realm = "Access to protected routes"
            authenticate { jwt ->
                JWebToken.validateToken(jwt.token)
                JWebToken.verifyToken(jwt.token)
            }
        }
    }
    routing { userRoutes(userInteractor) }
    routing { authenticationRoutes(userInteractor) }
    routing { offerRoutes(offerInteractor, userInteractor, taskInteractor) }
    routing { taskRoutes(taskInteractor) }
    routing { assignmentRoutes(assignmentInteractor) }
}
