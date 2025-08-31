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
import ch.abbts.utils.receiveHandled
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Tag(["Offer"])
fun Application.offerRoutes(
    offerInteractor: OfferInteractor,
    userInteractor: UserInteractor,
    taskInteractor: TaskInteractor,
) {
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
                        val dto = call.receiveHandled<OfferDto>()
                        val userId = JWebToken.getUserIdFromCall(call)

                        if (userId != dto.userId) {
                            throw OfferForbidden()
                        }

                        val createdOffer = offerInteractor.createOffer(dto)
                        call.respond(
                            HttpStatusCode.Created,
                            createdOffer as OfferDto,
                        )
                    } catch (e: WebserverError) {
                        call.respond(e.getStatus(), e.getMessage())
                    }
                }

                put("/accept/{id}") {
                    val offerId = call.parameters["id"]!!.toInt()
                    val currentUserId = JWebToken.getUserIdFromCall(call)
                    call.respond(
                        offerInteractor.acceptOffer(offerId, currentUserId)
                    )
                }

                put("/reject/{id}") {
                    val offerId = call.parameters["id"]!!.toInt()
                    val offer = offerInteractor.getOfferById(offerId)
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
