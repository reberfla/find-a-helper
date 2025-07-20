package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.TaskRepository
import ch.abbts.application.dto.TaskDto
import ch.abbts.application.dto.TaskQueryParams
import ch.abbts.domain.model.TaskModel
import ch.abbts.error.TaskNotFound
import ch.abbts.utils.Log

class TaskInteractor(val taskRepository: TaskRepository) {
    companion object : Log() {}

    fun createTask(task: TaskModel): TaskModel {
        return taskRepository.createTask(task)
    }

    fun updateTask(task: TaskDto, userId: Int, id: Int): TaskModel {
        val existing = taskRepository.getTaskById(id)
        if (existing == null) {
            throw TaskNotFound(id)
        } else if (existing.userId != userId) {
            TODO()
        }
        taskRepository.updateTask(task, id)
        val updatedTask = taskRepository.getTaskById(id)
        if (updatedTask == null) {
            throw TaskNotFound(id)
        } else {
            return updatedTask
        }
    }

    fun getTasks(filterQuery: TaskQueryParams? = null): List<TaskModel> {
        if (filterQuery == null) {
            return taskRepository.getAllTasks() ?: listOf<TaskModel>()
        } else {
            return taskRepository.getTasks(filterQuery) ?: listOf<TaskModel>()
        }
    }

    fun getTasksByCreator(userId: Int): List<TaskModel> {
        return taskRepository.getTaskByCreator(userId) ?: listOf<TaskModel>()
    }

    fun getTaskById(taskId: Int): TaskModel {
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
