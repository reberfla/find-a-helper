package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.AssignmentTable
import ch.abbts.adapter.database.table.OffersTable
import ch.abbts.adapter.database.table.TasksTable
import ch.abbts.adapter.database.table.UsersTable
import ch.abbts.application.dto.AssignmentDto
import ch.abbts.application.dto.AssignmentUpdateDto
import ch.abbts.application.dto.OfferDto
import ch.abbts.application.dto.TaskPublicDto
import ch.abbts.application.dto.UserDto
import ch.abbts.domain.model.AssignmentModel
import ch.abbts.error.AssignmentNotFound
import ch.abbts.utils.logger
import java.time.Instant
import java.time.format.DateTimeFormatter
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class AssignmentRepository {
    val log = logger()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun getAssignmentById(id: Int): AssignmentModel {
        return transaction {
            AssignmentTable.select { AssignmentTable.id eq id }
                .singleOrNull()
                ?.toModel() ?: throw AssignmentNotFound(id)
        }
    }

    fun createAssignment(assignment: AssignmentModel): AssignmentModel {
        val timeStamp = Instant.now().epochSecond
        val id = transaction {
            AssignmentTable.insert {
                it[AssignmentTable.taskId] = assignment.taskId
                it[AssignmentTable.offerId] = assignment.offerId
                it[AssignmentTable.status] = assignment.status
                it[AssignmentTable.active] = assignment.active
                it[AssignmentTable.createdAt] = timeStamp
            } get AssignmentTable.id
        }
        return assignment.copy(id = id, createdAt = timeStamp)
    }

    fun getAssignmentByUserId(id: Int): List<AssignmentDto>? {
        val filter: Op<Boolean> =
            Op.build {
                listOfNotNull(TasksTable.userId eq id, OffersTable.userId eq id)
                    .reduce { acc, op -> acc or op }
            }

        val taskCreator = UsersTable.alias("taskCreator")
        val offerCreator = UsersTable.alias("offerCreator")

        val join =
            AssignmentTable.join(
                    TasksTable,
                    JoinType.INNER,
                    AssignmentTable.taskId,
                    TasksTable.id,
                )
                .join(
                    OffersTable,
                    JoinType.INNER,
                    AssignmentTable.offerId,
                    OffersTable.id,
                )
                .join(
                    taskCreator,
                    JoinType.INNER,
                    TasksTable.userId,
                    taskCreator[UsersTable.id],
                )
                .join(
                    offerCreator,
                    JoinType.INNER,
                    OffersTable.userId,
                    offerCreator[UsersTable.id],
                )

        return transaction {
            join
                .select { filter }
                .map { row ->
                    AssignmentDto(
                        id = row[AssignmentTable.id],
                        createdAt = row[AssignmentTable.createdAt],
                        status = row[AssignmentTable.status],
                        active = row[AssignmentTable.active],
                        taskCreatorUser =
                            UserDto(
                                id = row[taskCreator[UsersTable.id]],
                                name = row[taskCreator[UsersTable.name]],
                                email = row[taskCreator[UsersTable.email]],
                                birthdate =
                                    row[taskCreator[UsersTable.birthdate]]
                                        .format(dateFormatter),
                            ),
                        offerCreatorUser =
                            UserDto(
                                id = row[offerCreator[UsersTable.id]],
                                name = row[offerCreator[UsersTable.name]],
                                email = row[offerCreator[UsersTable.email]],
                                birthdate =
                                    row[offerCreator[UsersTable.birthdate]]
                                        .format(dateFormatter),
                            ),
                        task =
                            TaskPublicDto(
                                id = row[TasksTable.id],
                                zipCode = row[TasksTable.zipCode],
                                title = row[TasksTable.title],
                                description = row[TasksTable.description],
                                category = row[TasksTable.category],
                                status = row[TasksTable.status],
                                active = row[TasksTable.active],
                                taskInterval = row[TasksTable.taskInterval],
                                createdAt = row[TasksTable.createdAt],
                            ),
                        offer =
                            OfferDto(
                                id = row[OffersTable.id],
                                userId = row[OffersTable.userId],
                                taskId = row[OffersTable.taskId],
                                title = row[OffersTable.title],
                                text = row[OffersTable.text],
                                status = row[OffersTable.status],
                                active = row[OffersTable.active],
                                validUntil =
                                    row[OffersTable.validUntil]?.format(
                                        dateFormatter
                                    ),
                            ),
                    )
                }
                .toList()
        }
    }

    fun updateAssignment(updateAssignment: AssignmentUpdateDto, id: Int): Unit {
        return transaction {
            val existingAssignment = getAssignmentById(id)
            AssignmentTable.update({ AssignmentTable.id eq id }) {
                it[AssignmentTable.status] =
                    updateAssignment.status ?: existingAssignment.status
                it[AssignmentTable.active] =
                    updateAssignment.active ?: existingAssignment.active
            }
        }
    }

    private fun ResultRow.toModel(): AssignmentModel {
        return AssignmentModel(
            id = this[AssignmentTable.id],
            taskId = this[AssignmentTable.taskId],
            offerId = this[AssignmentTable.offerId],
            status = this[AssignmentTable.status],
            active = this[AssignmentTable.active],
            createdAt = this[AssignmentTable.createdAt],
        )
    }

    fun getUserIdOfAssignment(assignmentId: Int): Int {
        val join =
            AssignmentTable.join(
                TasksTable,
                JoinType.INNER,
                AssignmentTable.taskId,
                TasksTable.id,
            )
        return transaction {
            join
                .select { AssignmentTable.id eq assignmentId }
                .singleOrNull()
                ?.get(TasksTable.userId)
                ?: throw AssignmentNotFound(assignmentId)
        }
    }
}
