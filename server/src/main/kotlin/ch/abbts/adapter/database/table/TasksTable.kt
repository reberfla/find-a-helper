package ch.abbts.adapter.database.table

import ch.abbts.domain.model.TaskCategory
import ch.abbts.domain.model.TaskInterval
import ch.abbts.domain.model.TaskStatus
import org.jetbrains.exposed.sql.CustomFunction
import org.jetbrains.exposed.sql.LongColumnType
import org.jetbrains.exposed.sql.Table

object TasksTable : Table("tasks") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val zipCode = varchar("zip_code", 10)
    val coordinates = varchar("coordinates", 255)
    val title = varchar("title", 255)
    val description = text("description")
    val category = enumerationByName("category", 255, TaskCategory::class)
    val status = enumerationByName("status", 255, TaskStatus::class)
    val active = bool("active")
    val deadline = long("deadline").nullable()
    val taskInterval =
        enumerationByName("task_interval", 255, TaskInterval::class)
    val weekdays = text("weekdays").nullable()
    val createdAt =
        long("created_at")
            .defaultExpression(
                CustomFunction("UNIX_TIMESTAMP() * 1000", LongColumnType())
            )
    override val primaryKey = PrimaryKey(id)
}
