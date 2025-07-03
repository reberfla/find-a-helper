package ch.abbts.error
import kotlinx.serialization.Serializable


@Serializable
sealed class AuthenticationException(override val message: String):Throwable() {}

class InvalidTokenFormat(tokenSection: String): AuthenticationException("invalid token $tokenSection format")
class InvalidSecret : AuthenticationException("invalid signature")
class TokenNotRecent: AuthenticationException("there has been a more recent token issued")
class InvalidIssuedTime: AuthenticationException("the token's issued at timestamp is invalid")
class TokenExpired: AuthenticationException("token is expired")
