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
public suspend fun <T> Collection<T>.forEachParallel(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.(T) -> Unit,
): Unit = withContext(context) {
    map { async(context, start) { block(it) } }.forEach { it.await() }
}

/**
 * Execute [block] in parallel using operator async for each element of the collection
 */
public suspend fun <A, B> Collection<A>.mapParallel(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.(A) -> B,
): List<B> = withContext(context) {
    map { async(context, start) { block(it) } }.map { it.await() }
}

public inline fun <T, R> Flow<Iterable<T>>.mapValues(
    crossinline transform: (T) -> R
): Flow<List<R>> = map { it.map(transform) }
