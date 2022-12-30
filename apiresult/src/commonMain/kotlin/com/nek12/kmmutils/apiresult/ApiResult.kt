@file:Suppress("MemberVisibilityCanBePrivate", "unused", "NOTHING_TO_INLINE", "TooManyFunctions")

package com.nek12.kmmutils.apiresult

import com.nek12.kmmutils.apiresult.ApiResult.Success
import com.nek12.kmmutils.apiresult.ApiResult.Error
import com.nek12.kmmutils.apiresult.ApiResult.Loading

class NotFinishedException(
    message: String? = "ApiResult is still in Loading state",
) : IllegalArgumentException(message)

class ConditionNotSatisfiedException(
    message: String? = "ApiResult condition was not satisfied",
) : IllegalArgumentException(message)

/**
 * A class that wraps a result of a network call.
 */
sealed class ApiResult<out T> {

    /**
     * Use this to indicate result loading state
     */
    object Loading : ApiResult<Nothing>() {

        override fun toString(): String = "ApiResult.Loading"
    }

    /**
     * Request has been performed successfully
     */
    data class Success<out T>(val result: T) : ApiResult<T>() {

        override fun toString(): String = "ApiResult.Success: result=$result"
    }

    /**
     * There was an error completing the request.
     */
    data class Error(val e: Exception) : ApiResult<Nothing>() {

        val message get() = e.message

        override fun toString(): String = "ApiResult.Error: message=$message and cause: $e"
    }

    val isSuccess get() = this is Success
    val isError get() = this is Error
    val isLoading get() = this is Loading

    companion object {

        /**
         * Execute [call] catching any exceptions.
         * Throwables are not caught on purpose.
         */
        inline fun <T> wrap(call: () -> T): ApiResult<T> = try {
            Success(call())
        } catch (expected: Exception) {
            Error(expected)
        }

        /**
         * If T is an exception, will produce ApiResult.Error, otherwise ApiResult.Success<T>
         */
        inline fun <T> of(value: T): ApiResult<T> = if (value is Exception) Error(value) else Success(value)

        inline fun <T> success(value: T) = Success(value)

        inline fun <T> error(e: Exception) = Error(e)

        inline fun loading() = Loading
    }
}

fun <R, T : R> ApiResult<T>.or(defaultValue: R): R = orElse { defaultValue }

inline fun <T> ApiResult<List<T>>.orEmpty(): List<T> = or(emptyList())

inline fun <T> ApiResult<Set<T>>.orEmpty(): Set<T> = or(emptySet())

inline fun <T> ApiResult<Collection<T>>.orEmpty(): Collection<T> = or(emptyList())

inline fun <T> ApiResult<T>.orNull(): T? = or(null)

/**
 * Throws [ApiResult.Error.e], or [NotFinishedException] if the request has not been completed yet.
 */
inline fun <T> ApiResult<T>.orThrow(): T = when (this) {
    is Loading -> throw NotFinishedException()
    is Error -> throw e
    is Success -> result
}

/**
 * [Loading] will result in [NotFinishedException]
 */
inline fun <R, T : R> ApiResult<T>.orElse(action: (e: Exception) -> R): R = when (this) {
    is Success -> result
    is Error -> action(e)
    is Loading -> action(NotFinishedException())
}

/**
 * By default, maps [Loading] to Error witn [NotFinishedException]
 */
inline fun <T, R> ApiResult<T>.fold(
    onSuccess: (result: T) -> R,
    onError: (exception: Exception) -> R,
    noinline onLoading: (() -> R)? = null,
): R = when (this) {
    is Success -> onSuccess(result)
    is Error -> onError(e)
    is Loading -> onLoading?.let { it() } ?: onError(NotFinishedException())
}

inline fun <T> ApiResult<T>.onError(block: (Exception) -> Unit): ApiResult<T> {
    if (this is Error) block(e)
    return this
}

inline fun <T> ApiResult<T>.onSuccess(block: (T) -> Unit): ApiResult<T> {
    if (this is Success) block(result)
    return this
}

