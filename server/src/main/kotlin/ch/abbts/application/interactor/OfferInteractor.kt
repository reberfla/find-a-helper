package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.OffersRepository
import ch.abbts.domain.model.OfferModel
import ch.abbts.error.*
import ch.abbts.utils.Log

class OfferInteractor(
    private val offerRepo: OffersRepository
    // private val taskRepo: TasksRepository,
) {
    companion object : Log()

    fun createOffer(offer: OfferModel): OfferModel {
        /*
        todo:implement checks for =>
        Task not found
        cannot submit offer to inactive task
        submit offer to own task
        */

        val alreadyExists =
            offerRepo.hasUserSubmittedOfferForTask(offer.userId, offer.taskId)
        if (alreadyExists) {
            throw OfferAlreadyExists()
        }

        return offerRepo.createOffer(offer)
    }

    fun deleteOffer(userId: Int, offerId: Int) {
        val offer = offerRepo.getOfferById(offerId)

        if (offer?.userId != userId) {
            throw OfferForbidden()
        }

        if (offer.status != ch.abbts.domain.model.OfferStatus.SUBMITTED) {
            throw OfferForbidden()
        }

        offer.id?.let { offerRepo.deleteOffer(offer.userId, it) }
    }
}
