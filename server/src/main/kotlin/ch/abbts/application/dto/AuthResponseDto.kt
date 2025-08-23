package ch.abbts.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val id: Int?,
    val jwt: String,
    val email: String,
    val name: String? = null,
    val imgUrl: String? = null, // for google-user
    val imgBlob: String? = null, // Data URI string (base64 PNG or JPG)
)
