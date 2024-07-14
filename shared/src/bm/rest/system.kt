@file:Suppress("PLUGIN_IS_NOT_ENABLED")
package bm.rest

import io.ktor.http.*
import kotlinx.serialization.Serializable

data class SystemResponse(
    val codecFormat: CodecFormat,
    val videoFormat: GetVideoFormat
)

suspend fun RestConfig.getSystem(): SystemResponse = get("/system")

@Serializable
data class SupportedCodecFormats(
    val codecs: List<CodecFormat>
)

suspend fun RestConfig.getSupportedCodecFormats(): SupportedCodecFormats = get("/system/supportedCodecFormats")

@Serializable
data class CodecFormat(
    val codec: String,
    val container: String
)

suspend fun RestConfig.getCodecFormat(): CodecFormat = get("/system/codecFormat")
suspend fun RestConfig.putCodecFormat(value: CodecFormat): HttpStatusCode = put("/system/codecFormat", value)

@Serializable
data class SupportedVideoFormats(
    val formats: List<VideoFormat>
)

suspend fun RestConfig.getSupportedVideoFormats(): SupportedVideoFormats = get("/system/supportedVideoFormats")

typealias FrameRate = String

val frameRates: List<FrameRate> = listOf(
    "23.98",
    "24.00",
    "24",
    "25.00",
    "25",
    "29.97",
    "30.00",
    "30",
    "47.95",
    "48.00",
    "48",
    "50.00",
    "50",
    "59.94",
    "60.00",
    "60",
    "119.88",
    "120.00",
    "120",
)

val FrameRate.index: Int get() = frameRates.indexOf(this)

@Serializable
data class VideoFormat(
    val frameRate: FrameRate,
    val height: Int,
    val width: Int,
    val interlaced: Boolean
)

@Serializable
data class GetVideoFormat(
    val name: String,
    val frameRate: FrameRate,
    val height: Int,
    val width: Int,
    val interlaced: Boolean
)

suspend fun RestConfig.getVideoFormat(): VideoFormat = get("/system/videoFormat")
suspend fun RestConfig.putVideoFormat(value: VideoFormat): HttpStatusCode = put("/system/videoFormat", value)


@Serializable
data class Format(
    val codec: String,
    val frameRate: FrameRate,
    val maxOffSpeedFrameRate: Int,
    val minOffSpeedFrameRate: Int,
    val offSpeedEnabled: Boolean,
    val offSpeedFrameRate: Int,
    val recordResolution: Resolution,
    val sensorResolution: Resolution
)

suspend fun RestConfig.getFormat(): Format = get("/system/format")
suspend fun RestConfig.putFormat(value: Format): HttpStatusCode = put("/system/format", value)

@Serializable
data class Resolution(
    val height: Int,
    val width: Int
)

@Serializable
data class SupportedFormats(
    val supportedFormats: List<SupportedFormat>
)

@Serializable
data class SupportedFormat(
    val codecs: List<String>,
    val frameRates: List<FrameRate>,
    val maxOffSpeedFrameRate: Int,
    val minOffSpeedFrameRate: Int,
    val recordResolution: Resolution,
    val sensorResolution: Resolution,
)

suspend fun RestConfig.getSupportedFormats(): SupportedFormats = get("/system/supportedFormats")
