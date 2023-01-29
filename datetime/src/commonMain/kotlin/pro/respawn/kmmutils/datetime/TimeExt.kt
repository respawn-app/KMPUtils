@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NewApi", "MagicNumber")

package pro.respawn.kmmutils.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.jvm.JvmName
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public fun String?.toTimeOrNull(): Time? = this?.runCatching { Time.parse(this) }?.getOrNull()

public val Iterable<Time?>.totalDuration: Time
    get() = Time.fromSecondsSinceMidnight(sumOf { it?.totalSeconds ?: 0 })

@get:JvmName("totalDurationSequence")
public val Sequence<Time?>.totalDuration: Time
    get() = Time.fromSecondsSinceMidnight(sumOf { it?.totalSeconds ?: 0 })

public infix fun Time.distanceTo(other: Time): Time = Time.fromSecondsSinceMidnight(
    (other.totalSeconds - totalSeconds).absoluteValue
)

/**
 * Preserves [nanos] of previous time
 */
public fun LocalDateTime.withTime(time: Time, nanos: Int = nanosecond): LocalDateTime =
    LocalDateTime(year, month, dayOfMonth, time.hour, time.minute, time.second, nanos)

public fun LocalTime.toTime(): Time = Time(hour, minute, second)

public operator fun Time.Companion.invoke(localTime: LocalTime): Time = localTime.toTime()

/**
 * Returns this object as [kotlin.time.Duration]
 */
public val Time.duration: Duration get() = totalSeconds.seconds

/**
 * @return the amount of seconds from this object's value [to]
 * The amount is always positive
 */
public infix fun Time.distanceInSeconds(to: Time): Int =
    abs(to.hour - hour) * 60 * 60 + abs(to.minute - minute) * 60 + abs(to.second - second)

/**
 * Returns an integer that represents this time, like a string, but without the ":"
 * Examples:
 * - 17:12:45 -> 171245
 * - 00:00:00 -> 0
 * - 05:00:00 -> 50000
 */
public fun Time.toInt(): Int = hour * 10000 + minute * 100 + second

/**
 * Whether the hours cross the PM threshold (hour >= 12)
 */
public val Time.isPM: Boolean get() = hour >= 12

/** * Get the amount of minutes since midnight.
 */
public val Time.totalMinutes: Double
    get() = hour * 60 + minute + second.toDouble() / 60

/**
 * Get the amount of seconds since midnight. Convenient for storing [Time] as an [Int] value
 */
public val Time.totalSeconds: Int
    get() = hour * 3600 + minute * 60 + second

/**
 * Get hour in am/pm format (just the number)
 */
public val Time.hourAs12H: Int
    get() = if (hour % 12 == 0) hour else hour % 12

/**
 * Add specified number of [hours], [minutes], or [seconds] to this time.
 * If the argument is not specified, 0 will be added.
 * **If the resulting value is bigger than 23:59:59,
 * the value will be wrapped (e.g. to 00:00:00)**
 * @return A new resulting Time object.
 */
public fun Time.add(hours: Int = 0, minutes: Int = 0, seconds: Int = 0): Time {
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
public operator fun Time.Companion.invoke(duration: Duration): Time =
    fromSecondsSinceMidnight(duration.inWholeSeconds.toInt())
