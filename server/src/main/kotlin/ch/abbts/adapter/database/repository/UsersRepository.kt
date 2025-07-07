package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.User
import ch.abbts.domain.model.usersModel
import ch.abbts.utils.LoggerService
import ch.abbts.domain.model.AuthProvider
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.mindrot.jbcrypt.BCrypt
import ch.abbts.utils.Log
import ch.abbts.error.UserCreationFailed
import ch.abbts.error.UpdatingIssuedTimeFailed

class UsersRepository {
    companion object : Log() {}
    fun createLocalUser(user: usersModel){
        try {
            transaction {
                LoggerService.debugLog(User)
                User.insert {
                    it[email] = user.email
                    it[password_hash] = BCrypt.hashpw(user.passwordHash, BCrypt.gensalt())
                    it[authProvider] = AuthProvider.LOCAL
                    it[birthdate] = user.birthdate
                    it[active] = user.active ?: true
                    it[name] = user.name
                    it[imageUrl] = user.imageUrl
                    it[image] =
                            user.image?.let { bytes ->
                                org.jetbrains.exposed.sql.statements.api.ExposedBlob(bytes)
                            }
                    it[zipCode] = user.zipCode
                }
            }
        } catch (e: Exception) {
            throw UserCreationFailed(e)
        }
    }

    fun updateIssuedTime(email: String, timestamp: Long){
        try {
            transaction {
                User.update({ User.email eq email }) { it[lastTokenIssued] = timestamp }
            }
        } catch (e: Exception) {
            throw UpdatingIssuedTimeFailed()
        }
    }

    fun getUserByEmail(email: String): usersModel? {
        log.debug("fetching user for $email")
        return try {
            transaction {
                User.select { User.email eq email }.singleOrNull()?.let {
                    usersModel(
                            id = it[User.id],
                            email = it[User.email],
                            passwordHash = it[User.password_hash],
                            authProvider = it[User.authProvider],
                            birthdate = it[User.birthdate],
                            active = it[User.active],
                            name = it[User.name],
                            imageUrl = it[User.imageUrl],
                            image = it[User.image]?.bytes,
                            zipCode = it[User.zipCode],
                            lastTokenIssued = it[User.lastTokenIssued],
                            lockedUntil = it[User.lockedUntil]
                    )
                }
            }
        } catch (e: Exception) {
            log.error("Error fetching user by email: ${e.message}")
            null
        }
    }
}
