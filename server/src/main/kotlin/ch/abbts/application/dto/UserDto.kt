package ch.abbts.application.dto

import ch.abbts.domain.model.AuthProvider
import ch.abbts.domain.model.UserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Serializable
data class UserDto(
    val id: Int? = null,
    val name: String? = null,
    val email: String,
    val password: String? = null,
    val zipCode: Int? = -1,
    val imageUrl: String? = null,
    val imgBase64: String? = null,
    val active: Boolean? = true,
    val authProvider: AuthProvider? = null,
    val birthdate: String?,             // format: "yyyy-MM-dd"
    val idToken: String? = null
) : DTO<UserModel> {

    override fun toModel(): UserModel {
        return UserModel(
            id = id,
            email = email,
            name = name ?: "",
            passwordHash = password ?: "",
            zipCode = zipCode,
            imageUrl = imageUrl ?: "",
            image = imgBase64?.let { Base64.getDecoder().decode(it) },
            active = active,
            authProvider = authProvider ?: AuthProvider.LOCAL,
            birthdate = LocalDate.parse(birthdate, dateFormatter),
        )
    }

    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fun toDTO(user: UserModel): UserDto {
            return UserDto(
                id = user.id,
                email = user.email,
                name = user.name,
                authProvider = user.authProvider,
                imageUrl = user.imageUrl,
                imgBase64 = user.image?.takeIf { it.isNotEmpty() }
                    ?.let { Base64.getEncoder().encodeToString(it) },
                zipCode = user.zipCode,
                active = user.active,
                birthdate = user.birthdate.format(dateFormatter),
            )
        }
    }
}
