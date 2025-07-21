package ch.abbts.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TaskStatus {
    @SerialName("OPEN") OPEN,
    @SerialName("ASSIGNED") ASSIGNED,
    @SerialName("COMPLETED") COMPLETED,
}
