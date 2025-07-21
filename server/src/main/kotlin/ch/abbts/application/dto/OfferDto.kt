package ch.abbts.application.dto
import ch.abbts.domain.model.OfferModel
import ch.abbts.domain.model.OfferStatus
import java.time.LocalDate

data class OfferDto(
    val id: Int? = null,
    val userId: Int,
    val taskId: Int,
    val status:OfferStatus = OfferStatus.SUBMITTED,
    val active:Boolean =true,
    val text: String,
    val title: String,
    val validUntil:String? = null,

):DTO<OfferModel> {
    override fun toModel(): OfferModel {
        return OfferModel(
            null,userId,taskId,status,active,text,title, LocalDate.parse(validUntil)
        )
    }
}
