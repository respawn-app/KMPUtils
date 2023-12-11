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
import kotlin.time.Duration.Companion.nanoseconds

// ---- converters

/**
 * Create a [LocalDateTime] with the specified [time]
 */
public fun LocalDate.atTime(time: LocalTime): LocalDateTime = atTime(time.hour, time.minute, time.second)

/**
 * Create a new [LocalDateTime] using given [date]
 */
public fun LocalTime.atDate(date: LocalDate): LocalDateTime = LocalDateTime(date, this)

/**
 * Returns an integer that represents this time, like a string, but without the ":"
 *
 * Truncates nanoseconds.
 *
 * Examples:
 * - 17:12:45 -> 171245
 * - 00:00:00 -> 0
 * - 05:00:00 -> 50000
 */
public fun LocalTime.toInt(): Int = hour * 10000 + minute * 100 + second

// ---- operations

/**
 * Calculate distance to the [other] between [LocalTime.MIN] and [LocalTime.MAX]
 */
public infix fun LocalTime.distanceTo(other: LocalTime): LocalTime = LocalTime.fromSecondOfDay(
    (other.toSecondOfDay() - toSecondOfDay()).absoluteValue
)

/**
 * Create a new [LocalDateTime] with the given [time] and [nanos]
 * Preserves [nanos] of previous time
 */
public fun LocalDateTime.withTime(time: LocalTime, nanos: Int = nanosecond): LocalDateTime =
    LocalDateTime(year, month, dayOfMonth, time.hour, time.minute, time.second, nanos)

/**
 * Returns this object as [kotlin.time.Duration]
 */
public val LocalTime.asDuration: Duration get() = toNanosecondOfDay().nanoseconds

/**
 * @return the amount of seconds from this object's value [to]
 * The amount is always positive
 */
public infix fun LocalTime.distanceInSeconds(to: LocalTime): Int =
    abs(to.hour - hour) * 60 * 60 + abs(to.minute - minute) * 60 + abs(to.second - second)

/**
 * Whether the hours cross the PM threshold (hour >= 12)
 */
public val LocalTime.isPM: Boolean get() = hour >= 12

/** * Get the amount of minutes since midnight.
 */
public val LocalTime.totalMinutes: Double
    get() = hour * 60 + minute + second.toDouble() / 60

/**
 * Get the amount of seconds since midnight. Convenient for storing [LocalTime] as an [Int] value
 */
public val LocalTime.totalSeconds: Int
    get() = hour * 3600 + minute * 60 + second

/**
 * Get hour in am/pm format (just the number)
 */
public val LocalTime.hourAs12H: Int
    get() = if (hour % 12 == 0) hour else hour % 12

/**
 * Add specified number of [hours], [minutes], or [seconds] to this time.
 * If the argument is not specified, 0 will be added.
 * **If the resulting value is bigger than 23:59:59,
 * the value will be wrapped (e.g. to 00:00:00)**
 * @return A new resulting [LocalTime] object.
 */
public fun LocalTime.add(hours: Int = 0, minutes: Int = 0, seconds: Int = 0): LocalTime {
    val (normalizedHours, normalizedMinutes, normalizedSeconds) = LocalTime.normalize(hours, minutes, seconds)

    val hDelta =
        (normalizedHours + hour + (minute + normalizedMinutes) / 60 + (second + normalizedSeconds) / 3600) % 24
    val mDelta = (minute + normalizedMinutes + (second + normalizedSeconds) / 60) % 60
    val sDelta = (second + normalizedSeconds) % 60

    val h = (if (hDelta >= 0) hDelta else hDelta + 24) % 24
    val m = if (mDelta >= 0) mDelta else mDelta + 60
    val s = if (sDelta >= 0) sDelta else sDelta + 60
    return LocalTime(h, m, s, nanosecond)
}

/**
 * Represents this numeric value as if it is a number on the clock
 * Examples:
 * - 0 -> "00"
 * - 1 -> "01"
 * - 15 -> "15"
 */
