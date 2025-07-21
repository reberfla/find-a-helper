package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.UsersTable
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
                UsersTable.insert {
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
                UsersTable.update({ UsersTable.email eq email }) {
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
                val user = UsersTable.select { UsersTable.email eq email }.singleOrNull()
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
            id = this[UsersTable.id],
            email = this[UsersTable.email],
            name = this[UsersTable.name],
            passwordHash = this[UsersTable.password_hash],
            zipCode = this[UsersTable.zipCode],
            imageUrl = this[UsersTable.imageUrl],
            image = this[UsersTable.image]?.bytes,
            active = this[UsersTable.active],
            authProvider = this[UsersTable.authProvider],
            birthdate = this[UsersTable.birthdate],
            lastTokenIssued = this[UsersTable.lastTokenIssued],
            lockedUntil = this[UsersTable.lockedUntil],
        )
    }
}
