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
val JSON = Json {
    encodeDefaults = false
    ignoreUnknownKeys = true
}


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

    fun dirtyCut(s: String, len: Int = 280): String {
        val firstDot = s.indexOf('.')
        if (firstDot == -1) { return s }
        if (firstDot > len) {
            return s.substring(0, firstDot+1)
        } else if (s.length > len) {
            val shortS = s.substring(0, len)
            val dotIndex = shortS.lastIndexOf('.')
            return shortS.substring(0, dotIndex+1)
        }
        else return s
    }
    
    fun getDefinition(query: String): String? {
        val raw = getDefinitionRaw(query) ?: return null
        val rawJson = JSON.decodeFromString(DuckDuckGoResponse.serializer(), raw)
        if (rawJson.abstractText != "") {
            val abstractT = dirtyCut(rawJson.abstractText.replace("\u0301", "").replace("•",""))  // replaces accents
            logger.info("query: $query; response (cut): $abstractT")
            return abstractT
        } else
        {
            logger.info("no response to query: $query")
            return null
        }
    }

    private suspend fun defineAsync(text: String): String {
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
//    print(cl.clearForTTS("13-й чемпион мира по шахматам"))
}

