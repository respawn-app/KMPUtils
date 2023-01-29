@file:Suppress("MemberVisibilityCanBePrivate", "unused", "NOTHING_TO_INLINE", "TooManyFunctions", "FunctionName")

package pro.respawn.kmmutils.apiresult

import pro.respawn.kmmutils.apiresult.ApiResult.Error
import pro.respawn.kmmutils.apiresult.ApiResult.Success

inline fun <T> ApiResult<Collection<T>>.orEmpty() = or(emptyList())
inline fun <T> ApiResult<List<T>>.orEmpty() = or(emptyList())

inline fun <T> ApiResult<Set<T>>.orEmpty() = or(emptySet())
inline fun <T> ApiResult<Sequence<T>>.orEmpty() = or(emptySequence())

inline fun <T, R> Iterable<ApiResult<T>>.mapResults(transform: (T) -> R) = map { it.map(transform) }
inline fun <T, R> Sequence<ApiResult<T>>.mapResults(crossinline transform: (T) -> R) = map { it.map(transform) }

inline fun <T> Iterable<ApiResult<T>>.mapErrors(transform: (Exception) -> Exception) = map { it.mapError(transform) }
inline fun <T> Sequence<ApiResult<T>>.mapErrors(
    crossinline transform: (Exception) -> Exception,
) = map { it.mapError(transform) }

inline fun <T> Iterable<ApiResult<T>>.filterErrors() = filterIsInstance<Error>()
inline fun <T> Sequence<ApiResult<T>>.filterErrors() = filterIsInstance<Error>()

inline fun <T> Iterable<ApiResult<T>>.filterSuccesses() = filterIsInstance<Success<T>>()
inline fun <T> Sequence<ApiResult<T>>.filterSuccesses() = filterIsInstance<Success<T>>()

inline fun <T> Iterable<ApiResult<T?>>.filterNulls() =
    filter { it !is Success || it.result != null }.mapResults { it!! }

inline fun <T> Sequence<ApiResult<T?>>.filterNulls() =
    filter { it !is Success || it.result != null }.mapResults { it!! }

inline fun <T, R : Collection<T>> ApiResult<R>.errorIfEmpty(
    exception: () -> Exception = { ConditionNotSatisfiedException("Collection was empty") },
) = errorIf(exception) { it.isEmpty() }

@JvmName("sequenceErrorIfEmpty")
inline fun <T, R : Sequence<T>> ApiResult<R>.errorIfEmpty(
    exception: () -> Exception = { ConditionNotSatisfiedException("Sequence was empty") },
) = errorIf(exception) { it.none() }

/**
 * Executes [ApiResult.map] on each value of the collection
 */
inline fun <T, R> ApiResult<Iterable<T>>.mapValues(transform: (T) -> R) = map { it.map(transform) }

/**
 * Executes [ApiResult.map] on each value of the sequence
 */
@JvmName("sequenceMapValues")
inline fun <T, R> ApiResult<Sequence<T>>.mapValues(noinline transform: (T) -> R) = map { it.map(transform) }
