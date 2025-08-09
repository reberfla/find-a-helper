package ch.abbts.adapter.database.table

import ch.abbts.domain.model.OfferStatus
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object OffersTable : Table("offers") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val taskId = integer("task_id")
    val text = text("text")
    val title = varchar("title", 255).nullable()
    val status = enumerationByName("status", 255, OfferStatus::class)
    val active = bool("active")
    val validUntil = date("valid_until").nullable()
}
