package ch.abbts.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class usersModel(
    val id: Int? = null,
    val name: String,
    val email: String,
    val passwordHash: String? = null,
    val zipCode: Int,
    val imageUrl: String? = "",
    val image: ByteArray? = null,
    val active: Boolean? = true,
    val authProvider: AuthProvider,
    val lastTokenIssued: Long? = null,
    @Contextual val birthdate: LocalDate,
    @Contextual val lockedUntil: Long? = null
)