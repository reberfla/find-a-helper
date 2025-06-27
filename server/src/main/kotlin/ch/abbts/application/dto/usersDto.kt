import ch.abbts.application.dto.DTO
import ch.abbts.domain.model.usersModel
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
    val locked_until: String? = null,  // format: "yyyy-MM-dd"
    val authProvider: String? = null,
    val birthdate: String,             // format: "yyyy-MM-dd"
    val idToken: String? = null,
) : DTO<usersModel> {

    override fun toModel(): usersModel {
        return usersModel(
            id = id,
            email = email,
            name = name ?: "",
            password_hash = password_hash ?: "",
            zip_code = zip_code,
            image_url = image_url ?: "",
            image = imgBase64?.let { Base64.getDecoder().decode(it) },
            active = active,
            authProvider = authProvider ?: "ENUM_LOCAL",
            locked_until = locked_until?.let { LocalDate.parse(it, dateFormatter) },
            birthdate = LocalDate.parse(birthdate, dateFormatter)
        )
    }

    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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
                locked_until = user.locked_until?.format(dateFormatter),
                birthdate = user.birthdate.format(dateFormatter)
            )
        }
    }
}
