package application.interactor

import adapter.database.repository.usersRepository
import application.dto.usersDto
import org.mindrot.jbcrypt.BCrypt
import java.time.Instant
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import application.dto.GoogleIdTokenResponse

class usersInteractor(
    private val userRepository: usersRepository,
    private val googleApi: String = "https://oauth2.googleapis.com/tokeninfo?id_token="
) {

    fun createLocalUser(dto: usersDto): Boolean {
        val existing = authenticateLocalUser(dto)
        if (existing) {
            return false
        }

        userRepository.createLocalUser(dto.toModel())
        return true
    }

    fun authenticateLocalUser(user: usersDto): Boolean {
        return userRepository.authenticateLocalUser(user.toModel())
    }

    fun verifyLocalUser(email: String, passwordHash: String): Boolean {
        val user = userRepository.getUserByEmail(email)
        println(user)
        if (user != null) {
            if (user.passwordHash == passwordHash && Instant.now().epochSecond < (user.lockedUntil ?: Long.MAX_VALUE)) {
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
