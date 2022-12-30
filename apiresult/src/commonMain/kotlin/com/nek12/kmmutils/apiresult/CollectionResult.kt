@file:Suppress("MemberVisibilityCanBePrivate", "unused", "NOTHING_TO_INLINE", "TooManyFunctions", "FunctionName")

package com.nek12.kmmutils.apiresult

import com.nek12.kmmutils.apiresult.ApiResult.Success

inline fun <T> ApiResult<Collection<T>>.orEmpty(): Collection<T> = or(emptyList())

inline fun <T> ApiResult<List<T>>.orEmpty(): List<T> = or(emptyList())

inline fun <T> ApiResult<Set<T>>.orEmpty(): Set<T> = or(emptySet())

/**
 * Map each [Success] value of given iterable
 */
inline fun <T, R> Iterable<ApiResult<T>>.mapResults(transform: (T) -> R): List<ApiResult<R>> = map { it.map(transform) }

/**
 * Map each [Error] value of given iterable
 */
inline fun <T> Iterable<ApiResult<T>>.mapErrors(transform: (Exception) -> Exception) = map { it.mapError(transform) }

/**
 * Change the exception of the [Error] response without affecting loading/success results
 */
inline fun <T, R : Exception> ApiResult<T>.mapError(block: (Exception) -> R): ApiResult<T> = when (this) {
    is Success -> this
    is ApiResult.Error -> ApiResult.Error(block(e))
    is ApiResult.Loading -> this
}

/**
 * Returns a list containing only [Error] values
 */
inline fun <T> Iterable<ApiResult<T>>.filterErrors() = filterIsInstance<ApiResult.Error>()

/**
 * Returns a list containing only [Success] values
 */
inline fun <T> Iterable<ApiResult<T>>.filterSuccesses() = filterIsInstance<Success<T>>()

/**
 * Returns a new list containing only items that are both [Success] an not null
 */
inline fun <T> Iterable<ApiResult<T?>>.filterNulls() =
    filter { it !is Success || it.result != null }.mapResults { it!! }

/**
 * Maps [Success] values of the sequence
 */
inline fun <T, R> Sequence<ApiResult<T>>.mapResults(crossinline transform: (T) -> R) = map { it.map(transform) }

/**
 * Makes [Success] an [Error] using provided [exception] if the collection is empty
 */
inline fun <T, R : Collection<T>> ApiResult<R>.errorIfEmpty(
    exception: Exception = ConditionNotSatisfiedException("Collection was empty")
) = errorIf(exception) { it.isEmpty() }
