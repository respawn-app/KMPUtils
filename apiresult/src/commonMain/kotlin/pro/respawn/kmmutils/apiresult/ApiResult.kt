@file:OptIn(ExperimentalContracts::class)
@file:Suppress(
    "MemberVisibilityCanBePrivate",
    "unused",
    "NOTHING_TO_INLINE",
    "TooManyFunctions",
    "ThrowingExceptionsWithoutMessageOrCause"
)

package pro.respawn.kmmutils.apiresult

import pro.respawn.kmmutils.apiresult.ApiResult.Error
import pro.respawn.kmmutils.apiresult.ApiResult.Loading
import pro.respawn.kmmutils.apiresult.ApiResult.Success
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName

/**
 * An exception that is thrown when an attempt to retrieve a result of an [ApiResult] is being made when the
 * result is [Loading]
 */
public class NotFinishedException(
    message: String? = "ApiResult is still in Loading state",
) : IllegalArgumentException(message)

/**
 * Exception representing unsatisfied condition when using [errorIf]
 */
public class ConditionNotSatisfiedException(
    message: String? = "ApiResult condition was not satisfied",
) : IllegalArgumentException(message)

/**
 * A class that represents a result of an operation.
 * Create an instance with [ApiResult.invoke] and use various operators on the resulting objects.
 * This class is **extremely efficient**: no actual objects are created,
 * all operations are inlined and no function resolution is performed.
 * ApiResult is **not** an Rx-style callback chain -
 * the operators that are invoked are called **immediately** and in-place.
 */
public sealed interface ApiResult<out T> {

    /**
     * A loading state of an [ApiResult]
     */
    public data object Loading : ApiResult<Nothing> {

        override fun toString(): String = "ApiResult.Loading"
    }

    /**
     * A value of [ApiResult] for its successful state.
     * @param result a successful result value
     */
    @JvmInline
    public value class Success<out T>(public val result: T) : ApiResult<T> {

        override fun toString(): String = "ApiResult.Success: $result"
    }

    /**
     * The state of [ApiResult] that represents an error.
     * @param e wrapped [Exception]
     */
    @JvmInline
    public value class Error(public val e: Exception) : ApiResult<Nothing> {

        /**
         * [e]'s message.
         */
        public val message: String? get() = e.message

        override fun toString(): String = "ApiResult.Error: message=$message and cause: $e"

        /**
         * Gets current stack trace as string
         */
        public fun asStackTrace(): String = e.stackTraceToString()
    }

    /**
     * Whether this is [Success]
     */
    public val isSuccess: Boolean get() = this is Success

    /**
     *  Whether this is [Error]
     */
    public val isError: Boolean get() = this is Error

    /**
     * Whether this is [Loading]
     */
    public val isLoading: Boolean get() = this is Loading

    public companion object {

        /**
         * Execute [call], catching any exceptions, and wrap it in an [ApiResult].
         * Caught exceptions are mapped to [ApiResult.Error]s.
         * [Throwable]s are not caught on purpose.
         * [CancellationException]s are rethrown.
         */
        public inline operator fun <T> invoke(call: () -> T): ApiResult<T> = try {
            Success(call())
        } catch (e: CancellationException) {
            throw e
        } catch (expected: Exception) {
            Error(expected)
        }

        /**
         *  * If T is an exception, will produce [ApiResult.Error]
         *  * If T is Loading, will produce [ApiResult.Loading]
         *  * Otherwise [ApiResult.Success]<T>
         */
        public inline operator fun <T> invoke(value: T): ApiResult<T> = when (value) {
            is Loading -> value
            is Exception -> Error(value)
            else -> Success(value)
        }

        /**
         * Returns an ApiResult(Unit) value.
         * Use this for applying operators such as `require` and `mapWrapping` to build chains of operators that should
         * start with an empty value.
         */
        public inline operator fun invoke(): ApiResult<Unit> = ApiResult(Unit)
    }
}

/**
 * Execute [block] wrapping it in an [ApiResult]
 * @see ApiResult.invoke
 */
public inline fun <T, R> T.runResulting(block: T.() -> R): ApiResult<R> = ApiResult { block() }

/**
 * Executes [block], wrapping it in an [ApiResult]
 * @see ApiResult.invoke
 */
public inline fun <T> runResulting(block: () -> T): ApiResult<T> = ApiResult { block() }

/**
 * Executes [block] if [this] is an [ApiResult.Error], otherwise returns [ApiResult.Success.result]
 * [Loading] will result in [NotFinishedException]
 */
public inline infix fun <T, R : T> ApiResult<T>.orElse(block: (e: Exception) -> R): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Success -> result
        is Error -> block(e)
        is Loading -> block(NotFinishedException())
    }
}

/**
 *  If [this] is [Error], returns [defaultValue].
 *  @see orElse
 */
public inline infix fun <T, R : T> ApiResult<T>.or(defaultValue: R): T = orElse { defaultValue }

