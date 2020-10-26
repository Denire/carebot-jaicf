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
import java.util.regex.Pattern

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

    fun dirtyCut(s: String): String {
        val shortS = s.substring(0, 280)
        val dotIndex = shortS.lastIndexOf('.')
        return shortS.substring(0, dotIndex+1)
    }

    fun getDefinition(query: String): String? {
        val raw = getDefinitionRaw(query) ?: return null
        val rawJson = Json.nonstrict.parse(DuckDuckGoResponse.serializer(), raw)
        logger.info("query: $query ; response: ${rawJson.abstractText}")
        if (rawJson.abstractText != "") {
            return dirtyCut(rawJson.abstractText.replace("\u0301", "").replace("•",""))  // replaces accents
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
    print(cl.getDefinition("путин"))
//    print(cl.clearForTTS("13-й чемпион мира по шахматам"))
}

