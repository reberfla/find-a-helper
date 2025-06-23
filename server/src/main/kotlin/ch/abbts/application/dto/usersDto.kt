
import ch.abbts.application.dto.DTO
import ch.abbts.domain.model.usersModel
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class usersDto(
    val id: Int? = null,
    val name: String? = null,
    val email: String,
    val password_hash: String? = null,
    val zip_code: Int,
    val image_url: String? = null,
    val imgBase64: String? = null,
    val active: Boolean? = true,
    val locked_until: Long? = null, //todo: date ??
    val authProvider: String? = null,
    val bithdate:Long,
    val idToken: String? = null,
) : DTO<usersModel> {
    override fun toModel(): usersModel {
        return usersModel(
            id = id,
            email = email,
            name = name ?: "",
            password_hash = password_hash ?: "",
            zip_code = zip_code ?: 0,
            image_url = image_url ?: "",
            image = imgBase64?.let { Base64.getDecoder().decode(it) },
            active = active,
            loced_until = locked_until,
            authProvider = authProvider ?: "ENUM_LOCAL",
            birthdate = bithdate ?: 0,
        )
    }

    companion object {
        fun fromModel(user: usersModel): usersDto {
            return usersDto(
                id = user.id,
                email = user.email,
                name = user.name,
                authProvider = user.authProvider,
                image_url = user.image_url,
                imgBase64 = user.image?.takeIf { it.isNotEmpty() }
                    ?.let { Base64.getEncoder().encodeToString(it) },
                zip_code = user.zip_code,
                active = user.active,
                locked_until = user.loced_until,
                bithdate = user.birthdate,
            )
        }
    }
}
