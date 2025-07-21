package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.TasksTable
import ch.abbts.application.dto.TaskDto
import ch.abbts.application.dto.TaskQueryParams
import ch.abbts.domain.model.TaskModel
import ch.abbts.utils.logger
import java.time.Instant
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Op
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
        return try {
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
                    it[TasksTable.createdAt] = timeStamp
                } get TasksTable.id
            }
            task.copy(id = id, createdAt = timeStamp)
        } catch (e: Exception) {
            log.debug("database error: ${e.message}")
            throw e
        }
    }

    fun getAllTasks(): List<TaskModel>? {
        return try {
            transaction {
                TasksTable.selectAll()
                    .map {
                        TaskModel(
                            it[TasksTable.id],
                            it[TasksTable.userId],
                            it[TasksTable.zipCode],
                            it[TasksTable.coordinates],
                            it[TasksTable.title],
                            it[TasksTable.description],
                            it[TasksTable.category],
                            it[TasksTable.status],
                            it[TasksTable.active],
                            it[TasksTable.deadline],
                            it[TasksTable.taskInterval],
                            it[TasksTable.weekdays]?.let {
                                Json.decodeFromString(it)
                            },
                            it[TasksTable.createdAt],
                        )
                    }
                    .toList()
            }
        } catch (e: Exception) {
            log.debug("database error: ${e.message}")
            null
        }
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
        return try {
            transaction {
                TasksTable.select { query }
                    .map {
                        TaskModel(
                            it[TasksTable.id],
                            it[TasksTable.userId],
                            it[TasksTable.zipCode],
                            it[TasksTable.coordinates],
                            it[TasksTable.title],
                            it[TasksTable.description],
                            it[TasksTable.category],
                            it[TasksTable.status],
                            it[TasksTable.active],
                            it[TasksTable.deadline],
                            it[TasksTable.taskInterval],
                            it[TasksTable.weekdays]?.let {
                                Json.decodeFromString(it)
                            },
                            it[TasksTable.createdAt],
                        )
                    }
                    .toList()
            }
        } catch (e: Exception) {
            log.debug("database error: ${e.message}")
            null
        }
    }

    fun getTaskById(taskId: Int): TaskModel? {
        return try {
            transaction {
                TasksTable.select { TasksTable.id eq taskId }
                    .singleOrNull()
                    ?.let {
                        TaskModel(
                            it[TasksTable.id],
                            it[TasksTable.userId],
                            it[TasksTable.zipCode],
                            it[TasksTable.coordinates],
                            it[TasksTable.title],
                            it[TasksTable.description],
                            it[TasksTable.category],
                            it[TasksTable.status],
                            it[TasksTable.active],
                            it[TasksTable.deadline],
                            it[TasksTable.taskInterval],
                            it[TasksTable.weekdays]?.let {
                                Json.decodeFromString(it)
                            },
                            it[TasksTable.createdAt],
                        )
                    }
            }
        } catch (e: Exception) {
            log.error("Error fetching user by email: ${e.message}")
            null
        }
    }

    fun getTaskByCreator(userId: Int): List<TaskModel>? {
        return try {
            transaction {
                TasksTable.select { TasksTable.userId eq userId }
                    .map {
                        TaskModel(
                            it[TasksTable.id],
                            it[TasksTable.userId],
                            it[TasksTable.zipCode],
                            it[TasksTable.coordinates],
                            it[TasksTable.title],
                            it[TasksTable.description],
                            it[TasksTable.category],
                            it[TasksTable.status],
                            it[TasksTable.active],
                            it[TasksTable.deadline],
                            it[TasksTable.taskInterval],
                            it[TasksTable.weekdays]?.let {
                                Json.decodeFromString(it)
                            },
                            it[TasksTable.createdAt],
                        )
                    }
                    .toList()
            }
        } catch (e: Exception) {
            log.error("Error fetching task by user_id: ${e.message}")
            null
        }
    }

    fun updateTask(task: TaskDto, id: Int): Int {
        return try {
            transaction {
                TasksTable.update({ TasksTable.id eq id }) {
                    it[TasksTable.zipCode] = task.zipCode
                    it[TasksTable.coordinates] = task.coordinates
                    it[TasksTable.title] = task.title
                    it[TasksTable.description] = task.description
                    it[TasksTable.category] = task.category!!
                    it[TasksTable.status] = task.status!!
                    it[TasksTable.active] = task.active!!
                    it[TasksTable.deadline] = task.deadline
                    it[TasksTable.taskInterval] = task.taskInterval!!
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteTask(taskId: Int): Unit {
        try {
            transaction { TasksTable.deleteWhere { id eq taskId } }
        } catch (e: Exception) {
            log.debug("database exception: ${e.message}")
        }
    }
}
