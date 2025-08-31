package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.AssignmentRepository
import ch.abbts.adapter.database.repository.TaskRepository
import ch.abbts.application.dto.AssignmentDto
import ch.abbts.application.dto.AssignmentUpdateDto
import ch.abbts.error.AssignmentUpdateNotAllowed
import ch.abbts.utils.logger

class AssignmentInteractor(
    val assignmentRepository: AssignmentRepository,
    val taskRepository: TaskRepository,
) {
    val log = logger()

    fun getAssignmentByUserId(id: Int): List<AssignmentDto> {
        return assignmentRepository.getAssignmentByUserId(id) ?: listOf()
    }

    fun updateAssignment(
        assignmentUpdateDto: AssignmentUpdateDto,
        assignmentId: Int,
        userId: Int,
    ): Unit {
        val existing = assignmentRepository.getUserIdOfAssignment(assignmentId)
        if (existing != userId) {
            throw AssignmentUpdateNotAllowed()
        }
        return assignmentRepository.updateAssignment(
            assignmentUpdateDto,
            assignmentId,
        )
    }
}
