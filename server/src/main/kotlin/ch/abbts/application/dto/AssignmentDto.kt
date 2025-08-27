package ch.abbts.application.dto

import kotlinx.serialization.Serializable
import ch.abbts.domain.model.AssignmentModel


@Serializable
data class AssignmentDto(
    val id: Int,
    val userId: Int,
    val taskId: Int,
    val offerId: Int,
    val createdAt: Long,
    val status: String,
    val active: Boolean = true,
    ) : DTO<AssignmentModel> {
    override fun toModel(): AssignmentModel {
        return AssignmentModel(
            null,
            userId,
            taskId,
            offerId,
            createdAt,
            status,
            active,
        )
    }
}