package ch.abbts.application.dto

import ch.abbts.domain.model.AuthProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationDto(
    val email: String,
    val password: String? = null,
    val token: String? = null,
    @SerialName("auth_provider") val authenticationProvider: AuthProvider,
)
