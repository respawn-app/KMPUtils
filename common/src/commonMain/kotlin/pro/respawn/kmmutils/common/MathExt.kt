package pro.respawn.kmmutils.common

import kotlin.jvm.JvmName
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.log10
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sign

/**
 * @return Arithmetic average of [values]
 * Result is rounded arithmetically
 */
public fun avg(vararg values: Int): Int = values.average().roundToInt()

/**
 * @return Arithmetic average of [values]
 * Result is rounded arithmetically
 */
public fun avg(vararg values: Long): Long = values.average().roundToLong()

/**
 * @return Arithmetic average of [values]
 * Result is rounded arithmetically
 */
public fun avg(vararg values: Float): Double = values.average()

/**
 * @return Arithmetic average of [values]
 * Result is rounded arithmetically
 */
public fun avg(vararg values: Double): Double = values.average()

/**
 * @return Arithmetic average of [values]
 * Result is rounded arithmetically
 */
@JvmName("avgByte")
public fun avg(vararg values: Byte): Int = values.average().roundToInt()

/**
 * uses [sign] and prepends it to the value of [this]
 */
public fun Int.toStringWithSign(): String = "$signChar$absoluteValue"

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
public fun Double?.takeIfFinite(): Double? = this?.takeIf { it.isFinite() }

/**
 * @returns null if [this] is NaN or Infinity
 */
public fun Float?.takeIfFinite(): Float? = this?.takeIf { it.isFinite() }

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
