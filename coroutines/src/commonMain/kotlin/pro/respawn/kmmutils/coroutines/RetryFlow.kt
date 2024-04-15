package pro.respawn.kmmutils.coroutines

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * A [Flow] instance that can be retried by calling [retry].
 * Whatever block or flow emission was before will be re-evaluated (re-created) again.
 */
public interface RetryFlow<T> : Flow<T> {

    public fun retry()
}

/**
 * Creates a new [RetryFlow] from [flow] builder
 */
public fun <T> retryFlow(flow: suspend () -> Flow<T>): RetryFlow<T> = RetryFlowImpl(flow)

/**
 * Creates a new [RetryFlow] from this [call] function, evaluated as a cold flow
 */
public inline fun <T> retry(crossinline call: suspend () -> T): RetryFlow<T> = RetryFlowImpl({ flow { emit(call()) } })

@PublishedApi
internal class RetryFlowImpl<T>(
    producer: suspend () -> Flow<T>,
    private val delegate: Channel<Unit> = Channel(Channel.CONFLATED),
) : RetryFlow<T>, Flow<T> by delegate.receiveAsFlow().flatMapLatest(transform = { producer() }) {

    init {
        retry()
    }

    override fun retry() {
        delegate.trySend(Unit)
    }
}
