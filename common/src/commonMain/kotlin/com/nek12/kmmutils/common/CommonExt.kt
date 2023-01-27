@file:Suppress("unused")

package com.nek12.kmmutils.common

import java.math.BigDecimal
import java.sql.Time
import java.util.Locale
import java.util.UUID
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.log10
import kotlin.math.sign

/**
 * The number of digits in this [Int]
 */
val Int.length
    get() = when (this) {
        0 -> 1
        else -> log10(abs(this).toDouble()).toInt() + 1
    }

fun Boolean.toInt(): Int = if (this) 1 else 0

/**
 * @return Whether this string is valid
 * Examples:
 * - null -> false
 * - "null" -> false
 * - "" -> false
 * - "NULL" -> false
 * - "  " -> false
 */
val String?.isValid: Boolean
    get() = !isNullOrBlank() && !equals("null", true)

fun String?.takeIfValid(): String? = if (isValid) this else null

/**
 * Check if this String has length in [range]
 */
infix fun String.spans(range: IntRange) = length in range

fun Float.format(digits: Int, locale: Locale? = null) = "%.${digits}f".format(locale, this)

val String.isAscii: Boolean get() = toCharArray().none { it < ' ' || it > '~' }

val String.asUUID: UUID
    get() = UUID.fromString(this)


val BigDecimal.sign: String get() = if (signum() < 0) "–" else ""

/**
 * Uses [LazyThreadSafetyMode.NONE] to provide values quicker
 */
fun <T> fastLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

/**
 * Filter this list by searching for elements that contain [substring],
 * or if string is not [String.isValid], the list itself
 * @param substring a string, that must be [String.isValid]
 * @return a resulting list
 */
fun List<String>.filterBySubstring(substring: String?): List<String> = if (substring.isValid) asSequence()
    .filter { it.contains(substring!!, true) }
    .toList()
else this

/**
 * Returns the sign of the number, as a char
 */
val Int.signChar: String
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
val Float.signChar: String
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
val Double.signChar: String
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
val Long.signChar: String
    get() {
        return when {
            this < 0L -> "-"
            this > 0L -> "+"
            else -> ""
        }
    }

fun Int.toStringWithSign() = "$sign$absoluteValue"

val ClosedRange<Int>.size get() = endInclusive - start

val ClosedRange<Double>.size get() = endInclusive - start

val ClosedRange<Float>.size get() = endInclusive - start

val ClosedRange<Long>.size get() = endInclusive - start

val ClosedRange<Short>.size @JvmName("sizeShort") get() = endInclusive - start

val ClosedRange<Byte>.size @JvmName("sizeByte") get() = endInclusive - start

fun Int?.takeIfNotZero() = takeIf { it != 0 }
fun Long?.takeIfNotZero() = takeIf { it != 0L }
fun Double?.takeIfNotZero() = takeIf { it != 0.0 }
fun Float?.takeIfNotZero() = takeIf { it != 0.0f }
fun Short?.takeIfNotZero() = takeIf { it != 0.toShort() }
fun Byte?.takeIfNotZero() = takeIf { it != 0.toByte() }
