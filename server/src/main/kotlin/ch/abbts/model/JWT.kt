package model

import com.typesafe.config.ConfigFactory
import java.time.Instant
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class JWebToken() {
        companion object {
                val config = ConfigFactory.load()
                val alg = config.getString("jwt.auth.alg")
                val typ = config.getString("jwt.auth.typ")
                val secret = config.getString("jwt.secret")
                val validDuration = 43500L // 12h + 5min
                val b64Encoder = Base64.getUrlEncoder().withoutPadding()
                val b64Decoder = Base64.getUrlDecoder()

                fun generateToken(name: String, email: String): String {
                        val hmac = Mac.getInstance("HmacSHA256")
                        val secret = SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256")
                        hmac.init(secret)
                        val iat: Long = Instant.now().epochSecond
                        val exp: Long = iat.plus(validDuration)
                        val header =
                                b64Encoder.encodeToString(
                                        Json.encodeToString(JWebTokenHeader(alg, typ))
                                                .toByteArray(Charsets.UTF_8)
                                )
                        val body =
                                b64Encoder.encodeToString(
                                        Json.encodeToString(JWebTokenBody(name, email, iat, exp))
                                                .toByteArray(Charsets.UTF_8)
                                )
                        val signature = generateSignature(header, body)
                        hmac.reset()
                        return "$header.$body.$signature"
                }

                fun generateSignature(header: String, body: String): String {
                        val hmac = Mac.getInstance("HmacSHA256")
                        val secret = SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256")
                        hmac.init(secret)
                        val signature = hmac.doFinal("$header.$body".toByteArray(Charsets.UTF_8))
                        hmac.reset()
                        return b64Encoder.encodeToString(signature)
                }
                fun validateToken(token: String): Unit {
                        val tokenSections = token.split(".")
                        val header = tokenSections[0]
                        val body = tokenSections[1]
                        val headerContent =
                                Json.decodeFromString<JWebTokenHeader>(
                                        b64Decoder.decode(header).decodeToString()
                                )
                        val bodyContent =
                                Json.decodeFromString<JWebTokenBody>(
                                        b64Decoder.decode(body).decodeToString()
                                )
                }
                fun verifyToken(token: String): Boolean {
                        val tokenSections = token.split(".")
                        val tokenHeader = tokenSections[0]
                        val tokenBody = tokenSections[1]
                        val claims =
                                Json.decodeFromString<JWebTokenBody>(
                                        b64Decoder.decode(tokenBody).decodeToString()
                                )
                        val tokenSignature = tokenSections[2]
                        val signature = b64Decoder.decode(tokenBody).decodeToString()
                        if (signature != JWebToken.generateSignature(tokenHeader, tokenBody)) {
                                return true
                        } else {
                                return false
                        }
                }
        }
}

@Serializable data class JWebTokenHeader(private val alg: String, private val typ: String) {}

@Serializable
data class JWebTokenBody(
        private val name: String,
        private val email: String,
        private val iat: Long,
        private val exp: Long
) {}
