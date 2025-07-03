package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.AuthProvider
import ch.abbts.adapter.database.table.User
import ch.abbts.domain.model.usersModel
import ch.abbts.utils.LoggerService
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt


class usersRepository  {
     fun createLocalUser(user: usersModel): Boolean {
        return try {
            transaction {
                if (User.select { User.email eq user.email }.count() > 0) return@transaction false

                LoggerService.debugLog(User)
                User.insert {
                    it[email] = user.email
                    it[password_hash] = user.password_hash
                    it[authProvider] = AuthProvider.ENUM_LOCAL
                    it[birthdate] = user.birthdate
                    it[active] = user.active ?: true
                    it[name] = user.name
                    it[image_url] = user.image_url
                    it[image] = user.image?.let { bytes -> org.jetbrains.exposed.sql.statements.api.ExposedBlob(bytes) }
                }
                true
            }
        } catch (e: Exception) {

            false
        }
    }

    fun authenticateLocalUser(user: usersModel): Boolean {
        return try {
            transaction {
                val userRow = User.select { User.email eq user.email }.singleOrNull() ?: return@transaction false

                if (userRow[User.authProvider] != AuthProvider.ENUM_LOCAL) return@transaction false

                val rawPassword = user.password_hash
                val storedHash = userRow[User.password_hash]

                LoggerService.debugLog("Entered password: $rawPassword")
                LoggerService.debugLog("Stored hash: $storedHash")
                val match = BCrypt.checkpw(rawPassword, storedHash)
                LoggerService.debugLog("Password match: $match")

                match
            }
        } catch (e: Exception) {
            println("authenticateLocalUser error: ${e.message}")
            false
        }
    }





}
