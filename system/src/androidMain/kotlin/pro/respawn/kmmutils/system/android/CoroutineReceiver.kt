package pro.respawn.kmmutils.system.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.seconds

/**
 * A [BroadcastReceiver] that calls [goAsync] and launches a coroutine to execute long-running tasks.
 * The [receive] method is still executed on main thread, move to another thread as needed.
 *
 * According to Android limitations, you have about **10 seconds** to finish the execution before
 * the system forcibly kills the receiver. For longer tasks, use WorkManager or a Service
 */
public abstract class CoroutineReceiver : BroadcastReceiver() {

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        val result = goAsync()
        scope.launch {
            try {
                withTimeoutOrNull(MaxDuration) {
                    receive(context, intent)
                } ?: onTimeout(IllegalStateException(Message))
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

    protected open fun onTimeout(exception: Exception): Unit = throw exception

    private companion object {

        private val MaxDuration = 10.seconds
        private val Message = """
            CoroutineReceiver has been suspended for more than $MaxDuration
            You cannot execute code in broadcast receivers for longer periods of time due to OS constraints. 
            If you want to execute long-running tasks, launch a worker from your receiver.
        """.trimIndent()
    }
}
