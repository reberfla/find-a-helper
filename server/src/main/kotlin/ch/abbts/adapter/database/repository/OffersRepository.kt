package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.OffersTable
import ch.abbts.domain.model.OfferModel
import ch.abbts.domain.model.OfferStatus
import ch.abbts.error.OfferAlreadyExists
import ch.abbts.error.OfferCreationFailed
import ch.abbts.error.OfferDeletionNotAllowed
import ch.abbts.error.OfferForbidden
import ch.abbts.utils.Log
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class OffersRepository {
    companion object : Log()

    fun createOffer(offer: OfferModel): OfferModel {
        try {

            return transaction {
                /*todo:implement checks for =>
                // Check: user cannot offer on their own task
                // Check: user has not already submitted offer for this task*/
                val existing = OffersTable.select {
                    (OffersTable.userId eq offer.userId) and (OffersTable.taskId eq offer.taskId)
                }.count()

                if (existing > 0) {
                    throw OfferAlreadyExists()
                }

                val inserted = OffersTable.insert {
                    it[userId] = offer.userId
                    it[taskId] = offer.taskId
                    it[status] = OfferStatus.SUBMITTED
                    it[active] = true
                    it[text] = offer.text
                    it[title] = offer.title
                    it[validUntil] = offer.validUntil
                    it[createdAt] = System.currentTimeMillis()
                }.resultedValues?.firstOrNull() ?: throw OfferCreationFailed()

                inserted.toModel()
            }
        } catch (e: OfferForbidden) {
            throw e
        } catch (e: Exception) {
            throw OfferCreationFailed()
        }
    }

    fun deleteOffer(userId: Int, offerId: Int): Boolean {
        return transaction {
            val offer = OffersTable.select {
                (OffersTable.id eq offerId) and (OffersTable.userId eq userId)
            }.singleOrNull() ?: return@transaction false

            if (offer[OffersTable.status] != OfferStatus.SUBMITTED) {
                throw OfferDeletionNotAllowed()
            }

            OffersTable.deleteWhere {
                (OffersTable.id eq offerId) and (OffersTable.userId eq userId)
            } > 0
        }
    }

    fun hasUserSubmittedOfferForTask(userId: Int, taskId: Int): Boolean {
        return transaction {
            OffersTable.select {
                (OffersTable.userId eq userId) and (OffersTable.taskId eq taskId)
            }.count() > 0
        }
    }


    fun getOffersByTask(taskId: Int): List<OfferModel> {
        return transaction {
            OffersTable.select {
                OffersTable.taskId eq taskId
            }.map { it.toModel() }
        }
    }

    fun getOfferById(offerId: Int): OfferModel? {
        return try  {
            transaction{
                val offer = OffersTable.select { OffersTable.id eq offerId }.singleOrNull()
                offer?.toModel()
        }
        }catch (e:Exception){
            null
        }
    }

    private fun ResultRow.toModel(): OfferModel {
        return OfferModel(
            id = this[OffersTable.id],
            userId = this[OffersTable.userId],
            taskId = this[OffersTable.taskId],
            status = this[OffersTable.status],
            active = this[OffersTable.active],
            text = this[OffersTable.text],
            title = this[OffersTable.title],
            validUntil = this[OffersTable.validUntil]
        )
    }
}
