package ch.abbts.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Weekdays {
    @SerialName("MONDAY") MONDAY,
    @SerialName("TUESDAY") TUESDAY,
    @SerialName("WEDNESDAY") WEDNESDAY,
    @SerialName("THURSDAY") THURSDAY,
    @SerialName("FRIDAY") FRIDAY,
    @SerialName("SATURDAY") SATURDAY,
    @SerialName("SUNDAY") SUNDAY,
}
