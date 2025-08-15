package ch.abbts.domain.model

import ch.abbts.application.dto.TaskPrivateDto
import ch.abbts.application.dto.TaskPublicDto
import kotlinx.serialization.Serializable

@Serializable
data class TaskModel(
    val id: Int? = null,
    val userId: Int,
    val zipCode: String,
    val coordinates: String,
    val title: String,
    val description: String,
    val category: TaskCategory,
    val status: TaskStatus,
    val active: Boolean,
    val deadline: Long? = null,
    val taskInterval: TaskInterval,
    val weekdays: List<Weekdays> = listOf<Weekdays>(),
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
