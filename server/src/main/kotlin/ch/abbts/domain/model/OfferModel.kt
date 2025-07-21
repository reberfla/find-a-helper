package ch.abbts.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class OfferModel(
    val id: Int? = null,
    val userId: Int,
    val taskId: Int,
    val status: OfferStatus,
    val active: Boolean,
    val text: String,
    val title: String,
    @Contextual val validUntil: LocalDate,
)
