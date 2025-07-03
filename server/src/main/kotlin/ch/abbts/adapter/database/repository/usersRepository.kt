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

class usersRepository {
    companion object : Log() {}
    fun createLocalUser(user: usersModel): Boolean {
        return try {
            transaction {
                if (User.select { User.email eq user.email }.count() > 0) return@transaction false

                LoggerService.debugLog(User)
                User.insert {
                    it[email] = user.email
                    it[password_hash] = user.passwordHash
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
                true
            }
        } catch (e: Exception) {

            false
        }
    }

    fun updateIssuedTime(email: String, timestamp: Long): Boolean {
        try {
            transaction {
                User.update({ User.email eq email }) { it[lastTokenIssued] = timestamp }
            }
            return true
        } catch (e: Exception) {
            print(e.message)
            return false
        }
    }

    fun authenticateLocalUser(user: usersModel): Boolean {
        return try {
            transaction {
                val userRow =
                        User.select { User.email eq user.email }.singleOrNull()
                                ?: return@transaction false

                if (userRow[User.authProvider] != AuthProvider.LOCAL) return@transaction false

                val rawPassword = user.passwordHash
                val storedHash = userRow[User.password_hash]

                LoggerService.debugLog("Entered password: $rawPassword")
                LoggerService.debugLog("Stored hash: $storedHash")
                val match = BCrypt.checkpw(rawPassword, storedHash)
                LoggerService.debugLog("Password match: $match")

                match
            }
        } catch (e: Exception) {
            false
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
                            lastTokenIssued = it[User.lastTokenIssued]
                    )
                }
            }
        } catch (e: Exception) {
            log.error("Error fetching user by email: ${e.message}")
            null
        }
    }
}
