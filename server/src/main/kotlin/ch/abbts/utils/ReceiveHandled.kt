package ch.abbts.utils

import ch.abbts.error.InvalidBody
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveText
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

suspend inline fun <reified T : Any> ApplicationCall.receiveHandled(): T {
    try {
        return Json.decodeFromString<T>(receiveText())
    } catch (e: SerializationException) {
        val missingFieldRegex =
            """(?<messageStart>.+')(\w+\.)(?<type>\w+.)+(?<messageEnd>.+)"""
                .toRegex()
        val wrongEnumValueRegex = """(\w+\.)+(?<message>.+)""".toRegex()
        val additionalFieldRegex = """unknown key '\w+'""".toRegex()
        val missingField = missingFieldRegex.matchEntire(e.message!!)
        val wrongEnumValue = wrongEnumValueRegex.matchEntire(e.message!!)
        val additionalField = additionalFieldRegex.find(e.message!!)
        if (missingField != null) {
            val groups = missingField.groups
            throw InvalidBody(
                "${groups["messageStart"]?.value}${groups["type"]?.value}${groups["messageEnd"]?.value}"
            )
        } else if (wrongEnumValue != null) {
            throw InvalidBody("${wrongEnumValue.groups["message"]?.value}")
        } else if (additionalField != null) {
            throw InvalidBody(additionalField.value)
        } else {
            throw InvalidBody(e.message!!)
        }
    }
}
