@file:Suppress("PLUGIN_IS_NOT_ENABLED")
package bm.rest

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class Iris(
    val continuousApertureAutoExposure: Boolean,
    val apertureStop: Float,
    val normalised: Float,
    val apertureNumber: Float
)

suspend fun RestConfig.getIris(): Iris = get("/lens/iris")
suspend fun RestConfig.putIris(value: Iris): HttpStatusCode = put("/lens/iris", value)

@Serializable
data class Zoom(
    val focalLength: Int,
    val normalised: Float,
)

suspend fun RestConfig.getZoom(): Zoom = get("/lens/zoom")
suspend fun RestConfig.putZoom(value: Zoom): HttpStatusCode = put("/lens/zoom", value)

@Serializable
data class Focus(
    val focus: Float,
)

suspend fun RestConfig.getFocus(): Focus = get("/lens/focus")
suspend fun RestConfig.putFocus(value: Focus): HttpStatusCode = put("/lens/focus", value)
suspend fun RestConfig.putDoAutoFocus(): HttpStatusCode = put("/lens/focus/doAutoFocus")
