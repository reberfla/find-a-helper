package ch.abbts.application.dto
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import ch.abbts.domain.model.AuthProvider

@Serializable
data class AuthenticationDto(
    val email: String,
    val password: String? = null,
    val token: String? = null,
    @SerialName("auth_provider")
    val authenticationProvider: AuthProvider
)
