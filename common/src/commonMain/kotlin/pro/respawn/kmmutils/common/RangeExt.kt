@file:Suppress("TooManyFunctions")

package pro.respawn.kmmutils.common

import kotlin.jvm.JvmName

/**
 * Returns the size of this range, from end inclusive to start
 */
public val ClosedRange<Int>.size: Int get() = endInclusive - start

/**
 * Returns the size of this range, from end inclusive to start
 */
public val ClosedRange<Double>.size: Double get() = endInclusive - start

/**
 * Returns the size of this range, from end inclusive to start
 */
public val ClosedRange<Float>.size: Float get() = endInclusive - start

/**
 * Returns the size of this range, from end inclusive to start
 */
public val ClosedRange<Long>.size: Long get() = endInclusive - start

/**
 * Returns the size of this range, from end inclusive to start
 */
@get:JvmName("sizeShort")
public val ClosedRange<Short>.size: Int get() = endInclusive - start

/**
 * Returns the size of this range, from end inclusive to start
 */
@get:JvmName("sizeByte")
public val ClosedRange<Byte>.size: Int get() = endInclusive - start

// midpoint

/**
 * A middle point of the range. The value is rounded down.
 */
public val ClosedRange<Long>.midpoint: Long get() = start / 2 + endInclusive / 2

/**
 * A middle point of the range. The value is rounded down.
 */
public val ClosedRange<Int>.midpoint: Int get() = start / 2 + endInclusive / 2

/**
 * A middle point of the range. The value is rounded down.
 */
public val ClosedRange<Float>.midpoint: Float get() = start / 2 + endInclusive / 2

/**
 * A middle point of the range. The value is rounded down.
 */
public val ClosedRange<Double>.midpoint: Double get() = start / 2 + endInclusive / 2

/**
 *
 * A middle point of the range. The value is rounded down.
 */
@get:JvmName("midpointShort")
public val ClosedRange<Short>.midpoint: Int get() = start / 2 + endInclusive / 2

/**
 * A middle point of the range. The value is rounded down.
 */
@get:JvmName("midpointByte")
public val ClosedRange<Byte>.midpoint: Int get() = start / 2 + endInclusive / 2

// coercion
/**
 * Produces a range where min value is the minimum between [other.start] and [ClosedRange.start]
 * and max value is maximum between [other.endInclusive] and [ClosedRange.endInclusive]
 */
public fun <T : Comparable<T>> ClosedRange<T>.expand(other: ClosedRange<T>): ClosedRange<T> =
    minOf(other.start, start)..maxOf(other.endInclusive, endInclusive)

/**
 * Produces a range that is fully contained within [other] range.
 */
public infix fun <T : Comparable<T>> ClosedRange<T>.coerceIn(other: ClosedRange<T>): ClosedRange<T> =
    maxOf(start, other.start)..minOf(endInclusive, other.endInclusive)

/**
 * Expand the range left and right by [delta] and return the resulting range
 */
public fun ClosedFloatingPointRange<Double>.expandBy(delta: Double): ClosedFloatingPointRange<Double> =
    start - delta..endInclusive + delta

/**
 * Shrink the range left and right by [delta] and return the resulting range
 */
public fun ClosedFloatingPointRange<Double>.shrinkBy(delta: Double): ClosedFloatingPointRange<Double> = expandBy(-delta)

/**
 * Expand the range left and right by [delta] and return the resulting range
 */
public fun ClosedFloatingPointRange<Int>.expandBy(delta: Int): IntRange =
    start - delta..endInclusive + delta

/**
 * Shrink the range left and right by [delta] and return the resulting range
 */
public fun ClosedFloatingPointRange<Int>.shrinkBy(delta: Int): IntRange = expandBy(-delta)

/**
 * Expand the range left and right by [delta] and return the resulting range
 */
public fun ClosedFloatingPointRange<Long>.expandBy(delta: Long): LongRange =
    start - delta..endInclusive + delta

/**
 * Shrink the range left and right by [delta] and return the resulting range
 */
public fun ClosedFloatingPointRange<Long>.shrinkBy(delta: Long): LongRange = expandBy(-delta)

/**
 * Expand the range left and right by [delta] and return the resulting range
 */
public fun ClosedFloatingPointRange<Float>.expandBy(delta: Float): ClosedFloatingPointRange<Float> =
    start - delta..endInclusive + delta

/**
 * Shrink the range left and right by [delta] and return the resulting range
 */
public fun ClosedFloatingPointRange<Float>.shrinkBy(delta: Float): ClosedFloatingPointRange<Float> = expandBy(-delta)

// conversion

/**
 * Convert this range to a [ClosedFloatingPointRange] of [Float]s
 */
public fun <T> ClosedRange<T>.toFloat(): ClosedFloatingPointRange<Float> where T : Number, T : Comparable<T> =
    start.toFloat()..endInclusive.toFloat()

/**
 * Convert this range to a [LongRange]
 */
public fun <T> ClosedRange<T>.toLong(): LongRange where T : Number, T : Comparable<T> =
    start.toLong()..endInclusive.toLong()

/**
 * Convert this range to an [IntRange]
 */
public fun <T> ClosedRange<T>.toInt(): IntRange where T : Number, T : Comparable<T> =
    start.toInt()..endInclusive.toInt()

/**
 * Convert this range to a [ClosedFloatingPointRange] of [Double]s
 */
public fun <T> ClosedRange<T>.toDouble(): ClosedFloatingPointRange<Double> where T : Number, T : Comparable<T> =
    start.toDouble()..endInclusive.toDouble()

// operators

/**
 * Get [ClosedRange.start]
 */
public operator fun <T : Comparable<T>> ClosedRange<T>.component1(): T = start

/**
 * Get [ClosedRange.endInclusive]
 */
public operator fun <T : Comparable<T>> ClosedRange<T>.component2(): T = endInclusive

/**
 * Returns `true` if [this] range is **fully contained** within the [other] range
 */
public infix operator fun <T : Comparable<T>> ClosedRange<T>.contains(other: ClosedRange<T>): Boolean =
    start <= other.start && endInclusive >= other.endInclusive

/**
 * Add [value] to **both start and end** of the range
 */
public operator fun ClosedRange<Float>.plus(value: Float): ClosedFloatingPointRange<Float> =
    start + value..endInclusive + value

/**
 * Add [value] to **both start and end** of the range
 */
public operator fun ClosedRange<Long>.plus(value: Long): LongRange = start + value..endInclusive + value

/**
 * Add [value] to **both start and end** of the range
 */
public operator fun ClosedRange<Int>.plus(value: Int): IntRange = start + value..endInclusive + value

/**
 * Add [value] to **both start and end** of the range
 */
public operator fun ClosedRange<Double>.plus(value: Double): ClosedFloatingPointRange<Double> =
    start + value..endInclusive + value

/**
 * Subtract [value] from **both start and end** of the range
 */
public operator fun ClosedRange<Float>.minus(value: Float): ClosedFloatingPointRange<Float> = plus(-value)

/**
 * Subtract [value] from **both start and end** of the range
 */
public operator fun ClosedRange<Long>.minus(value: Long): LongRange = plus(-value)

/**
 * Subtract [value] from **both start and end** of the range
 */
public operator fun ClosedRange<Int>.minus(value: Int): IntRange = plus(-value)

/**
 * Subtract [value] from **both start and end** of the range
 */
public operator fun ClosedRange<Double>.minus(value: Double): ClosedFloatingPointRange<Double> = plus(-value)
