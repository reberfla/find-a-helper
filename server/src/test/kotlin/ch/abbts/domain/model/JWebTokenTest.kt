package ch.abbts.domain.model

import ch.abbts.error.InvalidTokenFormat
import java.util.*
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.Test

class JWebTokenTest {

    @Test
    fun testTokenFormat() {
        val header = JWebToken.JWebTokenHeader(alg = "HS256", typ = "JWT")
        val body =
            JWebToken.JWebTokenBody("mail@mail.com", 1, iat = 0L, exp = 3L)

        val token = JWebToken.generateToken(header, body)
        JWebToken.validateToken(token.jwt)
    }

    @Test
    fun testInvalidTokenFormat() {
        val randomHeader =
            Base64.getUrlEncoder().encodeToString("header".toByteArray())
        val randomBody =
            Base64.getUrlEncoder().encodeToString("body".toByteArray())
        val randomSignature =
            Base64.getUrlEncoder().encodeToString("signature".toByteArray())
        val randomToken = "$randomHeader.$randomBody.$randomSignature"
        assertFailsWith<InvalidTokenFormat>(
            message = "invalid token header format",
            block = { JWebToken.validateToken(randomToken) },
        )
        val jwtBuilder = JWebToken("email@email.com", 1)
        val realToken =
            JWebToken.generateToken(jwtBuilder.header, jwtBuilder.body)
        val tokenWithRealHeader =
            "${realToken.jwt.split(".")[0]}.$randomBody.$randomSignature"
        assertFailsWith<InvalidTokenFormat>(
            message = "invalid token body format",
            block = { JWebToken.validateToken(tokenWithRealHeader) },
        )
    }
}
