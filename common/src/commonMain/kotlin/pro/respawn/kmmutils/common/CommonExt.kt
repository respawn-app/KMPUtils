@file:Suppress("unused")

package pro.respawn.kmmutils.common

import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.log10
import kotlin.math.sign

/**
 * @return The number of digits in this [Int]
 */
public val Int.length: Int
    get() = when (this) {
        0 -> 1
        else -> log10(abs(this).toDouble()).toInt() + 1
    }

/**
 * @return 1 if this is true and false otherwise
 */
public fun Boolean.toInt(): Int = if (this) 1 else 0

/**
 * @return Whether this string is valid
 * Examples:
 * - null -> false
 * - "null" -> false
 * - "" -> false
 * - "NULL" -> false
 * - "  " -> false
 */
public val String?.isValid: Boolean
    get() = !isNullOrBlank() && !equals("null", true)

/**
 * Takes this string only if it [isValid]
 * @see isValid
 */
public fun String?.takeIfValid(): String? = if (isValid) this else null

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
 * Filter [this] by searching for elements that contain [substring],
 * or if string is not [String.isValid], the list itself
 * @param substring a string, that must be [String.isValid]
 * @return a resulting list
 */
public fun Iterable<String>.filterBySubstring(substring: String?): List<String> = if (substring.isValid) asSequence()
    .filter { it.contains(substring!!, true) }
    .toList()
else this.toList()

/**
 * Returns the sign of the number, as a char
 * @return either +, - or "" (empty string) if this is 0
 */
public val Int.signChar: String
    get() {
        return when {
            this < 0 -> "-"
            this > 0 -> "+"
            else -> ""
        }
    }

/**
 * Returns the sign of the number, as a char
 * @return either +, - or "" (empty string) if this is 0
 */
public val Float.signChar: String
    get() {
        return when {
            this < 0f -> "-"
            this > 0f -> "+"
            else -> ""
        }
    }

/**
 * Returns the sign of the number, as a char
 * @return either +, - or "" (empty string) if this is 0
 */
public val Double.signChar: String
    get() {
        return when {
            this < 0.0 -> "-"
            this > 0.0 -> "+"
            else -> ""
        }
    }

/**
 * Returns the sign of the number, as a char
 * @return either +, - or "" (empty string) if this is 0
 */
public val Long.signChar: String
    get() {
        return when {
            this < 0L -> "-"
            this > 0L -> "+"
            else -> ""
        }
    }

/**
 * uses [sign] and prepends it to the value of [this]
 */
public fun Int.toStringWithSign(): String = "$sign$absoluteValue"

/**
 * @returns null if [this] is equal to 0
 */
public fun Int?.takeIfNotZero(): Int? = takeIf { it != 0 }

/**
 * @returns null if [this] is equal to 0
 */
public fun Long?.takeIfNotZero(): Long? = takeIf { it != 0L }

/**
 * @returns null if [this] is equal to 0
 */
public fun Double?.takeIfNotZero(): Double? = takeIf { it != 0.0 }

/**
 * @returns null if [this] is equal to 0
 */
public fun Float?.takeIfNotZero(): Float? = takeIf { it != 0.0f }

/**
 * @returns null if [this] is equal to 0
 */
public fun Short?.takeIfNotZero(): Short? = takeIf { it != 0.toShort() }

/**
 * @returns null if [this] is equal to 0
 */
public fun Byte?.takeIfNotZero(): Byte? = takeIf { it != 0.toByte() }

/**
 * @returns null if [this] is NaN or Infinity
 */
public fun Double.takeIfFinite(): Double? = takeIf { it.isFinite() }

/**
 * @returns null if [this] is NaN or Infinity
 */
public fun Float.takeIfFinite(): Float? = takeIf { it.isFinite() }
