package ch.abbts.application.dto

import ch.abbts.domain.model.AssignmentStatus
import kotlinx.serialization.Serializable

@Serializable
data class AssignmentDto(
    val id: Int,
    val taskCreatorUser: UserDto,
    val offerCreatorUser: UserDto,
    val task: TaskPublicDto,
    val offer: OfferDto,
    val status: AssignmentStatus,
    val active: Boolean = true,
)
