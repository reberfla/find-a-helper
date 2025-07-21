package ch.abbts.application.dto

import ch.abbts.domain.model.TaskCategory
import ch.abbts.domain.model.TaskInterval
import ch.abbts.domain.model.TaskStatus
import ch.abbts.error.InvalidParamValue
import ch.abbts.error.InvalidParams
import ch.abbts.utils.Log
import io.ktor.http.Parameters

class TaskQueryParams(params: Parameters) {
    var category = mutableSetOf<TaskCategory>()
    var interval = mutableSetOf<TaskInterval>()
    var status = mutableSetOf<TaskStatus>()

    companion object : Log() {}

    init {
        params.forEach { key, value ->
            when (key.lowercase()) {
                "category" ->
                    value.forEach {
                        try {
                            category.add(TaskCategory.valueOf(it.uppercase()))
                        } catch (e: IllegalArgumentException) {
                            throw InvalidParamValue(key, it)
                        }
                    }
                "interval" ->
                    value.forEach {
                        try {
                            interval.add(TaskInterval.valueOf(it.uppercase()))
                        } catch (e: IllegalArgumentException) {
                            throw InvalidParamValue(key, it)
                        }
                    }
                "status" ->
                    value.forEach {
                        try {
                            status.add(TaskStatus.valueOf(it.uppercase()))
                        } catch (e: IllegalArgumentException) {
                            throw InvalidParamValue(key, it)
                        }
                    }
                else -> {
                    log.debug(
                        "unsupported param in request: ${key.lowercase()}"
                    )
                    throw InvalidParams(key)
                }
            }
        }
    }

    override fun toString(): String {
        return "category: $category, interval: $interval, status: $status"
    }
}
