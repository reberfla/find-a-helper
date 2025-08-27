package ch.abbts.application.dto

import ch.abbts.domain.model.AssignmentStatus
import kotlinx.serialization.Serializable

@Serializable
data class AssignmentUpdateDto(
    val status: AssignmentStatus? = null,
    val active: Boolean? = null,
)
