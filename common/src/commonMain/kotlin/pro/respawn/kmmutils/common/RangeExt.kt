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
