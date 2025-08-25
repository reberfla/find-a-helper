package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.AssignmentTable
import ch.abbts.domain.model.AssignmentModel
import ch.abbts.utils.logger
import java.time.Instant
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class AssignmentRepository {
    val log = logger()

    fun createAssignment(assignment: AssignmentModel): AssignmentModel {
        val timeStamp = Instant.now().epochSecond
        val id = transaction {
            AssignmentTable.insert {
                it[AssignmentTable.userId] = assignment.userId
                it[AssignmentTable.taskId] = assignment.taskId
                it[AssignmentTable.offerId] = assignment.offerId
                it[AssignmentTable.status] = assignment.status
                it[AssignmentTable.active] = assignment.active
                it[AssignmentTable.createdAt] = assignment.createdAt
            } get AssignmentTable.id
        }
        return assignment.copy(id = id, createdAt = timeStamp)
    }


}