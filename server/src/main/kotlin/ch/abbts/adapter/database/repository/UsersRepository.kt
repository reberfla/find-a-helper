package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.UsersTable
import ch.abbts.domain.model.UserModel
import ch.abbts.error.UpdatingIssuedTimeFailed
import ch.abbts.error.UserCreationFailed
import ch.abbts.utils.logger
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.mindrot.jbcrypt.BCrypt

class UsersRepository {
    val log = logger()

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
        return try {
            transaction {
                val user =
                    UsersTable.select { UsersTable.email eq email }
                        .singleOrNull()
                user?.toUserModel()
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
                UsersTable.select { UsersTable.id eq id }
                    .singleOrNull()
                    ?.let {
                        UserModel(
                            id = it[UsersTable.id],
                            email = it[UsersTable.email],
                            passwordHash = it[UsersTable.password_hash],
                            authProvider = it[UsersTable.authProvider],
                            birthdate = it[UsersTable.birthdate],
                            active = it[UsersTable.active],
                            name = it[UsersTable.name],
                            imageUrl = it[UsersTable.imageUrl],
                            image = it[UsersTable.image]?.bytes,
                            zipCode = it[UsersTable.zipCode],
                            lastTokenIssued = it[UsersTable.lastTokenIssued],
                            lockedUntil = it[UsersTable.lockedUntil],
                        )
                    }
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

    fun updateUser(user: UserModel): UserModel? {
        return try {
            transaction {
                UsersTable.update({ UsersTable.id eq user.id!! }) {
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
            }
            getUserByEmail(user.email)
        } catch (e: Exception) {
            log.error("❌ Error updating user: ${e.message}")
            null
        }
    }
}
