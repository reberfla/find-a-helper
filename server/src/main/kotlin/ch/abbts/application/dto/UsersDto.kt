package ch.abbts.application.dto

import ch.abbts.domain.model.usersModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import ch.abbts.domain.model.AuthProvider

@Serializable
data class UsersDto(
    val id: Int? = null,
    val name: String? = null,
    val email: String,
    @SerialName("password_hash")
    val passwordHash: String? = null,
    @SerialName("zip_code")
    val zipCode: Int,
    @SerialName("image_url")
    val imageUrl: String? = null,
    val imgBase64: String? = null,
    val active: Boolean? = true,
    @SerialName("last_token_issued")
    val lastTokenIssued: Long? = 0L,
    @SerialName("locked_until")
    val lockedUntil: Long? = null,
    @SerialName("auth_provider")
    val authProvider: AuthProvider? = null,
    val birthdate: String,             // format: "yyyy-MM-dd"
    val idToken: String? = null
) : DTO<usersModel> {

    override fun toModel(): usersModel {
        return usersModel(
            id = id,
            email = email,
            name = name ?: "",
            passwordHash = passwordHash ?: "",
            zipCode = zipCode,
            imageUrl = imageUrl ?: "",
            image = imgBase64?.let { Base64.getDecoder().decode(it) },
            active = active,
            authProvider = authProvider ?: AuthProvider.LOCAL,
            lastTokenIssued = lastTokenIssued,
            lockedUntil = lockedUntil?: 0L,
            birthdate = LocalDate.parse(birthdate, dateFormatter),
        )
    }

    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fun imageUrl(user: usersModel): UsersDto {
            return UsersDto(
                id = user.id,
                email = user.email,
                name = user.name,
                authProvider = user.authProvider,
                imageUrl = user.imageUrl,
                imgBase64 = user.image?.takeIf { it.isNotEmpty() }
                    ?.let { Base64.getEncoder().encodeToString(it) },
                zipCode = user.zipCode,
                active = user.active,
                lockedUntil = user.lockedUntil,
                birthdate = user.birthdate.format(dateFormatter),
                lastTokenIssued = user.lastTokenIssued
            )
        }
    }
}
