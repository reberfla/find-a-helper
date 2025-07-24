package ch.abbts.adapter.controller

import ch.abbts.application.dto.SuccessMessage
import ch.abbts.application.dto.TaskDto
import ch.abbts.application.dto.TaskQueryParams
import ch.abbts.application.dto.TaskUpdateDto
import ch.abbts.application.interactor.TaskInteractor
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.InvalidPathParamInt
import ch.abbts.error.MissingPathParam
import ch.abbts.utils.receiveHandled
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
                    log.info("${call.route} with no query params set")
                    call.respond(taskInteractor.getTasks())
                } else {
                    val params = TaskQueryParams(call.queryParameters)
                    log.info("${call.route} with query = ${params.toString()}")
                    call.respond(
                        taskInteractor.getTasks(params).map { it.toPublicDto() }
                    )
                }
            }
            get("/{id}") {
                val id = call.parameters["id"]
                if (id != null) {
                    log.info("${call.route}".replace("{id}", id))
                    val intId =
                        id.toIntOrNull() ?: throw InvalidPathParamInt("id", id)
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
                    log.info("${call.route} for userId = $userId")
                    call.respond(taskInteractor.getTasksByCreator(userId))
                }

                post {
                    val task = call.receiveHandled<TaskDto>()
                    val userId = JWebToken.getUserIdFromCall(call)
                    log.info(
                        "${call.route} creating task under userId = $userId"
                    )
                    val createdTask =
                        taskInteractor.createTask(task.toModel(userId))
                    log.info(
                        "${call.route} task with id = ${createdTask.id} created"
                    )
                    call.respond(createdTask)
                }
                put("/{id}") {
                    val userId = JWebToken.getUserIdFromCall(call)
                    val taskId = call.parameters["id"]
                    if (taskId != null) {
                        val task = call.receiveHandled<TaskUpdateDto>()
                        log.info(
                            "${call.route} updating properties $task for task with id = $taskId"
                        )
                        val intId =
                            taskId.toIntOrNull()
                                ?: throw InvalidPathParamInt("id", taskId)
                        val updatedTask =
                            taskInteractor.updateTask(task, userId, intId)
                        log.info(
                            "${call.route} updated properties $task for task with id = $taskId"
                        )
                        call.respond(updatedTask)
                    } else {
                        throw MissingPathParam("id")
                    }
                }

                delete("/{id}") {
                    val taskId = call.parameters["id"]
                    if (taskId != null) {
                        val intId =
                            taskId.toIntOrNull()
                                ?: throw InvalidPathParamInt("id", taskId)
                        log.info(
                            "${call.route} deleting task with id = $taskId"
                        )
                        taskInteractor.deleteTask(intId)
                        log.info(
                            "${call.route} deleted task with id = $taskId successfully"
                        )
                        call.respond(SuccessMessage())
                    } else {
                        throw MissingPathParam("id")
                    }
                }
            }
        }
    }
}
