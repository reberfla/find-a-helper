package ch.abbts.application.dto

import ch.abbts.domain.model.TaskCategory
import ch.abbts.domain.model.TaskInterval
import ch.abbts.domain.model.TaskStatus
import ch.abbts.domain.model.Weekdays
import kotlinx.serialization.Serializable

@Serializable
data class TaskUpdateDto(
    val zipCode: String?,
    val coordinates: String? = null,
    val title: String? = null,
    val description: String? = null,
    val category: TaskCategory? = null,
    val status: TaskStatus? = null,
    val active: Boolean? = null,
    val deadline: Long? = null,
    val taskInterval: TaskInterval? = null,
    val weekdays: List<Weekdays>? = null,
)
