package ch.abbts.application.interactor

import ch.abbts.adapter.database.repository.UsersRepository
import ch.abbts.application.dto.GoogleIdTokenResponse
import ch.abbts.application.dto.UserDto
import ch.abbts.domain.model.AuthProvider
import ch.abbts.domain.model.UserModel
import ch.abbts.error.*
import ch.abbts.utils.Log
import ch.abbts.utils.LoggerService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.mindrot.jbcrypt.BCrypt
import java.time.Instant
import java.time.LocalDate

class UserInteractor(
    private val userRepository: UsersRepository,
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
        val idToken = verifyGoogleIdToken(token)
            ?: throw BadResponseFromGoogle()

        val payload = idToken.payload
        val email = payload["email"] as? String ?: throw NoEmailProvidedByGoogle()
        val name = payload["name"] as? String ?: ""
        val picture = payload["picture"] as? String

        val user: UserModel = userRepository.getUserByEmail(email) ?: run {

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

        return UserDto.toDTO(user)
    }


    fun verifyGoogleIdToken(idTokenString: String): GoogleIdToken? {
        val transport = NetHttpTransport()
        val jsonFactory = GsonFactory.getDefaultInstance()
        val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(
                listOf("1030506683349-po5p0i1593ap5vlur6ffivpcfefka4d7.apps.googleusercontent.com")
            )
            .build()
        return verifier.verify(idTokenString)
    }



}
