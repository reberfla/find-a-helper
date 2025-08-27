package ch.abbts.adapter.controller

import ch.abbts.application.dto.AssignmentDto
import ch.abbts.application.interactor.AssignmentInteractor
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.WebserverErrorMessage
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

@Tag(["Assignments"])
@KtorDescription("Routen für das Verwalten von Assignments")
fun Route.assignmentRoutes(assignmentInteractor: AssignmentInteractor) {

    val log = LoggerFactory.getLogger("AssignmentController")

    authenticate("jwt-auth") {
        route("v1/assignments") {
            @KtorDescription("Gibt ein Assignment nach User ID zurück")
            @KtorResponds(
                [
                    ResponseEntry(
                        status = "200",
                        description = "OK",
                        type = AssignmentDto::class,
                    ),
                    ResponseEntry(
                        status = "400",
                        description = "Ungültige Pfad-Variable",
                        type = WebserverErrorMessage::class,
                    ),
                    ResponseEntry(
                        status = "404",
                        description = "Nicht gefunden",
                        type = WebserverErrorMessage::class,
                    ),
                    ResponseEntry(
                        status = "500",
                        description = "Interner Serverfehler",
                        type = WebserverErrorMessage::class,
                    ),
                ]
            )
            get("/my") {
                val id = JWebToken.getUserIdFromCall(call)
                val assignments = assignmentInteractor.getAssignmentByUserId(id)
                call.respond(assignments)
            }

            @KtorDescription("Aktualisiert ein bestehendes Assignment")
            @KtorResponds(
                [
                    ResponseEntry(
                        status = "200",
                        description = "Assignment aktualisiert",
                        type = AssignmentDto::class,
                    ),
                    ResponseEntry(
                        status = "400",
                        description = "Ungültige Eingabe",
                        type = WebserverErrorMessage::class,
                    ),
                    ResponseEntry(
                        status = "401",
                        description = "Nicht autorisiert",
                        type = WebserverErrorMessage::class,
                    ),
                    ResponseEntry(
                        status = "404",
                        description = "Nicht gefunden",
                        type = WebserverErrorMessage::class,
                    ),
                    ResponseEntry(
                        status = "500",
                        description = "Interner Serverfehler",
                        type = WebserverErrorMessage::class,
                    ),
                ]
            )
            put("/{id}") { TODO() }
        }
    }
}
