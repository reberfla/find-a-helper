package ch.abbts.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AssignmentModel(
    val id: Int? = null,
    val userId: Int,
    val taskId: Int,
    val offerId: Int,
    val createdAt: Long,
    val status: String,
    val active: Boolean,
) /*{
    fun toPublicDto(): AssignementDto {
        return AssignementDto(
            id!!,
            taskId,
            offerId,
            createdAt,
            status,
            active,
        )
    }

    fun toPrivateDto(): AssignementDto {
        return AssignementDto(
            id!!,
            taskId,
            offerId,
            createdAt,
            status,
            active,
        )
    }
}*/