/**
 * @return null if [this] is an [ApiResult.Error] or [ApiResult.Loading], otherwise return self.
 */
public inline fun <T> ApiResult<T>.orNull(): T? = or(null)

/**
 * @return exception if [this] is [Error] or null
 */
public inline fun <T> ApiResult<T>.exceptionOrNull(): Exception? = (this as? Error)?.e

/**
 * Throws [ApiResult.Error.e], or [NotFinishedException] if the request has not been completed yet.
 */
public inline fun <T> ApiResult<T>.orThrow(): T = when (this) {
    is Loading -> throw NotFinishedException()
    is Error -> throw e
    is Success -> result
}

/**
 * Fold [this] returning the result of [onSuccess] or [onError]
 * By default, maps [Loading] to [Error] with [NotFinishedException]
 */
public inline fun <T, R> ApiResult<T>.fold(
    onSuccess: (result: T) -> R,
    onError: (e: Exception) -> R,
    noinline onLoading: (() -> R)? = null,
): R {
    contract {
        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onError, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Success -> onSuccess(result)
        is Error -> onError(e)
        is Loading -> onLoading?.invoke() ?: onError(NotFinishedException())
    }
}

/**
 * Invoke a given [block] if [this] is [Error]
 * @see onSuccess
 * @see onLoading
 */
public inline fun <T> ApiResult<T>.onError(block: (Exception) -> Unit): ApiResult<T> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return apply {
        if (this is Error) block(e)
    }
}

/**
 * Invoke a given block if [this] is [Error] and it's [Error.e] is of type [E].
 */
@JvmName("onErrorTyped")
public inline fun <reified E : Exception, T> ApiResult<T>.onError(block: (E) -> Unit): ApiResult<T> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return apply {
        if (this is Error && e is E) block(e)
    }
}

/**
 * Invoke a given [block] if [this] is [Success]
 * @see onError
 * @see onLoading
 */
public inline fun <T> ApiResult<T>.onSuccess(block: (T) -> Unit): ApiResult<T> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return apply { if (this is Success) block(result) }
}

/**
 * Invoke given [block] if [this] is [Loading]
 * @see onError
 * @see onSuccess
 */
public inline fun <T> ApiResult<T>.onLoading(block: () -> Unit): ApiResult<T> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return apply { if (this is Loading) block() }
}

/**
 * Makes [this] an [Error] if [predicate] returns false
 * @see errorIf
 */
public inline fun <T> ApiResult<T>.errorIfNot(
    exception: () -> Exception = { ConditionNotSatisfiedException() },
    predicate: (T) -> Boolean,
): ApiResult<T> = errorIf(exception) { !predicate(it) }

/**
 * Makes [this] an [Error] if [predicate] returns true
 * @see errorIfNot
 */
public inline fun <T> ApiResult<T>.errorIf(
    exception: () -> Exception = { ConditionNotSatisfiedException() },
    predicate: (T) -> Boolean,
): ApiResult<T> {
    contract {
        callsInPlace(predicate, InvocationKind.AT_MOST_ONCE)
        callsInPlace(exception, InvocationKind.AT_MOST_ONCE)
    }
    return if (this is Success && predicate(result)) Error(exception()) else this
}

/**
 * Change the type of the [Success] to [R] without affecting [Error]/[Loading] results
 * @see mapError
 * @see map
 * @see mapWrapping
 */
public inline infix fun <T, R> ApiResult<T>.map(block: (T) -> R): ApiResult<R> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Success -> Success(block(result))
        is Error -> Error(e)
        is Loading -> this
    }
}

/**
 * Maps [Loading] to a [Success], not affecting other states.
 * @see mapError
 * @see map
 * @see mapWrapping
 */
public inline infix fun <T, R : T> ApiResult<T>.mapLoading(block: () -> R): ApiResult<T> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Success, is Error -> this
        is Loading -> Success(block())
    }
}

/**
 * Change the exception of the [Error] response without affecting loading/success results
 */
public inline infix fun <T, R : Exception> ApiResult<T>.mapError(block: (Exception) -> R): ApiResult<T> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Success, is Loading -> this
        is Error -> Error(block(e))
    }
}

/**
 * Maps the error of the result, if present, to its cause, or self if cause is not available
 */
public inline fun <T> ApiResult<T>.mapErrorToCause(): ApiResult<T> = mapError { it.cause as? Exception ?: it }

/**
 * Unwrap an ApiResult<ApiResult<T>> to be ApiResult<T>
 */
public inline fun <T> ApiResult<ApiResult<T>>.unwrap(): ApiResult<T> = fold(
    onSuccess = { it },
    onError = { Error(it) },
    onLoading = { Loading }
)

/**
 * Change the type of successful result to [R], also wrapping [block]
 * in another result then folding it (handling exceptions)
 * @see map
 * @see mapError
 * @see mapLoading
 */
