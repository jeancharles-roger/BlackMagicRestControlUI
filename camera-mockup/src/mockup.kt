import bm.rest.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    val base = "/control/api/v1"
    embeddedServer(CIO, port = 80) {
        install(ContentNegotiation) {
            json()
        }

        var format = Format(
            codec = "av1",
            frameRate = "23.98",
            maxOffSpeedFrameRate = 200,
            minOffSpeedFrameRate = 10,
            offSpeedEnabled = false,
            offSpeedFrameRate = 200,
            recordResolution = Resolution(320, 200),
            sensorResolution = Resolution(320, 200)
        )

        var whiteBalance = WhiteBalance(12000)
        var whiteBalanceTint = WhiteBalanceTint(0)
        var iso = Iso(800)
        var shutter = Shutter(0)
        var iris = Iris(false, 0f, 0f, 0f)
        var zoom = Zoom(0, 0f)
        
        routing {
            
            // System 
            get("$base/system") {
                call.respond(
                    SystemResponse(
                        CodecFormat("???", "mkv"),
                        GetVideoFormat("format", "23.98", 320, 200, false)
                    )
                )
            }
            get("$base/system/supportedFormats") {
                call.respond(
                    SupportedFormats(
                        listOf()
                    )
                )
            }
            route("$base/system/format") {
                get {
                    call.respond(format)
                }
                put {
                    format = call.receive<Format>()
                }
            }
            
            
            // Video
            route("$base/video/whiteBalance") {
                get {
                    call.respond(whiteBalance)
                }
                put {
                    whiteBalance = call.receive<WhiteBalance>()
                }
            }
            
            route("$base/video/whiteBalanceTint") {
                get {
                    call.respond(whiteBalanceTint)
                }
                put {
                    whiteBalanceTint = call.receive<WhiteBalanceTint>()
                }
            }
            
            route("$base/video/iso") {
                get {
                    call.respond(iso)
                }
                put {
                    iso = call.receive<Iso>()
                }
            }
            
            route("$base/video/iso") {
                get {
                    call.respond(iso)
                }
                put {
                    iso = call.receive<Iso>()
                }
            }
            
            route("$base/video/shutter") {
                get {
                    call.respond(shutter)
                }
                put {
                    shutter = call.receive<Shutter>()
                }
            }
            
            route("$base/video/shutter") {
                get {
                    call.respond(shutter)
                }
                put {
                    shutter = call.receive<Shutter>()
                }
            }
                        
            // Lens
            route("$base/lens/iris") {
                get {
                    call.respond(iris)
                }
                put {
                    iris = call.receive<Iris>()
                }
            }
            route("$base/lens/zoom") {
                get {
                    call.respond(zoom)
                }
                put {
                    zoom = call.receive<Zoom>()
                }
            }
        }
    }.start(wait = true)
}
