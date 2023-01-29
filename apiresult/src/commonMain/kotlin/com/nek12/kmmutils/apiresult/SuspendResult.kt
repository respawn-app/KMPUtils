@file:Suppress("MemberVisibilityCanBePrivate", "unused", "NOTHING_TO_INLINE", "TooManyFunctions", "FunctionName")

package pro.respawn.kmmutils.apiresult

import pro.respawn.kmmutils.apiresult.ApiResult.Error
import pro.respawn.kmmutils.apiresult.ApiResult.Loading
import pro.respawn.kmmutils.apiresult.ApiResult.Success
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Catches [Exception]s only and rethrows [kotlin.Throwable]s (like [kotlin.Error]s).
 */
inline fun <T> Flow<T>.catchExceptions(
    crossinline action: suspend FlowCollector<T>.(Exception) -> Unit
) = catch { action(it as? Exception ?: throw it) }

/**
 * Run [block] with a given [context], catching any exceptions both in the block and nested coroutines.
 * [block] will **not** return until all nested launched coroutines, if any, return.
 * For the other type of behavior, use [ApiResult.invoke] directly.
 * A failure of a child does not cause the scope to fail and does not affect its other children.
 * A failure of the scope itself (exception thrown in the block or external cancellation)
 * fails the result with all its children, but does not cancel parent job.
 * @see ApiResult.invoke
 * @see supervisorScope
 * @see kotlinx.coroutines.SupervisorJob
 */
suspend inline fun <T> SuspendResult(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline block: suspend CoroutineScope.() -> T,
) = withContext(context) { ApiResult { supervisorScope(block) } }

/**
 * Emits [ApiResult.Loading], then executes [call] and [ApiResult]s it.
 * @see Flow.asApiResult
 */
inline fun <T> ApiResult.Companion.flow(
    crossinline call: suspend CoroutineScope.() -> T
) = kotlinx.coroutines.flow.flow {
    emit(Loading)
    emit(SuspendResult { call() })
}

/**
 * Emits [Loading] before this flow starts to be collected.
 * Then maps all values to [Success] and catches [Exception]s and maps them to [Error]s
 * @see ApiResult.Companion.flow
 * @see SuspendResult
 */
fun <T> Flow<T>.asApiResult() = this
    .map { ApiResult(it) }
    .onStart { emit(Loading) }
    .catchExceptions { emit(Error(it)) }

inline fun <T, R> Flow<ApiResult<T>>.mapResults(
    crossinline transform: suspend (T) -> R
) = map { result -> result.map { transform(it) } }

/**
 * Throws [CancellationException]s if this is an [Error]
 */
inline fun <T> ApiResult<T>.rethrowCancellation() = recover<CancellationException, T> { throw it }
