package ch.abbts.adapter.database.table

import ch.abbts.domain.model.AssignmentStatus
import org.jetbrains.exposed.sql.CustomFunction
import org.jetbrains.exposed.sql.LongColumnType
import org.jetbrains.exposed.sql.Table

object AssignmentTable : Table("assignments") {
    val id = integer("id").autoIncrement()
    val taskId = integer("task_id")
    val userId = integer("user_id")
    val offerId = integer("offer_id")
    val status = enumerationByName("status", 255, AssignmentStatus::class)
    val active = bool("active")
    val createdAt =
        long("created_at")
            .defaultExpression(
                CustomFunction("UNIX_TIMESTAMP() * 1000", LongColumnType())
            )
    override val primaryKey = PrimaryKey(id)
}