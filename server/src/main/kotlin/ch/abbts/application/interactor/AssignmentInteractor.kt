package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.AssignmentRepository
import ch.abbts.adapter.database.repository.TaskRepository
import ch.abbts.application.dto.AssignmentDto
import ch.abbts.application.dto.AssignmentUpdateDto
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
        id: Int,
    ): Unit {
        return assignmentRepository.updateAssignment(assignmentUpdateDto, id)
    }
}
