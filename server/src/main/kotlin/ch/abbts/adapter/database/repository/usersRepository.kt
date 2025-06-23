package ch.abbts.adapter.database.repository

import ch.abbts.adapter.database.table.AuthProvider
import ch.abbts.adapter.database.table.User
import ch.abbts.domain.model.usersModel
import org.gradle.internal.impldep.com.jcraft.jsch.jbcrypt.BCrypt
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction


class usersRepository  {
     fun createLocalUser(user: usersModel): Boolean {
        return try {
            transaction {
                if (User.select { User.email eq user.email }.count() > 0) return@transaction false

                User.insert {
                    it[email] = user.email
                    it[password_hash] = BCrypt.hashpw(user.password_hash, BCrypt.gensalt())
                    it[authProvider] = AuthProvider.ENUM_LOCAL
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
                BCrypt.checkpw(user.password_hash, userRow[User.password_hash])
            }
        } catch (e: Exception) {
            println("authenticateLocalUser error: ${e.message}")
            false
        }
    }




}
