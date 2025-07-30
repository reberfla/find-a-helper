package ch.abbts.domain.model

import ch.abbts.application.dto.TaskPrivateDto
import ch.abbts.application.dto.TaskPublicDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskModel(
    val id: Int? = null,
    @SerialName("user_id") val userId: Int,
    @SerialName("zip_code") val zipCode: String,
    val coordinates: String,
    val title: String,
    val description: String,
    val category: TaskCategory,
    val status: TaskStatus,
    val active: Boolean,
    val deadline: Long? = null,
    @SerialName("task_interval") val taskInterval: TaskInterval,
    val weekdays: List<Weekdays>? = null,
    val createdAt: Long? = null,
) {
    fun toPublicDto(): TaskPublicDto {
        return TaskPublicDto(
            id!!,
            zipCode,
            title,
            description,
            category,
            status,
            active,
            deadline,
            taskInterval,
            weekdays,
            createdAt!!,
        )
    }

    fun toPrivateDto(name: String, email: String): TaskPrivateDto {
        return TaskPrivateDto(
            id!!,
            name,
            email,
            zipCode,
            coordinates,
            title,
            description,
            category,
            status,
            active,
            deadline,
            taskInterval,
            weekdays,
            createdAt!!,
        )
    }
}