public fun LocalTime.Companion.asTimeNumber(value: Int): String = if (value < 10) "0$value" else value.toString()

// ---- builders

/**
 * Create a new [LocalTime] from [duration].
 * If the [duration] is larger than 24 hours, it will wrap around
 */
public operator fun LocalTime.Companion.invoke(duration: Duration): LocalTime =
    fromNanosecondOfDay(duration.inWholeNanoseconds)

/**
 * Parse a new time object using the int representation of it.
 * @see LocalTime.toInt
 */
public fun LocalTime.Companion.ofInt(hms: Int): LocalTime = LocalTime(hms / 10000, hms / 100 % 100, hms % 100)

/**
 * Create a time from milliseconds since midnight.
 * **[millis] is NOT a timestamp**
 */
public fun LocalTime.Companion.ofMillis(millis: Int): LocalTime = fromMillisecondOfDay(millis)

/**
 * Get current time value using specified seconds **since midnight**
 * [seconds] is NOT a timestamp
 * @see totalSeconds
 */
public fun LocalTime.Companion.ofSeconds(seconds: Int): LocalTime = fromSecondOfDay(seconds)

/**
 * Create a new time using specified [hours], [minutes], or [seconds]
 * If you specify a value bigger than the initial, the remainder will spill into the next
 * value. If you specify a value bigger than [LocalTime.MAX], it will wrap around.
 * Examples:
 * - `LocalTime.with(seconds=70)` -> 00:01:10
 * - `LocalTime.with(hours=25, minutes=60)` -> 02:00:00
 */
public fun LocalTime.Companion.with(
    hours: Int = 0,
    minutes: Int = 0,
    seconds: Int = 0
): LocalTime = MIN.add(hours, minutes, seconds)

// ---- Parsing

/**
 * Parse [s] as time serialized using [LocalTime.toString] and return null if it fails.
 * @see [LocalTime.Companion.parse]
 */
public fun LocalTime.Companion.parseOrNull(s: String): LocalTime? = s.runCatching { parse(this) }.getOrNull()

/**
 * Returns true if [time] is a valid time string
 */
public fun LocalTime.Companion.isValid(time: String): Boolean = parseOrNull(time) != null

/**
 * Returns true if this is a valid time string
 */
public val String?.isValidLocalTime: Boolean get() = this?.let { LocalTime.isValid(it) } ?: false

/**
 * Try to parse [this] as [LocalTime] or return null if it fails
 */
public fun String?.toLocalTimeOrNull(): LocalTime? = this?.let { LocalTime.parseOrNull(this) }

// ---- collection

/**
 * Compute total asDuration of all items in the collection
 */
public fun Iterable<LocalTime?>.totalDuration(): Duration = sumOf { it?.toNanosecondOfDay() ?: 0 }.nanoseconds

/**
 * Compute total asDuration of all items in the sequence
 */
@JvmName("totalDurationSequence")
public fun Sequence<LocalTime?>.totalDuration(): LocalTime = LocalTime.ofSeconds(sumOf { it?.totalSeconds ?: 0 })

/**
 * Convert this duration to [LocalTime]. If this duration is larger than 24 hours, clock will wrap around.
 */
public val Duration.asLocalTime: LocalTime get() = LocalTime.fromNanosecondOfDay(inWholeNanoseconds)

/**
 * If [this] is [LocalTime.MIN] return null, [this] otherwise
 */
public fun LocalTime?.takeIfNotZero(): LocalTime? = takeIf { it != LocalTime.MIN }

/** Same as toString but gives you a choice on whether to use 12H scheme.
 * [toString] uses asString(false).
 *
 * @param addSecondsIfZero If the seconds value is equal to 0, should include them in the representation? e.g.:
 * @param use12h whether to represent time in 12-hour format (AM/PM letters not added). Mind that deserializing
 * the resulting string back to [LocalTime] won't result in a valid value, if you have this parameter true
 * true => "17:00:00", false => "17:00".
 *
 * @return a string representation of this time
 * **/
