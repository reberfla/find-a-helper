package ch.abbts.error
import kotlinx.serialization.Serializable

class InvalidTokenFormat(tokenSection: String): WebserverError("invalid token $tokenSection format", 401)
class InvalidSecret : WebserverError("invalid signature", 401)
class TokenNotRecent: WebserverError("there has been a more recent token issued", 401)
class InvalidIssuedTime: WebserverError("the token's issued at timestamp is invalid", 401)
class InvalidCredentials: WebserverError("wrong email or password", 401)
class InvalidGoogelCredentials: WebserverError("invalid google credentials", 401)
class NoEmailProvidedByGoogle: WebserverError("no email provided by google", 400)
class TokenExpired: WebserverError("token is expired", 401)
class UserIsLocked: WebserverError("user is locked", 401)
class MissingPassword(): WebserverError("authentication method is 'LOCAL' but no password was provided", 400)
class MissingGoogleToken(): WebserverError("authentication method is 'GOOGLE' but no token was provided", 400)
class UpdatingIssuedTimeFailed(): WebserverError("unable to update issued time", 500)
class BadResponseFromGoogle(): WebserverError("google sent a bad response while verifying the token", 500)
