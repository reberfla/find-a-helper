package ch.abbts.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateDto(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val zipCode: String? = null,
    val imgBase64: String? = null,
    val birthdate: String? = null, // format: "yyyy-MM-dd"
)
