package bm.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bm.rest.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.DurationUnit
import kotlin.time.toDuration


@Composable
fun MainView(app: AppContext) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Camera")
            TextField(
                value = app.rest.base,
                onValueChange = { app.rest = RestConfig(it) }
            )
            Button(onClick = { app.updateStatus() }) {
                Text("Refresh")
            }
        }

        val scope = rememberCoroutineScope()

        val target = app.target
        if (target != null) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                WhiteBalanceSlider(app, scope, target)
                TintSlider(app, scope, target)
                Column(
                    modifier = Modifier.weight(1f / 3f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FrameRateSlider(app, scope, target)
                IrisSlider(app, scope, target)
                ShutterSlider(app, scope, target)
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IsoSelection(app, scope, target)
            }
        } else {
            val error = app.error
            if (error != null) {
                Text(
                    text = "${error::class.simpleName}: ${error.message}",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text("Not connected")
            }
        }
    }
}

@Composable
private fun RowScope.WhiteBalanceSlider(
    app: AppContext,
    scope: CoroutineScope,
    target: CameraStatus,
    weight: Float = 1f / 3f,
) {
    Column(
        modifier = Modifier.weight(weight).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("White Balance")
        val whiteBalance = target.whiteBalance.whiteBalance
        Text("${whiteBalance}K")
        Slider(
            value = whiteBalance.toFloat(),
            onValueChange = {
                scope.launch {
                    val value = WhiteBalance(it.toInt())
                    app.rest.putWhiteBalance(value)
                    app.target = target.copy(whiteBalance = value)
                    app.updateStatus()
                }
            },
            valueRange = 2500f..10000f,
            steps = 10000 - 2500
        )
    }
}


@Composable
private fun RowScope.TintSlider(
    app: AppContext,
    scope: CoroutineScope,
    target: CameraStatus,
    weight: Float = 1f / 3f,
) {
    Column(
        modifier = Modifier.weight(weight).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tint")
        val tint = target.tint.whiteBalanceTint
        Text("$tint")
        Slider(
            value = tint.toFloat(),
            onValueChange = {
                scope.launch {
                    val value = WhiteBalanceTint(it.toInt())
                    app.rest.putWhiteBalanceTint(value)
                    app.target = target.copy(tint = value)
                    app.updateStatus()
                }
            },
            valueRange = -50f..50f,
            steps = 50 - (-50)
        )

    }
}


@Composable
private fun RowScope.FrameRateSlider(
    app: AppContext,
    scope: CoroutineScope,
    target: CameraStatus,
    weight: Float = 1f / 3f,
) {
    Column(
        modifier = Modifier.weight(weight).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Frame Rate")
        val frameRate = target.format.frameRate
        val index = frameRate.index
        val localFrameRates = target.supported.supportedFormats.firstOrNull()?.frameRates ?: listOf("24")
        Text(frameRate)
        Slider(
            value = index.toFloat(),
            onValueChange = {
                scope.launch {
                    val newIndex = it.toInt()
                    val newFramerate = localFrameRates[newIndex]
                    val value = target.format.copy(frameRate = newFramerate)
                    app.rest.putFormat(value)
                    app.target = target.copy(format = value)
                    app.updateStatus()
                }
            },
            valueRange = 0f..(localFrameRates.size - 1).toFloat(),
            steps = localFrameRates.size
        )

    }
}


@Composable
private fun RowScope.IrisSlider(
    app: AppContext,
    scope: CoroutineScope,
    target: CameraStatus,
    weight: Float = 1f / 3f,
) {
    Column(
        modifier = Modifier.weight(weight).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iris")
        val focal = target.zoom.focalLength
        Text("f$focal")
        Slider(
            value = focal.toFloat(),
            onValueChange = {
                // TODO iris control
                /*
                scope.launch {
                    val value = WhiteBalanceTint(it.toInt())
                    app.rest.putWhiteBalanceTint(value)
                    app.target = target.copy(tint = value)
                    app.updateStatus()
                }
                */
            },
            valueRange = 0f..1000f,
            steps = 1000
        )

    }
}

@Composable
private fun RowScope.ShutterSlider(
    app: AppContext,
    scope: CoroutineScope,
    target: CameraStatus,
    weight: Float = 1f / 3f,
) {
    Column(
        modifier = Modifier.weight(weight).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Shutter")
        val shutter = target.shutter.shutterAngle
        Text(buildString {
            append(shutter / 100)
            append(".")
            append(shutter % 100)
            append("°")
        })
        Slider(
            value = (shutter / 100).toFloat(),
            onValueChange = {
                scope.launch {
                    val value = target.shutter.copy(shutterAngle = it.toInt() * 100)
                    app.rest.putShutter(value)
                    app.target = target.copy(shutter = value)
                    app.updateStatus()
                }
            },
            valueRange = 1f..360f,

            steps = 360 - 1
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RowScope.IsoSelection(
    app: AppContext,
    scope: CoroutineScope,
    target: CameraStatus,
    weight: Float = 1f,
) {
    Column(
        modifier = Modifier.weight(weight).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iso")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val iso = target.iso.iso
            val isoWeight = 1f / isos.size
            isos.forEach { current ->
                val selected = iso == current
                Box(
                    modifier = Modifier
                        .weight(isoWeight)
                        .padding(4.dp)
                        .border(
                            width = if (selected) 2.dp else 1.dp,
                            color = if (selected) Color.Blue else Color.Black
                        )
                        .padding(10.dp)
                        .clickable {
                            scope.launch {
                                val value = target.iso.copy(iso = current)
                                app.rest.putIso(value)
                                app.target = target.copy(iso = value)
                                app.updateStatus()
                            }
                        }
                ) {
                    Text(
                        text = "$current",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }
}

