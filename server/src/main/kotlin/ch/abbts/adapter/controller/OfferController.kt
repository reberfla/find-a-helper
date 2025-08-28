package ch.abbts.adapter.controller

import ch.abbts.application.dto.OfferDto
import ch.abbts.application.dto.toWithTaskDto
import ch.abbts.application.interactor.OfferInteractor
import ch.abbts.application.interactor.TaskInteractor
import ch.abbts.application.interactor.UserInteractor
import ch.abbts.domain.model.JWebToken
import ch.abbts.domain.model.OfferStatus
import ch.abbts.error.OfferForbidden
import ch.abbts.error.WebserverError
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

@Tag(["Offer"])
fun Application.offerRoutes(
    offerInteractor: OfferInteractor,
    userInteractor: UserInteractor,
    taskInteractor: TaskInteractor,
) {
    val log = LoggerFactory.getLogger("ch.abbts.adapter.controller")
    routing {
        route("/v1/offer") {
            authenticate("jwt-auth") {
                get("/id") {
                    val id = call.parameters["id"]!!.toInt()
                    call.respond(offerInteractor.getOfferById(id))
                }

                get("/my") {
                    val token = call.request.authorization()!!.split(" ")[1]
                    val id = JWebToken.getUserIdFromToken(token)
                    val offers = offerInteractor.getOffersByCreator(id)

                    val result =
                        offers.map { offer ->
                            val task = taskInteractor.getTaskById(offer.taskId)
                            offer.toWithTaskDto(task)
                        }
                    call.respond(HttpStatusCode.OK, result)
                }

                get("/task/{id}") {
                    val id = call.parameters["id"]!!.toInt()
                    call.respond(offerInteractor.getOffersForTask(id))
                }

                post {
                    try {
                        val dto = call.receive<OfferDto>()
                        val token = call.request.authorization()!!.split(" ")[1]
                        val email = JWebToken.decodeEmailFromToken(token)
                        val user = userInteractor.getUserByEmail(email)
                        log.debug("user of dto: ${dto.userId}")
                        log.debug("user of request: ${user?.id}")

                        if (user!!.id != dto.userId) {
                            throw OfferForbidden()
                        }

                        val createdOffer = offerInteractor.createOffer(dto)

                        val response =
                            createdOffer?.id?.let {
                                OfferDto(
                                    id = createdOffer.id,
                                    userId = createdOffer.userId,
                                    taskId = createdOffer.taskId,
                                    status = OfferStatus.SUBMITTED,
                                    active = true,
                                    text = createdOffer.text,
                                    title = createdOffer.title,
                                )
                            }

                        if (response != null) {
                            call.respond(HttpStatusCode.Created, response)
                        }
                    } catch (e: WebserverError) {
                        call.respond(e.getStatus(), e.getMessage())
                    }
                }

                put("/accept/{id}") {
                    val offerId = call.parameters["id"]!!.toInt()
                    val userId = JWebToken.getUserIdFromCall(call)
                    call.respond(offerInteractor.acceptOffer(offerId, userId))
                }

                put("/reject/{id}") {
                    val offerId = call.parameters["id"]!!.toInt()
                    offerInteractor.getOfferById(offerId)
                    call.respond(
                        offerInteractor.setOfferStatus(
                            offerId,
                            OfferStatus.REJECTED,
                        )
                    )
                }

                delete("/{id}") {
                    try {
                        val id =
                            call.parameters["id"]?.toIntOrNull()
                                ?: return@delete call.respond(
                                    HttpStatusCode.BadRequest,
                                    "Missing ID",
                                )

                        val token = call.request.authorization()!!.split(" ")[1]
                        val email = JWebToken.decodeEmailFromToken(token)
                        val user = userInteractor.getUserByEmail(email)

                        user?.id?.let { offerInteractor.deleteOffer(it, id) }
                        call.respond(
                            HttpStatusCode.OK,
                            mapOf("message" to "Offer deleted"),
                        )
                    } catch (e: WebserverError) {
                        call.respond(e.getStatus(), e.getMessage())
                    }
                }
            }
        }
    }
}
