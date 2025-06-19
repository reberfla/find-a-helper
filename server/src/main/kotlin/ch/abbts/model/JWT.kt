package models

import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class JWebToken() {
    companion object {
        val alg = "HS256"
        val typ = "JWT"
        val sub = "1234567890"
        val name = "John Doe"
        val iat = 1516239022L
        val secret = "a-string-secret-at-least-256-bits-long"

        fun generateToken(): String {
            val hmac = Mac.getInstance("HmacSHA256")
            val secret = SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256")
            hmac.init(secret)
            val header =
                    Base64.getUrlEncoder()
                            .withoutPadding()
                            .encodeToString(
                                    Json.encodeToString(JWebTokenHeader(alg, typ)).toByteArray(Charsets.UTF_8)
                            )
            val body =
                    Base64.getUrlEncoder()
                            .withoutPadding()
                            .encodeToString(
                                    Json.encodeToString(JWebTokenBody(sub, name, iat)).toByteArray(Charsets.UTF_8)
                            )
            val signature = hmac.doFinal("$header.$body".toByteArray(Charsets.UTF_8))
            return "$header.$body.${Base64.getUrlEncoder().withoutPadding().encodeToString(signature)}"
        }
    }
}

@Serializable data class JWebTokenHeader(private val alg: String, private val typ: String) {}

@Serializable
data class JWebTokenBody(
        private val sub: String,
        private val name: String,
        private val iat: Long
) {}
