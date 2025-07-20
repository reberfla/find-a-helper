package ch.abbts.application.dto

import ch.abbts.domain.model.AuthProvider
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationDto(
    val email: String,
    val name:String,
    val password: String? = null,
    val token: String? = null,
    val zipCode:Int?=null,
    val birthdate:String? = null,
    val authenticationProvider: AuthProvider
)
