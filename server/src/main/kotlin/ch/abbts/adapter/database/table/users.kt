package ch.abbts.adapter.database.table

import org.jetbrains.exposed.sql.Table

enum class AuthProvider {
    ENUM_LOCAL,
    ENUM_GOOGLE

}


object User: Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255).nullable()
    val email = varchar("email", 255)
    val password_hash = varchar("password", 255).nullable()
    val zip_code=integer("zip_code").nullable()
    val image_url = varchar("image_url", 255).nullable()
    val image = blob("img").nullable()
    val active = bool("active").nullable()
    val loced_until = long("loced_until").nullable()
    val authProvider = enumerationByName("authProvider", 255, AuthProvider::class)
    val birthdate = long("birthdate")
    override val primaryKey = PrimaryKey(id)
}