package ch.abbts.domain.model
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class AuthProvider {
        @SerialName("LOCAL")
        LOCAL,
        @SerialName("GOOGLE")
        GOOGLE
}
