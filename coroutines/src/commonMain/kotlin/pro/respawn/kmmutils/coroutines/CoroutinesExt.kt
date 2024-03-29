package pro.respawn.kmmutils.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Launch a new coroutine using [this] scope and [context],
 * but catch any [Throwables] using [onError] and execute [block].
 *The coroutines launched inside [block]'s scope will NOT cancel the scope.
 */
public fun CoroutineScope.launchCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onError: CoroutineContext.(Throwable) -> Unit,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    val handler = CoroutineExceptionHandler { it, e ->
        onError(it, e)
    }
    val scope = this + handler + SupervisorJob()
    return scope.launch(context, start, block)
}

/**
 * Execute [block] in parallel using operator async for each element of the collection
 */
public suspend fun <T> Iterable<T>.forEachParallel(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend (T) -> Unit,
): Unit = withContext(context) {
    map { async(context, start) { block(it) } }.forEach { it.await() }
}

/**
 * Execute [block] in parallel using operator async for each element of the collection
 */
public suspend fun <T, R> Iterable<T>.mapParallel(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend (T) -> R,
): List<R> = withContext(context) {
    map { async(context, start) { block(it) } }.map { it.await() }
}

/**
 * Maps values of each collection [this] flow emits.
 */
public inline fun <T, R> Flow<Iterable<T>>.mapValues(
    crossinline transform: (T) -> R
): Flow<List<R>> = map { it.map(transform) }
