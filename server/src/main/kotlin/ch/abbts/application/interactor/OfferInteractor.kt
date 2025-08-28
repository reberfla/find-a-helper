package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.AssignmentRepository
import ch.abbts.adapter.database.repository.OfferRepository
import ch.abbts.adapter.database.repository.TaskRepository
import ch.abbts.application.dto.AssignmentDto
import ch.abbts.application.dto.OfferDto
import ch.abbts.domain.model.AssignmentModel
import ch.abbts.domain.model.AssignmentStatus
import ch.abbts.domain.model.OfferStatus
import ch.abbts.error.*
import ch.abbts.utils.Log
import ch.abbts.utils.LoggerService

class OfferInteractor(
    private val offerRepo: OfferRepository,
    private val taskRepo: TaskRepository,
    private val assignmentRepo: AssignmentRepository,
) {
    companion object : Log()

    fun createOffer(offer: OfferDto): OfferDto? {

        val task =
            taskRepo.getTaskById(offer.taskId)
                ?: throw TaskNotFound(offer.taskId)

        if (!task.active) {
            throw OfferForbidden()
        }

        if (task.userId == offer.userId) {
            throw OfferForbidden()
        }

        if (hasUserSubmittedOfferForTask(offer.userId, offer.taskId)) {
            throw OfferAlreadyExists()
        }

        val newOfferDto =
            OfferDto(
                null,
                offer.userId,
                offer.taskId,
                text = offer.text,
                title = offer.title,
                validUntil = offer.validUntil,
            )

        val newOfferModel = offerRepo.createOffer(newOfferDto.toModel())
        return newOfferModel?.let { OfferDto.toDTO(it) }
    }

    fun deleteOffer(userId: Int, offerId: Int): Boolean {
        val offer = offerRepo.getOfferById(offerId)

        LoggerService.debugLog(offer.toString())

        if (offer?.userId != userId) {
            throw OfferForbidden()
        }

        if (offer.status == ch.abbts.domain.model.OfferStatus.SUBMITTED) {
            throw OfferForbidden()
        }

        offer.id?.let {
            offerRepo.deleteOffer(offer.userId, it)
            return true
        }

        return false
    }

    fun hasUserSubmittedOfferForTask(userId: Int, taskId: Int): Boolean {
        val offers = offerRepo.getOffersForTask(taskId)
        if (offers != null) {
            return offers.any { it.userId == userId }
        }
        return false
    }

    fun getOfferById(offerId: Int): OfferDto {
        val offer = offerRepo.getOfferById(offerId) ?: throw OfferNotFound()
        return offer.let { OfferDto.toDTO(it) }
    }

    fun getOffersByCreator(userId: Int): List<OfferDto> {
        val offers = offerRepo.getOffersByCreator(userId) ?: emptyList()
        return offers.map { OfferDto.toDTO(it) }
    }

    fun getOffersForTask(taskId: Int): List<OfferDto> {
        val offers = offerRepo.getOffersForTask(taskId) ?: emptyList()
        return offers.map { OfferDto.toDTO(it) }
    }

    fun setOfferStatus(offerId: Int, status: OfferStatus): OfferDto {
        val saved =
            offerRepo.setOfferStatus(offerId, status)
                ?: throw OfferUpdateFailed()
        return saved.let { OfferDto.toDTO(it) }
    }

    fun acceptOffer(offerId: Int, currentUserId: Int): AssignmentDto {
        val offer = getOfferById(offerId)
        val task = taskRepo.getTaskById(offer.taskId)
        LoggerService.debugLog(offer.toString())
        LoggerService.debugLog(task.toString())

        if (task.userId != currentUserId) {
            throw OfferForbidden()
        }

        offerRepo.setOfferStatus(offerId, OfferStatus.ACCEPTED)
            ?: throw OfferUpdateFailed()

        getOffersForTask(offer.taskId)
            .filter { it.id != offerId }
            .forEach { other ->
                offerRepo.setOfferStatus(other.id!!, OfferStatus.REJECTED)
            }

        LoggerService.debugLog("here")

        val assignment =
            AssignmentModel(
                taskId = task.id!!,
                offerId = offer.id!!,
                status = AssignmentStatus.OPEN,
                active = true,
            )

        LoggerService.debugLog("here")

        return assignmentRepo.createAssignment(assignment)
    }
}
