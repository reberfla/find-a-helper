package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.UsersRepository
import ch.abbts.application.dto.GoogleIdTokenResponse
import ch.abbts.application.dto.UserDto
import ch.abbts.error.*
import ch.abbts.utils.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.time.Instant
import kotlinx.coroutines.runBlocking
import org.mindrot.jbcrypt.BCrypt

class UserInteractor(
    private val userRepository: UsersRepository,
    private val googleApi: String =
        "https://oauth2.googleapis.com/tokeninfo?id_token=",
) {

    companion object : Log() {}

    fun createLocalUser(dto: UserDto) {
        val existing = userRepository.getUserByEmail(dto.email)
        if (existing != null) {
            throw UserAlreadyExists()
        }
        userRepository.createLocalUser(dto.toModel())
    }

    fun verifyLocalUser(email: String, password: String) {
        val user = userRepository.getUserByEmail(email)
        if (user != null) {
            if (!(Instant.now().epochSecond > (user.lockedUntil ?: 0L))) {
                throw UserIsLocked()
            } else if (!BCrypt.checkpw(password, user.passwordHash)) {
                throw InvalidCredentials()
            }
        } else {
            throw UserNotFound()
        }
    }

    fun updateIssuedTime(email: String, timestamp: Long) {
        return userRepository.updateIssuedTime(email, timestamp)
    }

    fun verifyGoogleUser(token: String) {
        log.debug("verifying user with token: $token")
        val email: String? = runBlocking {
            val client = HttpClient(CIO)
            val googleResponse = client.get("$googleApi$token")
            log.debug(googleResponse.body())
            if (googleResponse.status == HttpStatusCode.OK) {
                googleResponse.body<GoogleIdTokenResponse>().email
            } else {
                throw BadResponseFromGoogle()
            }
        }
        if (email != null) {
            val user = userRepository.getUserByEmail(email)
            if (Instant.now().epochSecond < (user?.lockedUntil ?: 0L)) {
                UserIsLocked()
            }
        } else {
            throw NoEmailProvidedByGoogle()
        }
    }
}
