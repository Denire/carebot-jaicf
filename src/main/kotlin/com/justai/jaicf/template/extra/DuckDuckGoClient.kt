package com.justai.jaicf.template.extra

import com.justai.jaicf.helpers.logging.WithLogger
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class DuckDuckGoResponse(
    @SerialName("AbstractText") val abstractText: String
)


class DuckDuckGoClient() : WithLogger {

    private val url = "https://api.duckduckgo.com/"
    private val client = HttpClient(CIO) {
        expectSuccess = true
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    private fun getDefinitionRaw(query: String): String? {
        try {
            return runBlocking { defineAsync(query) }
        } catch (ex: Exception) {
            logger.warn("Failed to conform", ex)
        }
        return null
    }

    fun getDefinition(query: String): String? {
        val raw = getDefinitionRaw(query) ?: return null
        val rawJson = Json.nonstrict.parse(DuckDuckGoResponse.serializer(), raw)
        logger.info("DuckDuckGo Controller. Query: $query ; response: ${rawJson.abstractText}")
        if (rawJson.abstractText != "") {
            return rawJson.abstractText.replace("\u0301", "")  // replaces accents
        } else return null
    }

    private suspend fun defineAsync(text: String): String? {
        return client.get<String>(url) {
            header("Accept-Language", "ru_RU")
            parameter("q", text)
            parameter("format", "json")
            parameter("t", "carebot")
            parameter("no_html", 1)
        }
    }
}

fun main() {
    val cl = DuckDuckGoClient()
    print(cl.getDefinition("каспаров"))
}

