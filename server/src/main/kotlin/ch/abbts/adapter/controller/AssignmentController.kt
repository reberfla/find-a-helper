package ch.abbts.adapter.controller

import ch.abbts.application.dto.AssignmentDto
import ch.abbts.application.dto.SuccessMessage
import ch.abbts.application.interactor.AssignmentInteractor
import ch.abbts.domain.model.JWebToken
import ch.abbts.domain.model.AssignmentModel
import ch.abbts.error.InvalidPathParamInt
import ch.abbts.error.MissingPathParam
import ch.abbts.error.WebserverErrorMessage
import ch.abbts.utils.receiveHandled
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

@Tag(name = "Assignments")
@KtorDescription("Routen für das Verwalten von Assignments")
fun Route.assignmentRoutes(assignmentInteractor: AssignmentInteractor) {

    val log = LoggerFactory.getLogger("AssignmentController")

    authenticate {
        route("/assignments") {

            @KtorDescription("Erstellt ein neues Assignment")
            @KtorResponds(
                ResponseEntry(code = 201, description = "Assignment erstellt", example = AssignmentDto::class),
                ResponseEntry(code = 400, description = "Ungültige Eingabe", example = WebserverErrorMessage::class),
                ResponseEntry(code = 401, description = "Nicht autorisiert", example = WebserverErrorMessage::class),
                ResponseEntry(code = 500, description = "Interner Serverfehler", example = WebserverErrorMessage::class),
            )
            post {
                val principal = call.principal<JWebToken>()
                if (principal == null) {
                    call.respond(HttpStatusCode.Unauthorized, WebserverErrorMessage("Nicht autorisiert"))
                    return@post
                }

                val dto = call.receiveHandled<AssignmentDto>()
                val model = dto.toModel(userId = principal.userId)

                val created = assignmentInteractor.createAssignment(model)
                call.respond(HttpStatusCode.Created, created.toDto())
            }

            @KtorDescription("Liefert ein Assignment nach ID")
            @KtorResponds(
                ResponseEntry(code = 200, description = "OK", example = AssignmentDto::class),
                ResponseEntry(code = 400, description = "Ungültige Pfad-Variable", example = WebserverErrorMessage::class),
                ResponseEntry(code = 404, description = "Nicht gefunden", example = WebserverErrorMessage::class),
                ResponseEntry(code = 500, description = "Interner Serverfehler", example = WebserverErrorMessage::class),
            )
            get("/{id}") {
                val id = call.parameters["id"] ?: throw MissingPathParam("id")
                val idInt = id.toIntOrNull() ?: throw InvalidPathParamInt("id")

                val model = assignmentInteractor.getById(idInt)
                call.respond(HttpStatusCode.OK, model.toDto())
            }

            @KtorDescription("Aktualisiert ein bestehendes Assignment")
            @KtorResponds(
                ResponseEntry(code = 200, description = "Assignment aktualisiert", example = AssignmentDto::class),
                ResponseEntry(code = 400, description = "Ungültige Eingabe", example = WebserverErrorMessage::class),
                ResponseEntry(code = 401, description = "Nicht autorisiert", example = WebserverErrorMessage::class),
                ResponseEntry(code = 404, description = "Nicht gefunden", example = WebserverErrorMessage::class),
                ResponseEntry(code = 500, description = "Interner Serverfehler", example = WebserverErrorMessage::class),
            )
            put("/{id}") {
                val principal = call.principal<JWebToken>()
                if (principal == null) {
                    call.respond(HttpStatusCode.Unauthorized, WebserverErrorMessage("Nicht autorisiert"))
                    return@put
                }

                val id = call.parameters["id"] ?: throw MissingPathParam("id")
                val idInt = id.toIntOrNull() ?: throw InvalidPathParamInt("id")

                val dto = call.receiveHandled<AssignmentDto>()
                val model = dto.toModel(userId = principal.userId).copy(id = idInt)

                val updated = assignmentInteractor.updateAssignment(model, principal.userId, idInt)
                call.respond(HttpStatusCode.OK, updated.toDto())
            }

            @KtorDescription("Löscht ein Assignment")
            @KtorResponds(
                ResponseEntry(code = 200, description = "Assignment gelöscht", example = SuccessMessage::class),
                ResponseEntry(code = 400, description = "Ungültige Pfad-Variable", example = WebserverErrorMessage::class),
                ResponseEntry(code = 401, description = "Nicht autorisiert", example = WebserverErrorMessage::class),
                ResponseEntry(code = 404, description = "Nicht gefunden", example = WebserverErrorMessage::class),
                ResponseEntry(code = 500, description = "Interner Serverfehler", example = WebserverErrorMessage::class),
            )
            delete("/{id}") {
                val principal = call.principal<JWebToken>()
                if (principal == null) {
                    call.respond(HttpStatusCode.Unauthorized, WebserverErrorMessage("Nicht autorisiert"))
                    return@delete
                }

                val id = call.parameters["id"] ?: throw MissingPathParam("id")
                val idInt = id.toIntOrNull() ?: throw InvalidPathParamInt("id")

                assignmentInteractor.deleteAssignment(idInt, principal.userId)
                call.respond(HttpStatusCode.OK, SuccessMessage("Assignment mit ID $idInt gelöscht"))
            }
        }
    }
}

private fun AssignmentDto.toModel(userId: Int): AssignmentModel =
    AssignmentModel(
        id = this.id,
        userId = userId,
        taskId = this.taskId,
        offerId = this.offerId,
        createdAt = this.createdAt,
        status = this.status,
        active = this.active
    )

private fun AssignmentModel.toDto(): AssignmentDto =
    AssignmentDto(
        id = this.id ?: 0,
        taskId = this.taskId,
        offerId = this.offerId,
        createdAt = this.createdAt,
        status = this.status,
        active = this.active
    )