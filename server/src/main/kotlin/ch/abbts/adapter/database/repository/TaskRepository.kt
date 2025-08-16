package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.TasksTable
import ch.abbts.application.dto.TaskQueryParams
import ch.abbts.application.dto.TaskUpdateDto
import ch.abbts.domain.model.TaskModel
import ch.abbts.domain.model.Weekdays
import ch.abbts.error.TaskNotFound
import ch.abbts.error.TaskOfOtherUser
import ch.abbts.utils.logger
import java.time.Instant
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class TaskRepository {
    val log = logger()

    fun createTask(task: TaskModel): TaskModel {
        val timeStamp = Instant.now().epochSecond
        val id = transaction {
            TasksTable.insert {
                it[TasksTable.userId] = task.userId
                it[TasksTable.zipCode] = task.zipCode
                it[TasksTable.coordinates] = task.coordinates
                it[TasksTable.title] = task.title
                it[TasksTable.description] = task.description
                it[TasksTable.category] = task.category
                it[TasksTable.status] = task.status
                it[TasksTable.active] = task.active
                it[TasksTable.deadline] = task.deadline
                it[TasksTable.taskInterval] = task.taskInterval
                it[TasksTable.weekdays] = Json.encodeToString(task.weekdays)
                it[TasksTable.createdAt] = timeStamp
            } get TasksTable.id
        }
        return task.copy(id = id, createdAt = timeStamp)
    }

    fun getAllTasks(): List<TaskModel>? {
        return transaction { TasksTable.selectAll().map { it.toModel() } }
    }

    fun getTasks(queryParams: TaskQueryParams): List<TaskModel>? {
        val query =
            Op.build {
                listOfNotNull(
                        queryParams.category
                            .takeIf { it.isNotEmpty() }
                            ?.let {
                                (TasksTable.category inList it) as Op<Boolean>
                            },
                        queryParams.status
                            .takeIf { it.isNotEmpty() }
                            ?.let { TasksTable.status inList it },
                        queryParams.interval
                            .takeIf { it.isNotEmpty() }
                            ?.let { TasksTable.taskInterval inList it },
                    )
                    .reduceOrNull { acc, op -> acc and op } ?: Op.TRUE
            }
        return transaction { TasksTable.select { query }.map { it.toModel() } }
    }

    fun getTaskById(taskId: Int): TaskModel {
        return transaction {
            TasksTable.select { TasksTable.id eq taskId }
                .singleOrNull()
                ?.let { it.toModel() } ?: throw TaskNotFound(taskId)
        }
    }

    fun getTaskByCreator(userId: Int): List<TaskModel>? {
        return transaction {
            TasksTable.select { TasksTable.userId eq userId }
                .map { it.toModel() }
                .toList()
        }
    }

    fun updateTask(task: TaskUpdateDto, id: Int): TaskModel {
        transaction {
            val existingTask = getTaskById(id)
            TasksTable.update({ TasksTable.id eq id }) {
                it[TasksTable.zipCode] = task.zipCode ?: existingTask.zipCode
                it[TasksTable.coordinates] =
                    task.coordinates ?: existingTask.coordinates
                it[TasksTable.title] = task.title ?: existingTask.title
                it[TasksTable.description] =
                    task.description ?: existingTask.description
                it[TasksTable.category] = task.category ?: existingTask.category
                it[TasksTable.status] = task.status ?: existingTask.status
                it[TasksTable.active] = task.active ?: existingTask.active
                it[TasksTable.deadline] = task.deadline
                it[TasksTable.taskInterval] =
                    task.taskInterval ?: existingTask.taskInterval
                it[TasksTable.weekdays] =
                    Json.encodeToString(task.weekdays)
                        ?: Json.encodeToString(existingTask.weekdays)
            }
        }
        return getTaskById(id)
    }

    fun deleteTask(taskId: Int, userId: Int): Unit {
        transaction {
            val task = getTaskById(taskId)
            if (task.userId == userId) {
                TasksTable.deleteWhere { id eq taskId }
            } else {
                throw TaskOfOtherUser()
            }
        }
    }

    private fun ResultRow.toModel(): TaskModel {
        return TaskModel(
            id = this[TasksTable.id],
            userId = this[TasksTable.userId],
            zipCode = this[TasksTable.zipCode],
            coordinates = this[TasksTable.coordinates],
            title = this[TasksTable.title],
            description = this[TasksTable.description],
            category = this[TasksTable.category],
            status = this[TasksTable.status],
            active = this[TasksTable.active],
            taskInterval = this[TasksTable.taskInterval],
            deadline = this[TasksTable.deadline],
            weekdays =
                this[TasksTable.weekdays]?.let { Json.decodeFromString(it) }
                    ?: listOf<Weekdays>(),
            createdAt = this[TasksTable.createdAt],
        )
    }
}
