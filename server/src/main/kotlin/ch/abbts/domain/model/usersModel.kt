package ch.abbts.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class usersModel(
    val id: Int? = null,
    val name:String,
    val email: String,
    val password_hash: String,
    val zip_code:Int,
    val image_url:String?="",
    val image: ByteArray?=null,
    val active:Boolean? = true,
    val loced_until:Long? = null,
    val authProvider: String,
    val birthdate:Long
)