public fun LocalTime.asString(use12h: Boolean = false, addSecondsIfZero: Boolean = false): String = buildString {
    append("${LocalTime.asTimeNumber(if (use12h) hourAs12H else hour)}:${LocalTime.asTimeNumber(minute)}")
    if (addSecondsIfZero || second != 0) append(":${LocalTime.asTimeNumber(second)}")
    if (use12h) append(" ${if (isPM) "PM" else "AM"}")
}

/**
 * Create a new time with the result of adding [LocalTime] to [other]
 */
public operator fun LocalTime.plus(other: LocalTime): LocalTime = add(other.hour, other.minute, other.second)

/**
 * Subtract [other] from [LocalTime]
 */
public operator fun LocalTime.minus(other: LocalTime): LocalTime = add(-other.hour, -other.minute, -other.second)

/**
 * Get either the hour or minute or second value of this time.
 * Values more than 2 will throw an [IndexOutOfBoundsException]
 */
public operator fun LocalTime.get(index: Int): Int = when (index) {
    0 -> hour
    1 -> minute
    2 -> second
    else -> throw IndexOutOfBoundsException("Only 0, 1 and 2 are valid values")
}

/**
 * 23:59:59
 */
public val LocalTime.Companion.MAX: LocalTime get() = LocalTime(23, 59, 59, 999_999_999)

/** example: 12:45:00, 4:30, 7:00 AM, 24 or 12h format, word separator is " ".
 * On a value that is not a valid time, will throw.
 * **/
@Throws(IllegalArgumentException::class)
public fun LocalTime.Companion.parseAs12H(s: String): LocalTime {
    try {
        require(s.isNotBlank()) { "Empty time string" }

        val words = s.split(" ")
        require(words.size in 1..2) { "Not a time" }

        val parts = words.first().split(':', '.', '-', ' ', ',', '_', ignoreCase = true)
        require(parts.size in 2..3) { "Invalid delimiter count" }

        val hours = parts[0].toInt() + if (words.size == 2 && words[1] == "PM") 12 else 0

        val minutes = parts[1].toInt()

        val seconds = if (parts.size == 3) parts[2].toInt() else 0

        return LocalTime(hours, minutes, seconds)
    } catch (expected: Exception) {
        throw IllegalArgumentException("Couldn't parse time", expected)
    }
}

/**
 * 00:00:00
 */
public val LocalTime.Companion.MIN: LocalTime get() = LocalTime(0, 0)

/** Normalize a set of values for hours, minutes, or seconds, to a valid time value.
 *
 * @returns values of hours, minutes and seconds adjusted if necessary to fit into their respective
 * ranges. Any excess is added to the value of the next order. The values can also wrap
 * around if the resulting time is bigger than [LocalTime.MAX]
 * Example: normalize(25,70,100) -> (2,11,40)
 */
internal fun LocalTime.Companion.normalize(hours: Int = 0, minutes: Int = 0, seconds: Int = 0): Triple<Int, Int, Int> {
    val normalizedHours = hours + (minutes + seconds / 60) / 60
    val normalizedMinutes = (minutes + seconds / 60) % 60
    val normalizedSeconds = seconds % 60
    return Triple(normalizedHours % 24, normalizedMinutes, normalizedSeconds)
}

/**
 * Get the current time using provided [zone].
 */
public fun LocalTime.Companion.now(zone: TimeZone): LocalTime = LocalDateTime.now(zone).time

/**
 * Returns the same time with [LocalTime.nanosecond] set to 0.
 */
public fun LocalTime.truncateNanos(): LocalTime = LocalTime(hour, minute, second, 0)

/**
 * Returns the same time with both nanos and seconds set to 0.
 */
public fun LocalTime.truncateSeconds(): LocalTime = LocalTime(hour, minute, 0, 0)

/**
 * Returns the same time with all of nanos, seconds and minutes set to 0
 */
public fun LocalTime.truncateMinutes(): LocalTime = LocalTime(hour, 0, 0, 0)
