@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NewApi", "MagicNumber", "TooManyFunctions")

package pro.respawn.kmmutils.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlin.jvm.JvmName
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

// ---- converters

/**
 * Convert [this] to [Time]
 */
public fun LocalTime.toTime(): Time = Time(hour, minute, second)

/**
 * Make a new [LocalTime] using this Time and a 0 nanosecond value
 */
public fun Time.toLocalTime(): LocalTime = LocalTime(hour, minute, second, 0)

/**
 * Create a [LocalDateTime] with the specified [time]
 */
public fun LocalDate.atTime(time: Time): LocalDateTime = atTime(time.hour, time.minute, time.second)

/**
 * Create a new [LocalDateTime] using given [date]
 */
public fun Time.atDate(date: LocalDate): LocalDateTime = LocalDateTime(date, toLocalTime())

/**
 * Returns an integer that represents this time, like a string, but without the ":"
 * Examples:
 * - 17:12:45 -> 171245
 * - 00:00:00 -> 0
 * - 05:00:00 -> 50000
 */
public fun Time.toInt(): Int = hour * 10000 + minute * 100 + second

// ---- operations

/**
 * Calculate distance to the [other] between [Time.MIN] and [Time.MAX]
 */
public infix fun Time.distanceTo(other: Time): Time = Time.ofSeconds(
    (other.totalSeconds - totalSeconds).absoluteValue
)

/**
 * Create a new [LocalDateTime] with the given [time] and [nanos]
 * Preserves [nanos] of previous time
 */
public fun LocalDateTime.withTime(time: Time, nanos: Int = nanosecond): LocalDateTime =
    LocalDateTime(year, month, dayOfMonth, time.hour, time.minute, time.second, nanos)

/**
 * Returns this object as [kotlin.time.Duration]
 */
public val Time.asDuration: Duration get() = totalSeconds.seconds

/**
 * @return the amount of seconds from this object's value [to]
 * The amount is always positive
 */
public infix fun Time.distanceInSeconds(to: Time): Int =
    abs(to.hour - hour) * 60 * 60 + abs(to.minute - minute) * 60 + abs(to.second - second)

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
 * Represents this numeric value as if it is a number on the clock
 * Examples:
 * - 0 -> "00"
 * - 1 -> "01"
 * - 15 -> "15"
 */
public fun Time.Companion.asTimeNumber(value: Int): String = if (value < 10) "0$value" else value.toString()

// ---- builders

/**
 * Create a new [Time] from [localTime]
 */
public operator fun Time.Companion.invoke(localTime: LocalTime): Time = localTime.toTime()

/**
 * Create a new [Time] from [duration].
 * If the [duration] is larger than 24 hours, it will wrap around
 */
public operator fun Time.Companion.invoke(duration: Duration): Time =
    ofSeconds(duration.inWholeSeconds.toInt())

/**
 * Parse a new time object using the int representation of it.
 * @see Time.toInt
 */
public fun Time.Companion.ofInt(hms: Int): Time = Time(hms / 10000, hms / 100 % 100, hms % 100)

/**
 * Create a time from milliseconds since midnight.
 * **[millis] is NOT a timestamp**
 */
public fun Time.Companion.ofMillis(millis: Long): Time {
    val totalSeconds = millis / 1000
    val totalMinutes = totalSeconds / 60
    val totalHours = totalMinutes / 60
    return Time(totalHours.toInt() % 24, (totalMinutes % 60).toInt(), (totalSeconds % 60).toInt())
}

/**
 * Get current time value using specified seconds **since midnight**
 * [seconds] is NOT a timestamp
 * @see totalSeconds
 */
public fun Time.Companion.ofSeconds(seconds: Int): Time = ofMillis(seconds * 1000L)

/**
 * Create a new time using specified [hours], [minutes], or [seconds]
 * If you specify a value bigger than the initial, the remainder will spill into the next
 * value. If you specify a value bigger than [Time.MAX], it will wrap around.
 * Examples:
 * - Time.with(seconds=70) -> 00:01:10
 * - Time.with(hours=25, minutes=60) -> 02:00:00
 */
public fun Time.Companion.with(
    hours: Int = 0,
    minutes: Int = 0,
    seconds: Int = 0
): Time = MIN.add(hours, minutes, seconds)

/**
 * Creates a new time set to current local time using specified [zone].
 */
public fun Time.Companion.now(zone: TimeZone): Time = LocalTime.now(zone).toTime()

// ---- Parsing

/**
 * Parse [s] as time serialized using [Time.toString] and return null if it fails.
 * @see [Time.Companion.parse]
 */
public fun Time.Companion.parseOrNull(s: String): Time? = s.runCatching { parse(this) }.getOrNull()

/**
 * Returns true if [time] is a valid time string
 */
public fun Time.Companion.isValid(time: String): Boolean = parseOrNull(time) != null

/**
 * Returns true if this is a valid time string
 */
public val String?.isValidTime: Boolean get() = this?.let { Time.isValid(it) } ?: false

/**
 * Try to parse [this] as [Time] or return null if it fails
 */
public fun String?.toTimeOrNull(): Time? = this?.let { Time.parseOrNull(this) }

// ---- collection

/**
 * Compute total asDuration of all items in the collection
 */
public fun Iterable<Time?>.totalDuration(): Time = Time.ofSeconds(sumOf { it?.totalSeconds ?: 0 })

/**
 * Compute total asDuration of all items in the sequence
 */
@JvmName("totalDurationSequence")
public fun Sequence<Time?>.totalDuration(): Time = Time.ofSeconds(sumOf { it?.totalSeconds ?: 0 })

public val Duration.asTime: Time get() = Time.ofSeconds(inWholeSeconds.toInt())
