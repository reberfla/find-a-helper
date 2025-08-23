package ch.abbts.domain.model

import java.time.LocalDate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: Int? = null,
    val name: String,
    val email: String,
    val passwordHash: String? = null,
    val zipCode: String? = "",
    val imageUrl: String? = "",
    val image: ByteArray? = null,
    val active: Boolean? = true,
    val authProvider: AuthProvider,
    val lastTokenIssued: Long? = null,
    @Contextual val birthdate: LocalDate,
    @Contextual val lockedUntil: Long? = null,
)
