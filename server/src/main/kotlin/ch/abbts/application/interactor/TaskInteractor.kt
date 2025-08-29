package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.TaskRepository
import ch.abbts.adapter.database.repository.UsersRepository
import ch.abbts.application.dto.TaskPrivateDto
import ch.abbts.application.dto.TaskQueryParams
import ch.abbts.application.dto.TaskUpdateDto
import ch.abbts.application.dto.TaskWithOfferUsersModel
import ch.abbts.domain.model.TaskModel
import ch.abbts.error.TaskOfOtherUser
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

    fun updateTask(task: TaskUpdateDto, userId: Int, id: Int): TaskPrivateDto {
        val existing = taskRepository.getTaskById(id)
        if (existing.userId != userId) {
            throw TaskOfOtherUser()
        }
        taskRepository.updateTask(task, id)
        val toUpdateTask = taskRepository.getTaskById(id)
        val updatedTask = taskRepository.updateTask(task, id)
        val user = userRepository.getUserById(updatedTask.userId)!!
        return toUpdateTask.toPrivateDto(user.name, user.email)
    }

    fun getTasks(
        filterQuery: TaskQueryParams? = null,
        userId: Int? = null,
    ): List<TaskWithOfferUsersModel> {
        return taskRepository.getAllTasksWithOfferUsers(filterQuery, userId)
    }

    fun getTasksByCreator(userId: Int): List<TaskPrivateDto> {
        val user = userRepository.getUserById(userId)!!
        return taskRepository.getTaskByCreator(userId)?.map {
            it.toPrivateDto(user.name, user.email)
        } ?: listOf<TaskPrivateDto>()
    }

    fun getTaskById(taskId: Int): TaskModel {
        val task = taskRepository.getTaskById(taskId)
        return task
    }

    fun deleteTask(id: Int, userId: Int): Unit {
        taskRepository.deleteTask(id, userId)
    }
}
