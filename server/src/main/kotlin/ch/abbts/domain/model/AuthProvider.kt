package ch.abbts.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AuthProvider {
    @SerialName("LOCAL") LOCAL,
    @SerialName("GOOGLE") GOOGLE,
}
