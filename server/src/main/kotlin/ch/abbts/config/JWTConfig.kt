package ch.abbts.config

import ch.abbts.utils.getEnvVariableWithFallback
import com.typesafe.config.ConfigFactory

object JWTConfig {
    val config = ConfigFactory.load()
    val secret = config.getEnvVariableWithFallback("JWT_SECRET", "jwt.secret")
}
