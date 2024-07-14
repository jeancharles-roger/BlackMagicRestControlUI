package bm.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import bm.rest.CameraStatus
import bm.rest.RestConfig
import bm.rest.status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AppContext {

    var rest: RestConfig by mutableStateOf(RestConfig("http://microg2.local/"))

    var status: CameraStatus? by mutableStateOf(null)

    var error: Throwable? by mutableStateOf(null)
    
    private val targetStatus: MutableState<CameraStatus?> = mutableStateOf(null)
    var target: CameraStatus?
        get() = targetStatus.value
        set(value) {
            targetStatus.value = value
        }

    private val statusUpdater = LazyTimer(
        scope = CoroutineScope(context = Dispatchers.IO),
        wait = 500.toDuration(DurationUnit.MILLISECONDS),
        block = {
            try {
                status = rest.status()
                target = status
                error = null
            } catch (t: Throwable) {
                error = t
                status = null
                target = null
            }
        }
    )

    fun updateStatus() {
        statusUpdater.start()
    }

}