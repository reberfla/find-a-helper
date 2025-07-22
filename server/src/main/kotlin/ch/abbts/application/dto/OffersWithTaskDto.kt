package ch.abbts.application.dto

import ch.abbts.domain.model.OfferStatus
import ch.abbts.domain.model.TaskModel
import kotlinx.serialization.Serializable

@Serializable
data class OfferWithTaskDto(
    val id: Int,
    val userId: Int,
    val taskId: Int,
    val status: OfferStatus,
    val active: Boolean,
    val title: String,
    val text: String,
    val validUntil: String? = null,
    val task: TaskPublicDto,
)

fun OfferDto.toWithTaskDto(task: TaskModel): OfferWithTaskDto {
    return OfferWithTaskDto(
        id = this.id ?: -1,
        userId = this.userId,
        taskId = this.taskId,
        status = this.status,
        active = this.active,
        title = this.title ?: "",
        text = this.text,
        validUntil = this.validUntil?.toString(),
        task = task.toPublicDto(),
    )
}
