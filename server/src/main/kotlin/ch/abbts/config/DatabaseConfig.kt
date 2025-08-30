package ch.abbts.config

import ch.abbts.utils.getEnvVariableWithFallback
import ch.abbts.utils.logger
import com.typesafe.config.ConfigFactory
import org.jetbrains.exposed.sql.Database

object DatabaseConfig {
    fun init() {
        val log = logger()
        val config = ConfigFactory.load()
        val user = config.getEnvVariableWithFallback("MYSQL_USER", "db.user")
        val password =
            config.getEnvVariableWithFallback("MYSQL_PASSWORD", "db.password")
        val databaseHost =
            config.getEnvVariableWithFallback("DB_HOST", "db.host")
        val databasePort =
            config.getEnvVariableWithFallback("DB_PORT", "db.port")
        val databaseName = config.getString("db.database")
        val dbUrl = "jdbc:mysql://$databaseHost:$databasePort/$databaseName"
        val driver = JWTConfig.config.getString("db.driver")

        log.info(
            "db-user: $user\n pass: $password\n host: $databaseHost\nport: $databasePort"
        )

        Database.connect(
            url = dbUrl,
            driver = driver,
            user = user,
            password = password,
        )
    }
}
