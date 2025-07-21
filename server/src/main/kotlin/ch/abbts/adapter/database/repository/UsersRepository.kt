package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.User
import ch.abbts.domain.model.UserModel
import ch.abbts.error.UpdatingIssuedTimeFailed
import ch.abbts.error.UserCreationFailed
import ch.abbts.utils.Log
import ch.abbts.utils.LoggerService
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.mindrot.jbcrypt.BCrypt

class UsersRepository {
    companion object : Log()

    fun createUser(user: UserModel): UserModel? {
        try {
            val userModel = transaction {
                User.insert {
                        it[email] = user.email
                        it[password_hash] =
                            BCrypt.hashpw(user.passwordHash, BCrypt.gensalt())
                        it[authProvider] = user.authProvider
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
                    .resultedValues
                    ?.firstOrNull()
                    ?.toUserModel()
            }
            return userModel
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
        LoggerService.debugLog("fetching user for in UserRespo $email")
        return try {
            transaction {
                val user = User.select { User.email eq email }.singleOrNull()
                LoggerService.debugLog(user.toString())
                user?.toUserModel()
            }
        } catch (e: Exception) {
            log.error("Error fetching user by email: ${e.message}")
            null
        }
    }

    private fun ResultRow.toUserModel(): UserModel {
        return UserModel(
            id = this[User.id],
            email = this[User.email],
            name = this[User.name],
            passwordHash = this[User.password_hash],
            zipCode = this[User.zipCode],
            imageUrl = this[User.imageUrl],
            image = this[User.image]?.bytes,
            active = this[User.active],
            authProvider = this[User.authProvider],
            birthdate = this[User.birthdate],
            lastTokenIssued = this[User.lastTokenIssued],
            lockedUntil = this[User.lockedUntil],
        )
    }

    fun updateUser(user: UserModel): UserModel? {
        return try {
            transaction {
                User.update({ User.id eq user.id!! }) {
                    it[email] = user.email
                    it[password_hash] = BCrypt.hashpw(user.passwordHash, BCrypt.gensalt())
                    it[authProvider] = user.authProvider
                    it[birthdate] = user.birthdate
                    it[active] = user.active ?: true
                    it[name] = user.name
                    it[imageUrl] = user.imageUrl
                    it[image] = user.image?.let { bytes -> org.jetbrains.exposed.sql.statements.api.ExposedBlob(bytes) }
                    it[zipCode] = user.zipCode
                }
            }
             getUserByEmail(user.email)
        } catch (e: Exception) {
            log.error("❌ Error updating user: ${e.message}")
            null
        }
    }

}
