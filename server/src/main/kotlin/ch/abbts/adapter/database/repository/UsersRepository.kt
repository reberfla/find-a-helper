package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.User
import ch.abbts.domain.model.AuthProvider
import ch.abbts.domain.model.UserModel
import ch.abbts.error.UpdatingIssuedTimeFailed
import ch.abbts.error.UserCreationFailed
import ch.abbts.utils.LoggerService
import ch.abbts.utils.logger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.mindrot.jbcrypt.BCrypt

class UsersRepository {
    val log = logger()

    fun createLocalUser(user: UserModel) {
        try {
            transaction {
                LoggerService.debugLog(User)
                User.insert {
                    it[email] = user.email
                    it[password_hash] =
                        BCrypt.hashpw(user.passwordHash, BCrypt.gensalt())
                    it[authProvider] = AuthProvider.LOCAL
                    it[birthdate] = user.birthdate
                    it[active] = user.active ?: true
                    it[name] = user.name
                    it[imageUrl] = user.imageUrl
                    it[image] =
                        user.image?.let { bytes ->
                            org.jetbrains.exposed.sql.statements.api
                                .ExposedBlob(bytes)
                        }
                    it[zipCode] = user.zipCode
                }
            }
        } catch (e: Exception) {
            throw UserCreationFailed(e)
        }
    }

    fun updateIssuedTime(email: String, timestamp: Long): Unit {
        try {
            transaction {
                User.update({ User.email eq email }) {
                    it[lastTokenIssued] = timestamp
                }
            }
        } catch (e: Exception) {
            throw UpdatingIssuedTimeFailed()
        }
    }

    fun getUserByEmail(email: String): UserModel? {
        log.debug("fetching user for $email")
        return try {
            transaction {
                User.select { User.email eq email }
                    .singleOrNull()
                    ?.let {
                        UserModel(
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
                            lockedUntil = it[User.lockedUntil],
                        )
                    }
            }
        } catch (e: Exception) {
            log.error("Error fetching user by email: ${e.message}")
            null
        }
    }

    fun getUserById(id: Int): UserModel? {
        log.debug("fetching user for $id")
        return try {
            transaction {
                User.select { User.id eq id }
                    .singleOrNull()
                    ?.let {
                        UserModel(
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
                            lockedUntil = it[User.lockedUntil],
                        )
                    }
            }
        } catch (e: Exception) {
            log.error("Error fetching user by email: ${e.message}")
            null
        }
    }
}
