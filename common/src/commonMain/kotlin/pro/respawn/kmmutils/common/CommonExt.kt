@file:Suppress("unused")

package pro.respawn.kmmutils.common

import kotlin.jvm.JvmName
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.log10
import kotlin.math.sign

/**
 * The number of digits in this [Int]
 */
public val Int.length: Int
    get() = when (this) {
        0 -> 1
        else -> log10(abs(this).toDouble()).toInt() + 1
    }

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

public fun String?.takeIfValid(): String? = if (isValid) this else null

/**
 * Check if this String has length in [range]
 */
public infix fun String.spans(range: IntRange): Boolean = length in range

public val String.isAscii: Boolean get() = toCharArray().none { it < ' ' || it > '~' }

/**
 * Uses [LazyThreadSafetyMode.NONE] to provide values quicker
 */
public fun <T> fastLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

/**
 * Filter this list by searching for elements that contain [substring],
 * or if string is not [String.isValid], the list itself
 * @param substring a string, that must be [String.isValid]
 * @return a resulting list
 */
public fun List<String>.filterBySubstring(substring: String?): List<String> = if (substring.isValid) asSequence()
    .filter { it.contains(substring!!, true) }
    .toList()
else this

/**
 * Returns the sign of the number, as a char
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
 */
public val Long.signChar: String
    get() {
        return when {
            this < 0L -> "-"
            this > 0L -> "+"
            else -> ""
        }
    }

public fun Int.toStringWithSign(): String = "$sign$absoluteValue"

public val ClosedRange<Int>.size: Int get() = endInclusive - start

public val ClosedRange<Double>.size: Double get() = endInclusive - start

public val ClosedRange<Float>.size: Float get() = endInclusive - start

public val ClosedRange<Long>.size: Long get() = endInclusive - start

@get:JvmName("sizeShort")
public val ClosedRange<Short>.size: Int get() = endInclusive - start

@get:JvmName("sizeByte")
public val ClosedRange<Byte>.size: Int get() = endInclusive - start

public fun Int?.takeIfNotZero(): Int? = takeIf { it != 0 }
public fun Long?.takeIfNotZero(): Long? = takeIf { it != 0L }
public fun Double?.takeIfNotZero(): Double? = takeIf { it != 0.0 }
public fun Float?.takeIfNotZero(): Float? = takeIf { it != 0.0f }
public fun Short?.takeIfNotZero(): Short? = takeIf { it != 0.toShort() }
public fun Byte?.takeIfNotZero(): Byte? = takeIf { it != 0.toByte() }
