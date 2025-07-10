package ch.abbts.application.dto
import kotlinx.serialization.Serializable

enum class ApiResponseMessage(val status: Int, val message: String) {
    LOGIN_SUCCESS(200, "Login erfolgreich"),
    REGISTER_SUCCESS(200, "Registrierung erfolgreich"),
    TOKEN_VALID(200, "Token ist gültig"),
    AUTH_FAILED(401, "Authentifizierung fehlgeschlagen"),
    INVALID_CREDENTIALS(400, "Ungültige Zugangsdaten"),
    NOT_FOUND(404, "Ressource nicht gefunden"),
    INTERNAL_ERROR(500, "Interner Serverfehler")
}


@Serializable
data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null,
    val errors: List<String>? = null
) {
    companion object {
        fun <T> from(msg: ApiResponseMessage, data: T? = null, errors: List<String>? = null): ApiResponse<T> {
            return ApiResponse(
                status = msg.status,
                message = msg.message,
                data = data,
                errors = errors
            )
        }
    }
}

