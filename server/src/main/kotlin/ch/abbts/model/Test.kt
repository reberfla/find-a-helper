package models

import io.github.tabilzad.ktor.annotations.KtorSchema
import kotlinx.serialization.Serializable

@Serializable
@KtorSchema("This is a test request")
data class Test(val message: String)
