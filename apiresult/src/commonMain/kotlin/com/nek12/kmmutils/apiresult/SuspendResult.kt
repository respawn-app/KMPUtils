@file:Suppress("MemberVisibilityCanBePrivate", "unused", "NOTHING_TO_INLINE", "TooManyFunctions", "FunctionName")

package com.nek12.kmmutils.apiresult

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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
 * This wrapper does NOT catch exceptions in nested coroutines. Use supervisorScope() for that
 */
suspend inline fun <T> SuspendResult(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend CoroutineScope.() -> T
) = withContext(context) { ApiResult { block() } }

/**
 * Emits [ApiResult.Loading], then executes [call] and [ApiResult]s it.
 */
inline fun <T> ApiResult.Companion.flow(crossinline call: suspend () -> T): Flow<ApiResult<T>> =
    kotlinx.coroutines.flow.flow {
        emit(ApiResult.Loading)
        emit(ApiResult { call() })
    }

fun <T> Flow<T>.asApiResult(): Flow<ApiResult<T>> = map<T, ApiResult<T>> { ApiResult.success(it) }
    .onStart { emit(ApiResult.Loading) }
    .catchExceptions { emit(ApiResult.Error(it)) }

inline fun <T, R> Flow<ApiResult<T>>.mapResults(crossinline transform: suspend (T) -> R): Flow<ApiResult<R>> =
    map { result -> result.map { transform(it) } }
