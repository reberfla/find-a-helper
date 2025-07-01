package ch.abbts.domain.model

import ch.abbts.adapter.database.repository.usersRepository
import ch.abbts.error.InvalidIssuedTime
import ch.abbts.error.InvalidSecret
import ch.abbts.error.InvalidTokenFormat
import ch.abbts.error.TokenExpired
import ch.abbts.error.TokenNotRecent
import com.typesafe.config.ConfigFactory
import java.time.Instant
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ch.abbts.utils.Log

class JWebToken(email: String) {
    private val validDuration = 43500L // 12h + 5min
    private val config = ConfigFactory.load()
    private val alg = config.getString("jwt.auth.alg")
    private val typ = config.getString("jwt.auth.typ")
    private val iat: Long = Instant.now().epochSecond
    private val exp: Long = iat.plus(validDuration)
    val header = JWebTokenHeader(alg, typ)
    val body = JWebTokenBody(email, iat, exp)
    val token = JWebToken.generateToken(header, body)

    companion object: Log() {
        val b64Encoder = Base64.getUrlEncoder().withoutPadding()
        val b64Decoder = Base64.getUrlDecoder()
        val config = ConfigFactory.load()
        val secret = config.getString("jwt.secret")
        val usersRepository = usersRepository()

        fun generateToken(header: JWebTokenHeader, body: JWebTokenBody): JWT {
            val encodedHeader =
                b64Encoder.encodeToString(
                    Json.encodeToString(header).toByteArray(Charsets.UTF_8)
                )
            val encodedBody =
                b64Encoder.encodeToString(
                    Json.encodeToString(body).toByteArray(Charsets.UTF_8)
                )
            val signature = generateSignature(encodedHeader, encodedBody)
            return JWT("$encodedHeader.$encodedBody.$signature")
        }

        fun generateSignature(header: String, body: String): String {
            val hmac = Mac.getInstance("HmacSHA256")
            val secretKey =
                SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256")
            hmac.init(secretKey)
            val signature = hmac.doFinal("$header.$body".toByteArray(Charsets.UTF_8))
            hmac.reset()
            return b64Encoder.encodeToString(signature)
        }

        fun validateToken(token: String): Unit {
            val tokenSections = token.split(".")
            val header = tokenSections[0]
            val body = tokenSections[1]
            try {
                Json.decodeFromString<JWebTokenHeader>(
                    b64Decoder.decode(header).decodeToString()

                )
            } catch (_: Exception) {
                log.error("Invalid Header Format")
                throw InvalidTokenFormat("header")
            }
            try {
                Json.decodeFromString<JWebTokenBody>(
                    b64Decoder.decode(body).decodeToString()
                )
            } catch (_: Exception){
                log.error("Invalid Body Format")
                throw InvalidTokenFormat("body")
            }
        }

        fun verifyToken(token: String): Unit {
            val tokenSections = token.split(".")
            val tokenHeader = tokenSections[0]
            val tokenBody = tokenSections[1]
            val claims =
                Json.decodeFromString<JWebTokenBody>(
                    b64Decoder.decode(tokenBody).decodeToString()
                )
            val tokenSignature = tokenSections[2]
            if (tokenSignature != JWebToken.generateSignature(tokenHeader, tokenBody)) {
                throw InvalidSecret()
            }
            val user = usersRepository.getUserByEmail(claims.email)
            if (user?.lastTokenIssued != claims.iat) {
                throw TokenNotRecent()
            }
            if (claims.iat > Instant.now().epochSecond || claims.iat < 0) {
                throw InvalidIssuedTime()
            } else if (claims.exp < Instant.now().epochSecond) {
                throw TokenExpired()
            }
        }
    }

    @Serializable
    data class JWebTokenHeader(private val alg: String, private val typ: String) {}

    @Serializable
    data class JWebTokenBody(
        val email: String,
        val iat: Long,
        val exp: Long
    ) {}
}

@Serializable
data class JWT(val JWT: String)
