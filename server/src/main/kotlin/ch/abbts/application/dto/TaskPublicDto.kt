package ch.abbts.application.dto

import ch.abbts.domain.model.TaskCategory
import ch.abbts.domain.model.TaskInterval
import ch.abbts.domain.model.TaskStatus
import ch.abbts.domain.model.Weekdays
import kotlinx.serialization.Serializable

@Serializable
data class TaskPublicDto(
    val id: Int,
    val zipCode: String,
    val title: String,
    val description: String,
    val category: TaskCategory,
    val status: TaskStatus,
    val active: Boolean,
    val deadline: Long? = null,
    val taskInterval: TaskInterval,
    val weekdays: List<Weekdays> = listOf<Weekdays>(),
    val createdAt: Long,
)
