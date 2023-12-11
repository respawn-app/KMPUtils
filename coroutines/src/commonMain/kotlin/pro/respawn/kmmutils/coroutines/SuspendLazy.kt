package pro.respawn.kmmutils.coroutines

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.updateAndGet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Use this like a [lazy], but because operator getValue cannot
 * be suspend, you'll have to invoke this object instead in a
 * suspend context to receive the value.
 *
 * Use when you want a lazy that is loaded via a suspend fun
 * and you use it in a suspend fun which can tolerate loading
 * the value on a miss.
 */
public interface SuspendLazy<T> {

    /**
     * Obtain the value of [SuspendLazy]. Will invoke the initializer on first try.
     *
     * Like the regular lazy, will attempt to run the initializer if the initializer has thrown the last time.
     */
    public suspend operator fun invoke(): T
}

/**
 * The mode in which the [SuspendLazy]  will operate
 */
public enum class SuspendLazyMode {

    /**
     * Handle the initialization of the suspend lazy type using an atomic variable.
     * This allows multiple coroutines to request the value, but only one will start the initialization
     */
    Atomic,

    /**
     * Run the initializer of the suspend lazy using a mutex. This will not allow parallel access from neither threads
     * or coroutines, but will be slower than [Atomic]
     */
    Mutex
}

private class AtomicSuspendLazy<T>(
    private val block: suspend CoroutineScope.() -> T,
) : SuspendLazy<T> {

    private val value = atomic<Deferred<T>?>(null)

    @Suppress("Indentation") // conflicts with IDE formatting
    override suspend operator fun invoke(): T = (
            value.value ?: coroutineScope {
                value.updateAndGet { actual ->
                    actual ?: async { block() }
                }!!
            }
            ).await()
}

private class MutexSuspendLazy<T>(
    private val block: suspend CoroutineScope.() -> T,
) : SuspendLazy<T> {

    private val mutex = Mutex()
    private var value: Any? = Uninitialized

    @Suppress("UNCHECKED_CAST")
    override suspend fun invoke() = mutex.withLock {
        coroutineScope {
            if (value == Uninitialized) value = block()
            value as T
        }
    }

    private data object Uninitialized
}

/**
 * Use this like a lazy, but because operator getValue cannot
 * be suspend, you'll have to invoke this object instead in a
 * suspend context to receive the value.
 *
 * Use when you want a lazy that is loaded via a suspend fun
 * and you use it in a suspend fun which can tolerate loading
 * the value on a miss.
 */
public fun <T> suspendLazy(
    type: SuspendLazyMode = SuspendLazyMode.Atomic,
    initializer: suspend CoroutineScope.() -> T,
): SuspendLazy<T> = when (type) {
    SuspendLazyMode.Atomic -> AtomicSuspendLazy(initializer)
    SuspendLazyMode.Mutex -> MutexSuspendLazy(initializer)
}
