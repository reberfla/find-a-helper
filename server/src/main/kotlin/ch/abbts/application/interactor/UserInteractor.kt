package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.UsersRepository
import ch.abbts.application.dto.GoogleIdTokenResponse
import ch.abbts.application.dto.UserDto
import ch.abbts.domain.model.AuthProvider
import ch.abbts.domain.model.UserModel
import ch.abbts.error.*
import ch.abbts.utils.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.mindrot.jbcrypt.BCrypt
import java.time.Instant
import java.time.LocalDate

class UserInteractor(
    private val userRepository: UsersRepository,
    private val googleApi: String = "https://oauth2.googleapis.com/tokeninfo?id_token="
) {

    companion object : Log() {}

    fun createLocalUser(dto: UserDto):UserDto? {
        val existing = userRepository.getUserByEmail(dto.email)
        if (existing != null) {
            throw UserAlreadyExists()
        }
        val newUserModel = userRepository.createUser(dto.toModel())
        return newUserModel?.let { UserDto.toDTO(it) }
    }

    fun verifyLocalUser(email: String, password: String):UserDto {
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
        return user.let { UserDto.toDTO(it) }
    }

    fun updateIssuedTime(email: String, timestamp: Long) {
        return userRepository.updateIssuedTime(email, timestamp)
    }

    fun verifyGoogleUser(token: String): UserDto {
        log.debug("Verifying user with token: $token")

        return runBlocking {
            val client = HttpClient(CIO)
            val googleResponse = client.get("$googleApi$token")

            if (googleResponse.status != HttpStatusCode.OK) {
                throw BadResponseFromGoogle()
            }

            val googleData = googleResponse.body<GoogleIdTokenResponse>()
            val email = googleData.email ?: throw NoEmailProvidedByGoogle()
            val name = googleData.name ?: ""
            val picture = googleData.picture

            val user: UserModel = userRepository.getUserByEmail(email) ?: run {
                log.debug("User not found, creating new Google user: $email")

                val newUser = UserModel(
                    email = email,
                    authProvider = AuthProvider.GOOGLE,
                    birthdate = LocalDate.parse("1991-01-01"),
                    active = true,
                    name = name,
                    imageUrl = picture,
                    image = null,
                    zipCode = -1,
                    lockedUntil = null
                )

                userRepository.createUser(newUser)
                    ?: throw Exception("User creation failed for $email")
            }

            if (Instant.now().epochSecond < (user.lockedUntil ?: 0L)) {
                throw UserIsLocked()
            }

            return@runBlocking UserDto.toDTO(user)
        }
    }


}
