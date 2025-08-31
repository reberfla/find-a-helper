package ch.abbts.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AssignmentModel(
    val id: Int? = null,
    val taskId: Int,
    val offerId: Int,
    val status: AssignmentStatus,
    val active: Boolean,
    val createdAt: Long? = null,
)
