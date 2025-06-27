package ch.abbts.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class usersModel(
    val id: Int? = null,
    val name: String,
    val email: String,
    val password_hash: String,
    val zip_code: Int,
    val image_url: String? = "",
    val image: ByteArray? = null,
    val active: Boolean? = true,
    val authProvider: String,
    @Contextual val birthdate: LocalDate,
    @Contextual val locked_until: LocalDate? = null
