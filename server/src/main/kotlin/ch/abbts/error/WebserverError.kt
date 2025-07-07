package ch.abbts.error
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

abstract class WebserverError(override val message: String, val statusCode: Int): Throwable() {
    fun getStatus(): HttpStatusCode {
        return HttpStatusCode.fromValue(statusCode)
    }

    fun getMessage(): JsonObject {
        return buildJsonObject{
            put("message", message)
        }
    }
}
