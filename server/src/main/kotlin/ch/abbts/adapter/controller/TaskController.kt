package ch.abbts.adapter.controller

import ch.abbts.application.dto.SuccessMessage
import ch.abbts.application.dto.TaskDto
import ch.abbts.application.dto.TaskQueryParams
import ch.abbts.application.interactor.TaskInteractor
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.InvalidPathParam
import ch.abbts.error.MissingPathParam
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

@Tag(["Task"])
fun Application.taskRoutes(taskInteractor: TaskInteractor) {
    val log = LoggerFactory.getLogger(object {}::class.java.`package`.name)
    routing {
        route("/v1/task") {
            get {
                if (call.queryParameters.isEmpty()) {
                    log.debug("no query params set")
                    call.respond(taskInteractor.getTasks())
                } else {
                    val params = TaskQueryParams(call.queryParameters)
                    log.debug("params: ${params.toString()}")
                    call.respond(
                        taskInteractor.getTasks(params).map { it.toPublicDto() }
                    )
                }
            }
            get("/{id}") {
                val id = call.parameters["id"]
                if (id != null) {
                    log.debug("GET request with id: $id")
                    val intId =
                        id.toIntOrNull() ?: throw InvalidPathParam("id", id)
                    call.respond(
                        taskInteractor.getTaskById(intId).toPublicDto()
                    )
                } else {
                    throw MissingPathParam("id")
                }
            }
            authenticate("jwt-auth") {
                get("/my") {
                    val userId = JWebToken.getUserIdFromCall(call)
                    log.debug("$userId")
                    call.respond(taskInteractor.getTasksByCreator(userId))
                }

                post {
                    val task = call.receive<TaskDto>()
                    log.debug("$task")
                    val userId = JWebToken.getUserIdFromCall(call)
                    log.debug("got id $userId")
                    call.respond(
                        taskInteractor.createTask(task.toModel(userId))
                    )
                }
                put("/{id}") {
                    val userId = JWebToken.getUserIdFromCall(call)
                    val taskId = call.parameters["id"]
                    if (taskId != null) {
                        val task = call.receive<TaskDto>()
                        log.debug(task.toString())
                        val intId =
                            taskId.toIntOrNull()
                                ?: throw InvalidPathParam("id", taskId)
                        call.respond(
                            taskInteractor.updateTask(task, userId, intId)
                        )
                    } else {
                        throw MissingPathParam("id")
                    }
                }

                delete("/{id}") {
                    val taskId = call.parameters["id"]
                    if (taskId != null) {
                        val intId =
                            taskId.toIntOrNull()
                                ?: throw InvalidPathParam("id", taskId)
                        taskInteractor.deleteTask(intId)
                        call.respond(SuccessMessage())
                    } else {
                        throw MissingPathParam("id")
                    }
                }
            }
        }
    }
}
