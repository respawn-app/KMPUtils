@file:Suppress("unused", "NOTHING_TO_INLINE")

package pro.respawn.kmmutils.common

import kotlin.contracts.contract
import kotlin.enums.enumEntries
import kotlin.jvm.JvmName

/**
 * @return Whether this string is valid
 *
 * Examples:
 * - null -> false
 * - "null" -> false
 * - "" -> false
 * - "NULL" -> false
 * - "  " -> false
 */
@Deprecated("Use the function version as it allows for smart-casting", ReplaceWith("this.isValid()"))
@get:JvmName("getIsValid")
public val String?.isValid: Boolean
    get() = !isNullOrBlank() && !equals("null", true)

/**
 * @return Whether this string is valid
 *
 * Examples:
 * - null -> false
 * - "null" -> false
 * - "" -> false
 * - "NULL" -> false
 * - "  " -> false
 */
public inline fun String?.isValid(): Boolean {
    contract {
        returns(true) implies (this@isValid != null)
    }
    return !isNullOrBlank() && !equals("null", true)
}

/**
 * Takes this string only if it [isValid]
 * @see isValid
 */
public fun String?.takeIfValid(): String? = if (isValid()) this else null

/**
 * Check if this String has length in [range]
 */
public infix fun String.spans(range: IntRange): Boolean = length in range

/**
 * Returns true if all chars in [this] are ASCII symbols
 */
public val String.isAscii: Boolean get() = toCharArray().none { it < ' ' || it > '~' }

/**
 * Uses [LazyThreadSafetyMode.NONE] to provide values quicker.
 */
public fun <T> fastLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

/**
 * Returns the next value of this enum (ordered the same way as declared in code, i.e. by `ordinal`).
 *
 * If the current value is the last, returns the first value.
 * @see nextOrNull
 * @see previousOrNull
 */
@ExperimentalStdlibApi
public inline val <reified T : Enum<T>> Enum<T>.next: T get() = enumEntries<T>().let { it[(ordinal + 1) % it.size] }

/**
 * Returns the next value of this enum (ordered the same way as declared in code, i.e. by `ordinal`).
 *
 * If the current value is the last, returns `null`.
 * @see next
 * @see previous
 */
@ExperimentalStdlibApi
public inline val <reified T : Enum<T>> Enum<T>.nextOrNull: T? get() = enumEntries<T>().getOrNull(ordinal + 1)

/**
 * Returns the previous value of this enum (ordered the same way as declared in code, i.e. by `ordinal`).
 *
 * If the current value is the first, returns the last value.
 * @see previousOrNull
 * @see next
 */
@ExperimentalStdlibApi
public inline val <reified T : Enum<T>> Enum<T>.previous: T
    get() = enumEntries<T>().let { it[(ordinal.takeIfNotZero() ?: it.size) - 1] }

/**
 * Returns the previous value of this enum (ordered the same way as declared in code, i.e. by `ordinal`).
 *
 * If the current value is the first, returns `null`.
 * @see next
 * @see previous
 */
@ExperimentalStdlibApi
public inline val <reified T : Enum<T>> Enum<T>.previousOrNull: T? get() = enumEntries<T>().getOrNull(ordinal - 1)

/**
 * Calls [requireNotNull] on this value and returns it
 */
public fun <T> T?.requireNotNull(): T & Any = requireNotNull(this)

/**
 * Calls [requireNotNull] on this value and returns it
 */
public inline fun <T> T?.requireNotNull(lazyMessage: () -> Unit): T & Any = requireNotNull(this, lazyMessage)

/**
 * If this is an [Error], throws it, otherwise returns [this] as an [Exception]
 */
public fun Throwable?.rethrowErrors(): Exception? = this?.let { it as? Exception? ?: throw it }

/**
 * If this is an [Error], throws it, otherwise returns [this] as an [Exception]
 */
@JvmName("rethrowErrorsNotNull")
public fun Throwable.rethrowErrors(): Exception = this as? Exception? ?: throw this
