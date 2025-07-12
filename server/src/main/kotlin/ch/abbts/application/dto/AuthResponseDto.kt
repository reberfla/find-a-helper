package ch.abbts.application.dto

import ch.abbts.domain.model.JWT
import kotlinx.serialization.Serializable

@Serializable
class AuthResponseDto (
    val token: JWT,
    val email: String,
    val name: String? = null,
    val imgUrl: String? = null
)