package ch.abbts.application.dto

import java.util.*
import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateDto(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val zipCode: Int? = null,
    val imgBase64: String? = null,
    val birthdate: String? = null, // format: "yyyy-MM-dd"
)
