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
    val zipCode = integer("zip_code").nullable()
    val coordinates = varchar("coordinates", 255).nullable()
    val title = varchar("title", 255)
    val description = text("description")
    val category =
        enumerationByName("category", 255, TaskCategory::class).nullable()
    val status = enumerationByName("status", 255, TaskStatus::class).nullable()
    val active = bool("active").nullable()
    val deadline = long("deadline").nullable()
    val taskInterval =
        enumerationByName("task_interval", 255, TaskInterval::class).nullable()
    val weekdays = text("weekdays").nullable()
    val createdAt =
        long("created_at")
            .defaultExpression(
                CustomFunction("UNIX_TIMESTAMP() * 1000", LongColumnType())
            )
    override val primaryKey = PrimaryKey(id)
}
