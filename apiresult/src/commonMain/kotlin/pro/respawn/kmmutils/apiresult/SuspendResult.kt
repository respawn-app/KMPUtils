@file:Suppress("MemberVisibilityCanBePrivate", "unused", "TooManyFunctions", "FunctionName", "NOTHING_TO_INLINE")

package pro.respawn.kmmutils.apiresult

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import pro.respawn.kmmutils.apiresult.ApiResult.Error
import pro.respawn.kmmutils.apiresult.ApiResult.Loading
import pro.respawn.kmmutils.apiresult.ApiResult.Success
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmName

/**
 * Catches [Exception]s only and rethrows [kotlin.Throwable]s (like [kotlin.Error]s).
 */
public inline fun <T> Flow<T>.catchExceptions(
    crossinline action: suspend FlowCollector<T>.(Exception) -> Unit
): Flow<T> = catch { action(it as? Exception ?: throw it) }

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
public suspend inline fun <T> SuspendResult(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline block: suspend CoroutineScope.() -> T,
): ApiResult<T> = withContext(context) { ApiResult { supervisorScope(block) } }

/**
 * Emits [ApiResult.Loading], then executes [call] and [ApiResult]s it.
 * @see Flow.asApiResult
 */
public inline fun <T> ApiResult.Companion.flow(
    crossinline call: suspend () -> T
): Flow<ApiResult<T>> = kotlinx.coroutines.flow.flow {
    emit(Loading)
    emit(ApiResult { call() })
}

/**
 * Emits [ApiResult.Loading], then executes [call]
 * @see Flow.asApiResult
 */
@JvmName("flowWithResult")
public inline fun <T> ApiResult.Companion.flow(
    crossinline call: suspend () -> ApiResult<T>,
): Flow<ApiResult<T>> = kotlinx.coroutines.flow.flow {
    emit(Loading)
    emit(call())
}

/**
 * Emits [Loading] before this flow starts to be collected.
 * Then maps all values to [Success] and catches [Exception]s and maps them to [Error]s
 * @see ApiResult.Companion.flow
 * @see SuspendResult
 */
public inline fun <T> Flow<T>.asApiResult(): Flow<ApiResult<T>> = this
    .map { ApiResult(it) }
    .onStart { emit(Loading) }
    .catchExceptions { emit(Error(it)) }

/**
 * Maps each [Success] value of [this] flow using [transform]
 */
public inline fun <T, R> Flow<ApiResult<T>>.mapResults(
    crossinline transform: suspend (T) -> R
): Flow<ApiResult<R>> = map { result -> result.map { transform(it) } }

/**
 * Throws [CancellationException]s if this is an [Error].
 *
 * Important to use this with coroutines if you're not using [SuspendResult] or [ApiResult.Companion.invoke].
 *
 * [ApiResult.Companion.invoke] already throws [CancellationException]s.
 */
public inline fun <T> ApiResult<T>.rethrowCancellation(): ApiResult<T> =
    recover<CancellationException, T> { throw it }

/**
 * Invokes [block] each time [this] flow emits an [ApiResult.Success] value
 */
public inline fun <T> Flow<ApiResult<T>>.onEachResult(crossinline block: suspend (T) -> Unit): Flow<ApiResult<T>> =
    onEach { result -> result.onSuccess { block(it) } }
