package ch.abbts.application.dto

import ch.abbts.domain.model.OfferModel
import ch.abbts.domain.model.OfferStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.serialization.Serializable

@Serializable
data class OfferDto(
    val id: Int? = null,
    val userId: Int,
    val taskId: Int,
    val status: OfferStatus = OfferStatus.SUBMITTED,
    val active: Boolean = true,
    val text: String,
    val title: String?,
    val validUntil: String? = null,
) : DTO<OfferModel> {
    override fun toModel(): OfferModel {
        val parsedValidUntil =
            validUntil
                ?.takeIf { it.isNotBlank() }
                ?.let { LocalDate.parse(it, dateFormatter) }

        return OfferModel(
            id = null,
            userId = userId,
            taskId = taskId,
            status = status,
            active = active,
            text = text,
            title = title,
            validUntil = parsedValidUntil, // <-- jetzt nullable
        )
    }

    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fun toDTO(offer: OfferModel): OfferDto {
            return OfferDto(
                id = offer.id,
                userId = offer.userId,
                taskId = offer.taskId,
                status = offer.status,
                active = offer.active,
                text = offer.text,
                title = offer.title,
                validUntil = offer.validUntil?.format(dateFormatter),
            )
        }
    }
}
