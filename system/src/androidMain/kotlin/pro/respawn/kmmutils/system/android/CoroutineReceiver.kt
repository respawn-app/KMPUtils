package pro.respawn.kmmutils.system.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.seconds

/**
 * A [BroadcastReceiver] that calls [goAsync] and launches a coroutine to execute long-running tasks.
 * The [receive] method is still executed on main thread, move to another thread as needed.
 *
 * According to Android limitations, you still have about **10 seconds** to finish the execution before
 * the system forcibly kills the receiver. For longer tasks, use WorkManager or a Service
 */
public abstract class CoroutineReceiver : BroadcastReceiver() {

    protected val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        val result = goAsync()
        scope.launch {
            try {
                withTimeout(MaxDuration) { receive(context, intent) }
            } catch (e: TimeoutCancellationException) {
                throw IllegalStateException(Message, e)
            } finally {
                result.finish()
            }
        }
    }

    /**
     * Still executed on main thread.
     * Maximum duration for execution is about 10 seconds, after which the system will kill the broadcast receiver.
     */
    protected abstract suspend fun receive(context: Context, intent: Intent)

    private companion object {

        private val MaxDuration = 10.seconds
        private val Message = """
            CoroutineReceiver has been suspended for more than ${MaxDuration.inWholeSeconds}
            You cannot execute CoroutineReceiver for a long time because the system will kill it.
            If you want to execute long-running tasks, launch a worker from your receiver.
        """.trimIndent()
    }
}
