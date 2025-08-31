package ch.abbts.utils

import com.typesafe.config.Config

fun Config.getEnvVariableWithFallback(
    envVariable: String,
    fallback: String,
): String {
    return System.getenv(envVariable) ?: this.getString(fallback)
}
