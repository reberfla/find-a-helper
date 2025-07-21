package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.TaskRepository
import ch.abbts.adapter.database.repository.UsersRepository
import ch.abbts.application.dto.TaskDto
import ch.abbts.application.dto.TaskPrivateDto
import ch.abbts.application.dto.TaskQueryParams
import ch.abbts.domain.model.TaskModel
import ch.abbts.error.TaskNotFound
import ch.abbts.utils.logger

class TaskInteractor(
    val taskRepository: TaskRepository,
    val userRepository: UsersRepository,
) {
    val log = logger()

    fun createTask(task: TaskModel): TaskPrivateDto {
        val createdTask = taskRepository.createTask(task)
        val user = userRepository.getUserById(task.userId)!!
        return createdTask.toPrivateDto(user.name, user.email)
    }

    fun updateTask(task: TaskDto, userId: Int, id: Int): TaskPrivateDto {
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
            val user = userRepository.getUserById(updatedTask.userId)!!
            return updatedTask.toPrivateDto(user.name, user.email)
        }
    }

    fun getTasks(filterQuery: TaskQueryParams? = null): List<TaskModel> {
        if (filterQuery == null) {
            return taskRepository.getAllTasks() ?: listOf<TaskModel>()
        } else {
            return taskRepository.getTasks(filterQuery) ?: listOf<TaskModel>()
        }
    }

    fun getTasksByCreator(userId: Int): List<TaskPrivateDto> {
        val user = userRepository.getUserById(userId)!!
        return taskRepository.getTaskByCreator(userId)?.map {
            it.toPrivateDto(user.name, user.email)
        } ?: listOf<TaskPrivateDto>()
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
