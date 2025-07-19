package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.TaskRepository
import ch.abbts.application.dto.TaskQueryParams
import ch.abbts.domain.model.TaskModel
import ch.abbts.error.TaskNotFound

class TaskInteractor(val taskRepository: TaskRepository) {

    fun createTask(task: TaskModel): TaskModel {
        taskRepository.createTask(task)
        return task
    }

    fun updateTask(taskmodel: TaskModel): TaskModel {
        TODO()
    }

    fun getTasks(filterQuery: TaskQueryParams? = null): List<TaskModel> {
        if (filterQuery == null) {
            return taskRepository.getAllTasks() ?: listOf<TaskModel>()
        } else {
            return taskRepository.getTasks(filterQuery) ?: listOf<TaskModel>()
        }
    }

    fun getTasksByCreator(userId: Int): List<TaskModel> {
        TODO()
    }

    fun getTasksById(taskId: Int): TaskModel {
        val task = taskRepository.getTaskById(taskId)
        if (task != null) {
            return task
        } else {
            throw TaskNotFound(taskId)
        }
    }

    fun deleteTask(id: Int): Unit {
        taskRepository.deleteTask(id)
    }
}