public inline infix fun <T, R> ApiResult<T>.tryMap(block: (T) -> R): ApiResult<R> =
    map { ApiResult { block(it) } }.unwrap()

/**
 * Change the type of successful result to [R], also wrapping [block]
 * in another result then folding it (handling exceptions)
 * @see map
 * @see mapError
 * @see mapLoading
 */
@Deprecated("use tryMap", ReplaceWith("this.tryMap<T,R>(block)"))
public inline infix fun <T, R> ApiResult<T>.mapWrapping(block: (T) -> R): ApiResult<R> = tryMap(block)

/**
 * Make this result an [Error] if [Success] value was null.
 * @see errorIfNot
 * @see errorIf
 * @see errorIfEmpty
 */
public inline fun <T> ApiResult<T?>.errorOnNull(
    exception: () -> Exception = { ConditionNotSatisfiedException("Value was null") },
): ApiResult<T & Any> = errorIf(exception) { it == null }.map { requireNotNull(it) }

/**
 * Maps [Error] values to nulls
 * @see orNull
 */
public inline fun <T> ApiResult<T>.nullOnError(): ApiResult<T?> = if (this is Error) Success(null) else this

/**
 * Recover from an exception of type [R], else no-op.
 * Does not affect [Loading].
 *
 * Overload for a lambda that already returns an [ApiResult].
 * @see recover
 */
public inline infix fun <reified T : Exception, R> ApiResult<R>.recover(another: (e: T) -> ApiResult<R>): ApiResult<R> =
    when (this) {
        is Success, is Loading -> this
        is Error -> if (e is T) another(e) else this
    }

@Deprecated("use tryRecover", ReplaceWith("this.tryRecover<T, R>(block)"))
public inline infix fun <reified T : Exception, R> ApiResult<R>.recoverWrapping(
    block: (T) -> R
): ApiResult<R> = tryRecover<T, R>(block)

/**
 * calls [recover] catching and wrapping any exceptions thrown inside [block].
 */
public inline infix fun <reified T : Exception, R> ApiResult<R>.tryRecover(block: (T) -> R): ApiResult<R> =
    when (this) {
        is Success, is Loading -> this
        is Error -> if (e is T) ApiResult { block(e) } else this
    }

/**
 * Recover from an [Error] only if the [condition] is true, else no-op.
 * Does not affect [Loading]
 * @see recover
 */
public inline fun <T> ApiResult<T>.recoverIf(
    condition: (Exception) -> Boolean,
    block: (Exception) -> T
): ApiResult<T> {
    contract {
        callsInPlace(condition, InvocationKind.AT_MOST_ONCE)
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Success, is Loading -> this
        is Error -> if (condition(e)) Success(block(e)) else this
    }
}

/**
 * Call [another] and retrieve the result.
 * If the result is success, continue (**the result of calling [another] is discarded**).
 * If the result is an error, propagate it to [this].
 * Effectively, requires for another [ApiResult] to succeed before proceeding with this one.
 * @see [ApiResult.then]
 */
public inline infix fun <T> ApiResult<T>.chain(another: (T) -> ApiResult<*>): ApiResult<T> {
    contract {
        callsInPlace(another, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Loading, is Error -> this
        is Success -> another(result).fold(
            onSuccess = { this },
            onError = { Error(it) },
        )
    }
}

/**
 * Call [block], wrapping it in an [ApiResult], and then discard the [Success] value, but propagate [Error]s.
 *
 * If the result is success, continue (**the result of calling [block] is discarded**).
 * If the result is an error, propagate it to [this].
 *
 * Alias for [chain] for calls that do not return an ApiResult already.
 * @see [ApiResult.chain]
 * @see [ApiResult.then]
 */
public inline fun <T> ApiResult<T>.tryChain(block: (T) -> Unit): ApiResult<T> =
    chain(another = { ApiResult { block(it) } })

/**
 * Call [another] and if it succeeds, continue with [another]'s result.
 * If it fails, propagate the error.
 * Effectively, map to another result.
 * @see [ApiResult.chain]
 */
public inline infix fun <T, R> ApiResult<T>.then(another: (T) -> ApiResult<R>): ApiResult<R> {
    contract {
        callsInPlace(another, InvocationKind.AT_MOST_ONCE)
    }
    return map(another).unwrap()
}

/**
 * Makes [this] an error with [IllegalArgumentException] using specified [message] if the [predicate] returns false
 */
public inline fun <T> ApiResult<T>.require(
    message: () -> String? = { null },
    predicate: (T) -> Boolean
): ApiResult<T> =
    errorIfNot(
        exception = { IllegalArgumentException(message()) },
        predicate = predicate,
    )

/**
 * Alias for [errorOnNull]
 * @see errorOnNull
 */
public inline fun <T> ApiResult<T?>.requireNotNull(): ApiResult<T & Any> = errorOnNull()
