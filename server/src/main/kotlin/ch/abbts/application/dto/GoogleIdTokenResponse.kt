package ch.abbts.application.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleIdTokenResponse(
    val iss: String,
    val sub: String,
    val azp: String,
    val aud: String,
    val iat: String,
    val exp: String,
    val email: String? = null,
    @SerialName("email_verified") val emailVerified: String? = null,
    val name: String? = null,
    val picture: String? = null,
    @SerialName("given_name") val givenName: String? = null,
    @SerialName("family_name") val familyName: String? = null,
    val locale: String? = null,
)
