package ch.abbts.adapter.controller

import ch.abbts.application.dto.SuccessMessage
import ch.abbts.application.dto.TaskQueryParams
import ch.abbts.application.interactor.TaskInteractor
import ch.abbts.domain.model.JWebToken
import ch.abbts.domain.model.TaskModel
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
                    call.respond(taskInteractor.getTasks(params))
                }
            }
            get("/{id}") {
                val id = call.parameters["id"]!!.toInt()
                log.debug("GET request with id: $id")
                call.respond(taskInteractor.getTaskById(id))
            }
            authenticate("jwt-auth") {
                get("/my") {
                    val token = call.request.authorization()!!.split(" ")[1]
                    val id = JWebToken.getUserIdFromToken(token)
                    log.debug("$id")
                    call.respond(taskInteractor.getTasksByCreator(id))
                }

                post {
                    val task = call.receive<TaskModel>()
                    log.debug(task.toString())
                    call.respond(taskInteractor.createTask(task))
                }
                put("/{id}") {
                    val id = call.parameters["id"]
                    log.debug("PUT request with id: $id")
                }

                delete("/{id}") {
                    val id = call.parameters["id"]!!.toInt()
                    taskInteractor.deleteTask(id)
                    call.respond(SuccessMessage())
                }
            }
        }
    }
}
