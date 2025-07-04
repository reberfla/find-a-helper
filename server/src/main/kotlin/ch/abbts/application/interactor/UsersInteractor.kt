package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.UsersRepository
import ch.abbts.application.dto.UsersDto
import java.time.Instant
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import ch.abbts.application.dto.GoogleIdTokenResponse
import org.mindrot.jbcrypt.BCrypt

class UsersInteractor(
    private val userRepository: UsersRepository,
    private val googleApi: String = "https://oauth2.googleapis.com/tokeninfo?id_token="
) {

    fun createLocalUser(dto: UsersDto): Boolean {
        val existing = authenticateLocalUser(dto)
        if (existing) {
            return false
        }

        userRepository.createLocalUser(dto.toModel())
        return true
    }

    fun authenticateLocalUser(user: UsersDto): Boolean {
        return userRepository.authenticateLocalUser(user.toModel())
    }

    fun verifyLocalUser(email: String, passwordHash: String): Boolean {
        val user = userRepository.getUserByEmail(email)
        if (user != null) {
            if (BCrypt.checkpw(passwordHash, user.passwordHash) && Instant.now().epochSecond > (user.lockedUntil ?: 0L)) {
                return true
            }
        }
        return false
    }
    fun updateIssuedTime(email: String, timestamp: Long): Boolean {
        return userRepository.updateIssuedTime(email, timestamp)
    }

    fun verifyGoogleUser(token: String): Boolean {
        val client = HttpClient()
        val email: String? = runBlocking {
            val googleResponse = client.get("$googleApi$token")
            if (googleResponse.status == HttpStatusCode.OK) {
                googleResponse.body<GoogleIdTokenResponse>().email
            } else {
                null
            }
        }
        if (email != null) {
            val user = userRepository.getUserByEmail(email)
            if (Instant.now().epochSecond < (user?.lockedUntil ?: Long.MAX_VALUE)) {
                return true
            }
        }
        return false
    }
}
