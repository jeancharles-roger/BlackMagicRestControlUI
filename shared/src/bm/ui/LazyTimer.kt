package bm.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

/**
 * A lazy timer executes a block after a delay.
 * If it's started again before the delay, the current block is cancelled and
 * a new one starts again from zero.
 */
class LazyTimer(
    val scope: CoroutineScope,
    /** Waits duration before launching the block*/
    val wait: Duration,
    /** block to execute after time */
    val block: suspend () -> Unit
) {

    private class Timer(
        val scope: CoroutineScope,
        val wait: Duration,
        val block: suspend () -> Unit
    ) {
        var job: Job? = null
        var cancelled = false

        fun cancel() {
            cancelled = true
        }

        fun start() {
            job = scope.launch {
                delay(wait)
                if (!cancelled) block()
            }
        }
    }

    private var current: Timer? = null

    fun start() {
        current?.cancel()
        current = Timer(scope, wait, block)
        current?.start()
    }

    suspend fun join() {
        current?.job?.join()
    }
}