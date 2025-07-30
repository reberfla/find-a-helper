package ch.abbts.adapter.database.table

import ch.abbts.domain.model.AuthProvider
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object User : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val password_hash = varchar("password_hash", 255).nullable()
    val zipCode = integer("zip_code").nullable()
    val imageUrl = varchar("image_url", 255).nullable()
    val image = blob("image").nullable()
    val active = bool("active").nullable()
    val lockedUntil = long("locked_until").nullable()
    val lastTokenIssued = long("last_token_issued").nullable()
    val authProvider =
        enumerationByName("auth_provider", 255, AuthProvider::class)
    val birthdate = date("birthdate")
    override val primaryKey = PrimaryKey(id)
}
