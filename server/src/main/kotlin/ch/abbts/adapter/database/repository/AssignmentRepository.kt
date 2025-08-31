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
import java.time.format.DateTimeFormatter
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class AssignmentRepository {
    val log = logger()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val taskCreator = UsersTable.alias("taskCreator")
    private val offerCreator = UsersTable.alias("offerCreator")

    private val joinDto =
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

    fun getAssignmentById(id: Int): AssignmentModel {
        return transaction {
            AssignmentTable.select { AssignmentTable.id eq id }
                .singleOrNull()
                ?.toModel() ?: throw AssignmentNotFound(id)
        }
    }

    fun createAssignment(assignment: AssignmentModel): AssignmentDto {
        return transaction {
            val id =
                AssignmentTable.insert {
                    it[taskId] = assignment.taskId
                    it[offerId] = assignment.offerId
                    it[status] = assignment.status
                    it[active] = assignment.active
                } get AssignmentTable.id

            joinDto.select { AssignmentTable.id eq id }.single().toDto()
        }
    }

    fun getAssignmentByUserId(id: Int): List<AssignmentDto>? {
        val filter: Op<Boolean> =
            Op.build {
                listOfNotNull(TasksTable.userId eq id, OffersTable.userId eq id)
                    .reduce { acc, op -> acc or op }
            }

        return transaction {
            joinDto.select { filter }.map { it.toDto() }.toList()
        }
    }

    fun updateAssignment(updateAssignment: AssignmentUpdateDto, id: Int): Unit {
        transaction {
            val existingAssignment = getAssignmentById(id)
            AssignmentTable.update({ AssignmentTable.id eq id }) {
                it[status] =
                    updateAssignment.status ?: existingAssignment.status
                it[active] =
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

    fun ResultRow.toDto(): AssignmentDto {
        return AssignmentDto(
            id = this[AssignmentTable.id],
            status = this[AssignmentTable.status],
            active = this[AssignmentTable.active],
            taskCreatorUser =
                UserDto(
                    id = this[taskCreator[UsersTable.id]],
                    name = this[taskCreator[UsersTable.name]],
                    email = this[taskCreator[UsersTable.email]],
                    birthdate =
                        this[taskCreator[UsersTable.birthdate]].format(
                            dateFormatter
                        ),
                ),
            offerCreatorUser =
                UserDto(
                    id = this[offerCreator[UsersTable.id]],
                    name = this[offerCreator[UsersTable.name]],
                    email = this[offerCreator[UsersTable.email]],
                    birthdate =
                        this[offerCreator[UsersTable.birthdate]].format(
                            dateFormatter
                        ),
                ),
            task =
                TaskPublicDto(
                    id = this[TasksTable.id],
                    zipCode = this[TasksTable.zipCode],
                    title = this[TasksTable.title],
                    description = this[TasksTable.description],
                    category = this[TasksTable.category],
                    status = this[TasksTable.status],
                    active = this[TasksTable.active],
                    taskInterval = this[TasksTable.taskInterval],
                    createdAt = this[TasksTable.createdAt],
                ),
            offer =
                OfferDto(
                    id = this[OffersTable.id],
                    userId = this[OffersTable.userId],
                    taskId = this[OffersTable.taskId],
                    title = this[OffersTable.title],
                    text = this[OffersTable.text],
                    status = this[OffersTable.status],
                    active = this[OffersTable.active],
                    validUntil =
                        this[OffersTable.validUntil]?.format(dateFormatter),
                ),
        )
    }
}
