package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.AssignmentTable
import ch.abbts.adapter.database.table.TasksTable
import ch.abbts.domain.model.AssignmentModel
import ch.abbts.domain.model.AssignmentStatus
import ch.abbts.utils.logger
import ch.abbts.error.TaskNotFound
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import java.time.Instant
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*
import kotlin.collections.map

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

    fun delete(id: Int): Boolean = transaction {
        AssignmentTable.deleteWhere { AssignmentTable.id eq id } > 0
    }

    fun findById (id: Int): AssignmentModel {
        return transaction {
            AssignmentTable.select { AssignmentTable.id eq id }
                .firstOrNull()
                ?.let { it.toModel() } ?: throw TaskNotFound(id)

        }
    }

    fun updateStatus(id: Int, status: AssignmentStatus): Boolean = transaction {
        AssignmentTable.update({ AssignmentTable.id eq id }) {
            it[AssignmentTable.status] = status
        } > 0
    }


    fun listByTask (taskId: Int) {
        transaction {
            AssignmentTable.select { AssignmentTable.taskId eq taskId }
        }
    }

    fun listAll(onlyActive: Boolean = true): List<AssignmentModel> = transaction {
        val query = if (onlyActive) {
            AssignmentTable.select { AssignmentTable.active eq true }
        } else {
            AssignmentTable.selectAll()
        }
        query.map { it.toModel() }
    }


    fun setActive () {

    }


    //not sure, this has to be here
    private fun ResultRow.toModel(): AssignmentModel {
        return AssignmentModel(
            id = this[TasksTable.id],
            userId = this[TasksTable.userId],
            status = this[TasksTable.status],
            active = this[TasksTable.active],
            createdAt = this[TasksTable.createdAt],
            offerId = this[AssignmentTable.offerId],
            taskId = this[AssignmentTable.taskId],
        )
    }


}