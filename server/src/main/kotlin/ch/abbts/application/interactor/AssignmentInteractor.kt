package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.AssignmentRepository
import ch.abbts.adapter.database.repository.TaskRepository
import ch.abbts.domain.model.AssignmentModel
import ch.abbts.domain.model.AssignmentStatus
import ch.abbts.utils.logger

class AssignmentInteractor (
    val assignmentRepository: AssignmentRepository,
    val taskRepository: TaskRepository,
){
    val log = logger()

    fun getById(id: Int): AssignmentModel {
        return assignmentRepository.findById(id)
            ?: throw NoSuchElementException("Assignment $id nicht gefunden.")
    }

    fun list(taskId: Int? = null, onlyActive: Boolean = true): List<AssignmentModel> {
        return if (taskId != null) {
            assignmentRepository.listByTask(taskId, onlyActive)
        } else {
            assignmentRepository.listAll(onlyActive)
        }
    }

    fun deactivate(id: Int): Boolean {
        val current = getById(id)
        if (!current.active) {
            log.debug("Assignment $id ist bereits inaktiv.")
            return true
        }
        log.info("Deaktiviere Assignment $id")
        return assignmentRepository.setActive(id, false)
    }

    fun delete(id: Int): Boolean {
        log.warn("Lösche Assignment $id (Hard-Delete)")
        return assignmentRepository.delete(id)
    }

    private fun ensureTaskExists(taskId: Int) {
        val task = taskRepository.findById(taskId)
            ?: throw IllegalArgumentException("Task $taskId existiert nicht.")
    }
}

