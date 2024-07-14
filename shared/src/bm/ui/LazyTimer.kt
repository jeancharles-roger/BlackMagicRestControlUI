package bm.ui

import kotlinx.coroutines.*
import kotlin.time.Duration

/**
 * A lazy timer executes a block after a delay.
 * If it's started again before the delay, the current block is cancelled and
 * a new one starts again from zero.
 */
class LazyTimer<T>(
    val scope: CoroutineScope,
    /** Waits duration before launching the block */
    val wait: Duration,
    /** Ran each time a timer is done */
    val onDone: (T) -> Unit = {},
    /** block to execute after time */
    val block: suspend () -> T
) {

    private class Timer<T>(
        val scope: CoroutineScope,
        val wait: Duration,
        val block: suspend () -> T
    ) {
        var job: Job? = null
        var cancelled = false

        fun cancel() {
            cancelled = true
        }

        fun start(parent: LazyTimer<T>) {
            job = scope.launch {
                delay(wait)
                if (!cancelled) {
                    val result = block()
                    parent.onDone(result)
                }
            }
        }
    }

    private var current: Timer<T>? = null

    fun start() {
        current?.cancel()
        current = Timer(scope, wait, block)
        current?.start(this)
    }

    fun running() = current != null

    suspend fun join() {
        current?.job?.join()
    }
}