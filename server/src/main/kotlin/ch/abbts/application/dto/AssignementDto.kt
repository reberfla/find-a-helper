package ch.abbts.application.dto

import kotlinx.serialization.Serializable


@Serializable
data class AssignementDto(
    val id: Int,
    val taskId: Int,
    val offerId: Int,
    val createdAt: Long,
    val status: String,
    val active: Boolean, = true,
    ) {
    fun toModel(userID: Int): AssignmentModel {
        return AssignmentModel(
            null,
            userID,
            taskId,
            offerId,
            createdAt,
            status,
            active,
        )
    }
}