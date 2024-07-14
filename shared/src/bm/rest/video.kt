@file:Suppress("PLUGIN_IS_NOT_ENABLED")
package bm.rest

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class Iso(val iso: Int)

val isos: List<Int> = listOf(200, 400, 800, 1600, )

suspend fun RestConfig.getIso(): Iso = get("/video/iso")
suspend fun RestConfig.putIso(value: Iso): HttpStatusCode = put("/video/iso", value)

@Serializable
data class Gain(val gain: Int)
suspend fun RestConfig.getGain(): Gain = get("/video/gain")
suspend fun RestConfig.putGain(value: Gain): HttpStatusCode = put("/video/gain", value)

@Serializable
data class Shutter(
    val shutterAngle: Int,
    val continuousShutterAutoExposure: Boolean = false
)
suspend fun RestConfig.getShutter(): Shutter = get("/video/shutter")
suspend fun RestConfig.putShutter(value: Shutter): HttpStatusCode = put("/video/shutter", value)

@Serializable
data class WhiteBalance(
    val whiteBalance: Int
)
suspend fun RestConfig.getWhiteBalance(): WhiteBalance = get("/video/whiteBalance")
suspend fun RestConfig.putWhiteBalance(value: WhiteBalance): HttpStatusCode = put("/video/whiteBalance", value)
suspend fun RestConfig.putWhiteBalanceDoAuto(): HttpStatusCode = put("/video/whiteBalance/doAuto")

@Serializable
data class WhiteBalanceTint(
    val whiteBalanceTint: Int /* -50 to 50 */
)
suspend fun RestConfig.getWhiteBalanceTint(): WhiteBalanceTint = get("/video/whiteBalanceTint")
suspend fun RestConfig.putWhiteBalanceTint(value: WhiteBalanceTint): HttpStatusCode = put("/video/whiteBalanceTint", value)
