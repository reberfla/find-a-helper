package ch.abbts.application.dto

import ch.abbts.domain.model.TaskCategory
import ch.abbts.domain.model.TaskInterval
import ch.abbts.domain.model.TaskStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskPrivateDto(
    val id: Int,
    val name: String,
    val email: String,
    @SerialName("zip_code") val zipCode: String,
    val coordinates: String,
    val title: String,
    val description: String,
    val category: TaskCategory,
    val status: TaskStatus,
    val active: Boolean,
    val deadline: Long? = null,
    @SerialName("task_interval") val taskInterval: TaskInterval,
    val weekdays: List<String>? = listOf<String>(),
    val createdAt: Long,
)
