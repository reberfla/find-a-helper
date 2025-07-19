package ch.abbts.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TaskInterval {
    @SerialName("CONTINUOUS") CONTINUOUS,
    @SerialName("RECURRING") RECURRING,
    @SerialName("ONE_TIME") ONE_TIME,
}
