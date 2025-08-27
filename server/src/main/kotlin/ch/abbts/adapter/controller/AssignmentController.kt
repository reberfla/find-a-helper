package ch.abbts.adapter.controller

import ch.abbts.application.dto.AssignmentDto
import ch.abbts.application.dto.AssignmentUpdateDto
import ch.abbts.application.dto.SuccessMessage
import ch.abbts.application.interactor.AssignmentInteractor
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.InvalidPathParamInt
import ch.abbts.error.WebserverErrorMessage
import ch.abbts.utils.receiveHandled
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
        route("v1/assignment") {
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
            put("/{id}") {
                val userId = JWebToken.getUserIdFromCall(call)
                val assignmentId = call.parameters["id"]
                if (assignmentId != null) {
                    val assignment = call.receiveHandled<AssignmentUpdateDto>()
                    log.info(
                        "${call.route} updating properties $assignment for assignment with id = $assignmentId"
                    )
                    val intId =
                        assignmentId.toIntOrNull()
                            ?: throw InvalidPathParamInt("id", assignmentId)
                    assignmentInteractor.updateAssignment(
                        assignment,
                        intId,
                        userId,
                    )
                    log.info(
                        "${call.route} updated properties $assignment for task with id = $assignmentId"
                    )
                    call.respond(SuccessMessage())
                }
            }
        }
    }
}
