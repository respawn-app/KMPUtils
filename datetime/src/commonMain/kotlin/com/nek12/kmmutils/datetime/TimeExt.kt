@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NewApi", "MagicNumber")

package com.nek12.kmmutils.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun String.toTimeOrNull() = runCatching { Time.parse(this) }.getOrNull()

val Iterable<Time?>.totalDuration
    get() = Time.fromSecondsSinceMidnight(sumOf { it?.totalSeconds ?: 0 })

@get:JvmName("totalDurationSequence")
val Sequence<Time?>.totalDuration
    get() = Time.fromSecondsSinceMidnight(sumOf { it?.totalSeconds ?: 0 })

infix fun Time.distanceTo(other: Time) = Time.fromSecondsSinceMidnight(
    (other.totalSeconds - totalSeconds).absoluteValue
)

/**
 * Preserves [nanos] of previous time
 */
fun LocalDateTime.withTime(time: Time, nanos: Int = nanosecond) =
    LocalDateTime(year, month, dayOfMonth, time.hour, time.minute, time.second, nanos)

fun LocalTime.toTime(): Time = Time(hour, minute, second)

operator fun Time.Companion.invoke(localTime: LocalTime) = localTime.toTime()

val Time.duration get() = totalSeconds.seconds

/**
 * @return the amount of seconds from this object's value [to]
 * The amount is always positive
 */
infix fun Time.distanceInSeconds(to: Time): Int =
    abs(to.hour - hour) * 60 * 60 + abs(to.minute - minute) * 60 + abs(to.second - second)

/**
 * Returns an integer that represents this time, like a string, but without the ":"
 * Examples:
 * - 17:12:45 -> 171245
 * - 00:00:00 -> 0
 * - 05:00:00 -> 50000
 */
fun Time.toInt(): Int = hour * 10000 + minute * 100 + second

/**
 * Whether the hours cross the PM threshold (hour >= 12)
 */
val Time.isPM get() = hour >= 12

/** * Get the amount of minutes since midnight.
 */
val Time.totalMinutes: Double
    get() = hour * 60 + minute + second.toDouble() / 60

/**
 * Get the amount of seconds since midnight. Convenient for storing [Time] as an [Int] value
 */
val Time.totalSeconds: Int
    get() = hour * 3600 + minute * 60 + second

/**
 * Get hour in am/pm format (just the number)
 */
val Time.hourAs12H: Int
    get() = if (hour % 12 == 0) hour else hour % 12

/**
 * Add specified number of [hours], [minutes], or [seconds] to this time.
 * If the argument is not specified, 0 will be added.
 * **If the resulting value is bigger than 23:59:59,
 * the value will be wrapped (e.g. to 00:00:00)**
 * @return A new resulting Time object.
 */
fun Time.add(hours: Int = 0, minutes: Int = 0, seconds: Int = 0): Time {
    val (normalizedHours, normalizedMinutes, normalizedSeconds) = Time.normalize(hours, minutes, seconds)

    val hDelta =
        (normalizedHours + hour + (minute + normalizedMinutes) / 60 + (second + normalizedSeconds) / 3600) % 24
    val mDelta = (minute + normalizedMinutes + (second + normalizedSeconds) / 60) % 60
    val sDelta = (second + normalizedSeconds) % 60

    val h = (if (hDelta >= 0) hDelta else hDelta + 24) % 24
    val m = if (mDelta >= 0) mDelta else mDelta + 60
    val s = if (sDelta >= 0) sDelta else sDelta + 60
    return Time(h, m, s)
}

/**
 * If the [duration] is larger than 24 hours, it will wrap around
 */
operator fun Time.Companion.invoke(duration: Duration) = fromSecondsSinceMidnight(duration.inWholeSeconds.toInt())
