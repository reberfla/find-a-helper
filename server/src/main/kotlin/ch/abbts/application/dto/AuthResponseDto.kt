package ch.abbts.application.dto

import ch.abbts.domain.model.JWT
import kotlinx.serialization.Serializable

@Serializable
class AuthResponseDto(
    val id: Int?,
    val token: JWT?,
    val email: String,
    val name: String? = null,
    val imgUrl: String? = null, // for google-user
    val imgBlob: String? = null, // Data URI string (base64 PNG or JPG)
)
