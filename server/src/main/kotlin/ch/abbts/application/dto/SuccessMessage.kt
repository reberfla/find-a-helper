package ch.abbts.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class SuccessMessage(val message: String = "success")
