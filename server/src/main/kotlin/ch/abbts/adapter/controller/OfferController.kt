package ch.abbts.adapter.controller

import ch.abbts.application.dto.OfferDto
import ch.abbts.application.interactor.OfferInteractor
import ch.abbts.application.interactor.UserInteractor
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.OfferForbidden
import ch.abbts.error.WebserverError
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.offerRoutes(
    offerInteractor: OfferInteractor,
    userInteractor: UserInteractor,
) {
    routing {
        route("/v1/offers") {
            post {
                try {
                    val dto = call.receive<OfferDto>()
                    val token = call.request.authorization()!!.split(" ")[1]
                    val email = JWebToken.decodeEmailFromToken(token)
                    val user = userInteractor.getUserByEmail(email)

                    if (user?.id != dto.userId) {
                        throw OfferForbidden()
                    }

                    val offer = dto.toModel()
                    val createdOffer = offerInteractor.createOffer(offer)

                    val response =
                        OfferDto(
                            id = createdOffer.id,
                            userId = createdOffer.userId,
                            taskId = createdOffer.taskId,
                            status = createdOffer.status,
                            active = createdOffer.active,
                            text = createdOffer.text,
                            title = createdOffer.title,
                        )

                    call.respond(HttpStatusCode.Created, response)
                } catch (e: WebserverError) {
                    call.respond(e.getStatus(), e.getMessage())
                }
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
