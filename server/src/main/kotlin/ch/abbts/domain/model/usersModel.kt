package ch.abbts.domain.model

import domain.model.AuthProvider
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import java.time.LocalDate

@Serializable
data class usersModel(
    val id: Int? = null,
    val name: String,
    val email: String,
    @SerialName("password_hash")
    val passwordHash: String? = null,
    @SerialName("zip_code")
    val zipCode: Int,
    @SerialName("image_url")
    val imageUrl: String? = "",
    val image: ByteArray? = null,
    val active: Boolean? = true,
    @SerialName("auth_provider")
    val authProvider: AuthProvider,
    @SerialName("last_token_issued")
    val lastTokenIssued: Long?,
    @Contextual val birthdate: LocalDate,
    @SerialName("locked_until")
    @Contextual val lockedUntil: Long? = null
)
