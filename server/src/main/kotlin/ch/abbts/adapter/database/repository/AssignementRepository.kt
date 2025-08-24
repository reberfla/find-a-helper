package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.AssignementTable
import ch.abbts.domain.model.AssignmentModel
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

class AssignementRepository {
    val log = logger()

    fun createAssignment(assignment: AssignmentModel): AssignmentModel {
        val timeStamp = Instant.now().epochSecond
        val id = transaction {
            AssignementTable.insert {
                it[AssignementTable.userId] = assignment.userId
                it[AssignementTable.taskId] = assignment.taskId
                it[AssignementTable.offerId] = assignment.offerId
                it[AssignementTable.status] = assignment.status
                it[AssignementTable.active] = assignment.active
                it[AssignementTable.createdAt] = assignment.createdAt
            } get AssignementTable.id
        }
        return assignment.copy(id = id, createdAt = timeStamp)
    }


}