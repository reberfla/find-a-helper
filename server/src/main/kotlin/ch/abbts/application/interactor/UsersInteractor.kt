package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.UsersRepository
import ch.abbts.application.dto.GoogleIdTokenResponse
import ch.abbts.application.dto.UsersDto
import ch.abbts.error.InvalidCredentials
import ch.abbts.error.NoEmailProvidedByGoogle
import ch.abbts.error.UserAlreadyExists
import ch.abbts.error.UserNotFound
import ch.abbts.error.UserIsLocked
import ch.abbts.error.BadResponseFromGoogle
import ch.abbts.utils.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.*
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import java.time.Instant
import kotlinx.coroutines.runBlocking
import org.mindrot.jbcrypt.BCrypt

class UsersInteractor(
        private val userRepository: UsersRepository,
        private val googleApi: String = "https://oauth2.googleapis.com/tokeninfo?id_token="
) {

    companion object: Log() {}

    fun createLocalUser(dto: UsersDto) {
        val existing = userRepository.getUserByEmail(dto.email)
        if (existing != null) {
            throw UserAlreadyExists()
        }
        userRepository.createLocalUser(dto.toModel())
    }

    fun verifyLocalUser(email: String, passwordHash: String) {
        val user = userRepository.getUserByEmail(email)
        if (user != null) {
            if (!(Instant.now().epochSecond > (user.lockedUntil ?: 0L))) {
                throw UserIsLocked()
            } else if (!BCrypt.checkpw(passwordHash, user.passwordHash)) {
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
