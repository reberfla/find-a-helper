package ch.abbts.application.dto

import kotlinx.serialization.Serializable
import ch.abbts.domain.model.AssignmentModel


@Serializable
data class AssignmentDto(
    val id: Int,
    val taskId: Int,
    val offerId: Int,
    val createdAt: Long,
    val status: String,
    val active: Boolean, = true,
    ) {
    fun toModel(userID: Int): AssignmentModel {
        return AssignmentModel(
            id = requireNotNull(id),
            taskId,
            offerId,
            createdAt,
            status,
            active,
        )
    }
}