package ch.abbts.application.dto

import ch.abbts.domain.model.TaskCategory
import ch.abbts.domain.model.TaskInterval
import ch.abbts.domain.model.TaskModel
import ch.abbts.domain.model.TaskStatus
import ch.abbts.domain.model.Weekdays
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    @SerialName("zip_code") val zipCode: String,
    val coordinates: String,
    val title: String,
    val description: String,
    val category: TaskCategory? = TaskCategory.OTHERS,
    val status: TaskStatus? = TaskStatus.OPEN,
    val active: Boolean? = true,
    val deadline: Long? = null,
    @SerialName("task_interval")
    val taskInterval: TaskInterval? = TaskInterval.ONE_TIME,
    val weekdays: List<Weekdays>? = listOf<Weekdays>(),
) {
    fun toModel(userId: Int): TaskModel {
        return TaskModel(
            null,
            userId,
            zipCode,
            coordinates,
            title,
            description,
            category!!,
            status!!,
            active!!,
            deadline,
            taskInterval!!,
            weekdays,
        )
    }
}
