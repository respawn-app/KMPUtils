package pro.respawn.kmmutils.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

/**
 * A [Flow] instance that can be retried by calling [retry].
 * Whatever block or flow emission was before will be re-evaluated (re-created) again.
 */
public interface RetryFlow<T> : Flow<T> {

    /**
     * Retry the invocation of this flow. The flow that originally was used with this wrapper will be recreated
     */
    public fun retry(): Job
}

private class RetryFlowImpl<T>(
    private val scope: CoroutineScope,
    private val loader: suspend () -> T,
    private val delegate: MutableSharedFlow<T> = MutableSharedFlow(replay = 1),
) : RetryFlow<T>, Flow<T> by delegate {

    private val mutex = Mutex()

    init {
        retry()
    }

    override fun retry(): Job = scope.launch {
        if (mutex.tryLock()) {
            try {
                delegate.emit(loader())
            } finally {
                mutex.unlock()
            }
        }
    }
}

/**
 * Creates a new [RetryFlow] from this [call] function, evaluated as a cold flow
 */
@OverloadResolutionByLambdaReturnType
public fun <T> CoroutineScope.retry(
    @BuilderInference call: suspend () -> T
): RetryFlow<T> = RetryFlowImpl(this, call)
