package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.OffersTable
import ch.abbts.domain.model.OfferModel
import ch.abbts.domain.model.OfferStatus
import ch.abbts.utils.Log
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class OfferRepository {
    companion object : Log()

    fun createOffer(offer: OfferModel): OfferModel? {
        return try {
            val offerModel = transaction {
                OffersTable.insert {
                        it[userId] = offer.userId
                        it[taskId] = offer.taskId
                        it[status] = OfferStatus.SUBMITTED
                        it[active] = true
                        it[text] = offer.text
                        it[title] = offer.title
                        it[validUntil] = offer.validUntil
                    }
                    .resultedValues
                    ?.firstOrNull()
                    ?.toModel()
            }
            offerModel
        } catch (e: Exception) {
            throw e
        }
    }

    fun setOfferStatus(offerId: Int, status: OfferStatus): OfferModel? {
        return try {
            transaction {
                OffersTable.update({ OffersTable.id eq offerId }) {
                    it[OffersTable.status] = status
                }
            }
            getOfferById(offerId)
        } catch (e: Exception) {
            null
        }
    }

    fun deleteOffer(userId: Int, offerId: Int): Boolean {
        return try {
            transaction {
                OffersTable.deleteWhere {
                    (id eq offerId) and (OffersTable.userId eq userId)
                } > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun getOffersForTask(taskId: Int): List<OfferModel>? {
        return try {
            transaction {
                OffersTable.select { OffersTable.taskId eq taskId }
                    .map { it.toModel() }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getOffersByCreator(userId: Int): List<OfferModel>? {
        return try {
            transaction {
                OffersTable.select { OffersTable.userId eq userId }
                    .map { it.toModel() }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getOfferById(offerId: Int): OfferModel? {
        return try {
            transaction {
                val offer =
                    OffersTable.select { OffersTable.id eq offerId }
                        .singleOrNull()
                offer?.toModel()
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun ResultRow.toModel(): OfferModel {
        try {
            return OfferModel(
                id = this[OffersTable.id],
                userId = this[OffersTable.userId],
                taskId = this[OffersTable.taskId],
                status = this[OffersTable.status],
                active = this[OffersTable.active],
                text = this[OffersTable.text],
                title = this[OffersTable.title],
                validUntil = this[OffersTable.validUntil],
            )
        } catch (e: Exception) {
            throw e
        }
    }
}