inline fun <T> ApiResult<T>.onLoading(block: () -> Unit): ApiResult<T> {
    if (this is Loading) block()
    return this
}

/**
 * Makes the result an error if [predicate] returns false
 */
inline fun <T> ApiResult<T>.errorIfNot(
    exception: Exception = ConditionNotSatisfiedException(),
    predicate: (T) -> Boolean,
): ApiResult<T> = errorIf(exception) { !predicate(it) }

/**
 * Makes this result an error if [predicate] returns true
 */
inline fun <T> ApiResult<T>.errorIf(
    exception: Exception = ConditionNotSatisfiedException(),
    predicate: (T) -> Boolean,
): ApiResult<T> = if (this is Success && predicate(result)) Error(exception) else this

/**
 * Change the type of the [Success] to [R] without affecting error/loading results
 */
inline fun <T, R> ApiResult<T>.map(block: (T) -> R): ApiResult<R> = when (this) {
    is Success -> Success(block(result))
    is Error -> Error(e)
    is Loading -> this
}

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
    is Error -> Error(block(e))
    is Loading -> this
}

/**
 * Maps [Loading] to a [Success], not touching anything else
 */
inline fun <R, T : R> ApiResult<T>.mapLoading(block: () -> R): ApiResult<R> = when (this) {
    is Success -> this
    is Error -> this
    is Loading -> Success(block())
}

/**
 * Fix situations where you have ApiResult<ApiResult<T>>
 */
inline fun <T> ApiResult<ApiResult<T>>.unwrap(): ApiResult<T> = fold(
    { it },
    { Error(it) },
    { Loading }
)

/**
 * Change the type of successful result to [R], also wrapping [block] in another result then folding it (handling exceptions)
 */
inline fun <T, R> ApiResult<T>.mapWrapping(block: (T) -> R): ApiResult<R> =
    map { ApiResult.wrap { block(it) } }.unwrap()

/**
 * Make this result an error if [Success] value was null
 */
inline fun <T> ApiResult<T?>.errorOnNull(
    exception: Exception = ConditionNotSatisfiedException("Value was null")
): ApiResult<T> = errorIf(exception) { it == null }.map { requireNotNull(it) }

/**
 * Maps [Error] values to nulls
 */
inline fun <T> ApiResult<T>.nullOnError(): ApiResult<T?> = if (this is Error) Success(null) else this

/**
 * Returns a list containing only [Error] values
 */
inline fun <T> Iterable<ApiResult<T>>.filterErrors() = filterIsInstance<Error>()

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

/**
 * Recover from an exception of type [R], else no-op.
 * Does not affect [Loading]
 */
inline fun <T, reified R : Exception> ApiResult<T>.recover(block: (R) -> T) = when (this) {
    is Success, is Loading -> this
    is Error -> if (e is R) Success(block(e)) else this
}

/**
 * Recover from an [Error] only if the [condition] is true, else no-op.
 * Does not affect [Loading]
 */
inline fun <T> ApiResult<T>.recoverIf(condition: (Exception) -> Boolean, block: (Exception) -> T) =
    when (this) {
        is Success, is Loading -> this
        is Error -> if (condition(e)) Success(block(e)) else this
    }

/**
 * Call [another] and retrieve the result.
 * If the result is success, continue (**the result of calling [another] is discarded**)
 * If the result is an error, propagate it to [this]
 * Effectively, requires for another ApiResult to succeed before proceeding with this one
 * @see [ApiResult.then]
 */
inline fun <T, R> ApiResult<T>.chain(another: (result: T) -> ApiResult<R>) = when (this) {
    is Loading, is Error -> this
    is Success -> another(result).fold(
        onSuccess = { this },
        onError = { Error(it) },
    )
}

/**
 * Call [another] and if it succeeds, continue with [another]'s result.
 * If it fails, propagate the error.
 * Effectively, map to another result.
 * @see [ApiResult.chain]
 */
inline fun <T, R> ApiResult<T>.then(another: (T) -> ApiResult<R>) = map { another(it) }.unwrap()
