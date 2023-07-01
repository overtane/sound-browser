package com.example.soundbrowser.sounddb

import android.util.Log
import com.example.soundbrowser.BuildConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private val API_KEY = BuildConfig.API_KEY
private const val SERVICE = "https://freesound.org/"

private const val DEFAULT_FILTER =
    "duration:[10 TO 60] samplerate:[8000 TO 48000] bitdepth:16 channels:[1 TO 2]"

private const val DEFAULT_FIELDS = "id,name,type,duration,samplerate,channels,images"

private const val TIMEOUT: Long = 6000

object FreeSoundHttpClient {
    private val instance = HttpClient(CIO) {

        install(ContentNegotiation){
            json(Json{
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
            engine {
                requestTimeout = TIMEOUT
            }
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Logger Ktor =>", message)
                }

            }
            level = LogLevel.ALL
        }
        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status:", "${response.status.value}")
            }
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    suspend fun search(query: String): String {
        val response = instance.get(SERVICE) {
            url {
                appendPathSegments( "apiv2", "search", "text")
                parameters.append("token", API_KEY)
                parameters.append("query", query)
                parameters.append("page_size", "15")
                parameters.append("filter", DEFAULT_FILTER)
                parameters.append("fields", DEFAULT_FIELDS)
            }
        }
        return response.bodyAsText()
    }
}