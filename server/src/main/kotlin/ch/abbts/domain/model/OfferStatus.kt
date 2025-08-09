package ch.abbts.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class OfferStatus {
    @SerialName("SUBMITTED") SUBMITTED,
    @SerialName("ACCEPTED") ACCEPTED,
    @SerialName("REJECTED") REJECTED,
}
