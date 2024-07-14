package bm.rest

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

class RestConfig(
    val base: String,
    val prefix: String = "/control/api/v1"
) {
    val url = "$base$prefix"
    
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
     
    suspend inline fun <reified T> get(path: String): T {
        val response = client.get("$url$path")
        return response.body<T>()
    }
    
    suspend inline fun put(path: String): HttpStatusCode {
        val response = client.put("$url$path")
        return response.status
    }
    
    suspend inline fun <reified T> put(path: String, input: T): HttpStatusCode {
        val response = client.put("$url$path") {
            contentType(ContentType.Application.Json)
            setBody(input)
        }
        return response.status
    }
    
    fun close() {
        client.close()
    }
}

data class CameraStatus(
    val supported: SupportedFormats,
    val format: Format,
    val whiteBalance: WhiteBalance,
    val tint: WhiteBalanceTint,
    val iso: Iso,
    val shutter: Shutter,
    val zoom: Zoom
)

suspend fun RestConfig.status(): CameraStatus = CameraStatus(
    supported = getSupportedFormats(),
    format = getFormat(),
    whiteBalance = getWhiteBalance(),
    tint = getWhiteBalanceTint(),
    iso = getIso(),
    shutter = getShutter(),
    zoom = getZoom()
)