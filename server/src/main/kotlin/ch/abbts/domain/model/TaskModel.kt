package ch.abbts.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskModel(
    val id: Int? = null,
    @SerialName("user_id") val userId: Int,
    @SerialName("zip_code") val zipCode: Int? = null,
    val coordinates: String? = null,
    val title: String,
    val description: String,
    val category: TaskCategory? = null,
    val status: TaskStatus? = null,
    val active: Boolean? = true,
    val deadline: Long? = null,
    @SerialName("task_interval") val taskInterval: TaskInterval? = null,
    val weekdays: List<String>? = null,
    val createdAt: Long? = null,
)
