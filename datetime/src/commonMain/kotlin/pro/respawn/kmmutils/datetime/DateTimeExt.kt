@file:Suppress("TooManyFunctions", "unused")

package pro.respawn.kmmutils.datetime

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
import kotlin.time.Clock
import kotlin.time.Instant

/**
 *  ISO8601 value of the day of the week, from 1 (Monday) to 7 (Sunday).
 */
public val DayOfWeek.value: Int get() = ordinal + 1

/**
 * Returns a new LocalDateTime that has either the same (if [this] has the same day of week as [dayOfWeek]) or
 * the previous [dayOfWeek].
 */
public fun LocalDateTime.withPreviousOrSameDayOfWeek(
    dayOfWeek: DayOfWeek,
    zone: TimeZone,
): LocalDateTime {
    val daysDiff = dayOfWeek.value - this.dayOfWeek.value
    if (daysDiff == 0) return this

    val daysToAdd = if (daysDiff >= 0) 7 - daysDiff else -daysDiff
    return minusDays(daysToAdd, zone)
}

/**
 * Returns a new LocalDateTime that is set to [DayOfWeek.MONDAY] and [00:00:00.000] of the week of [this].
 */
public fun LocalDateTime.asStartOfWeek(zone: TimeZone): LocalDateTime =
    withPreviousOrSameDayOfWeek(DayOfWeek.MONDAY, zone).asMidnight()

/**
 * Returns a new LocalDateTime that is set to the first day of month and [00:00:00.000] of the month of [this].
 */
public fun LocalDateTime.asStartOfMonth(): LocalDateTime = LocalDateTime(year, month, 1, 0, 0, 0, 0)

/**
 * Returns a new LocalDateTime that is set to the first day of year and [00:00:00.000] of the year of [this].
 */
public fun LocalDateTime.asStartOfYear(): LocalDateTime = LocalDateTime(year, Month.JANUARY, 1, 0, 0, 0, 0)

/**
 * Adds a given value to the [this], converting it to [Instant] for the addition.
 */
public fun LocalDateTime.plus(value: Int, unit: DateTimeUnit, zone: TimeZone): LocalDateTime =
    toInstant(zone).plus(value, unit, zone).toLocalDateTime(zone)

/**
 * Returns a new [LocalDateTime] with the given [day] of month and everything else of [this].
 */
public fun LocalDateTime.withDayOfMonth(day: Int): LocalDateTime =
    LocalDateTime(year, month, day, hour, minute, second, nanosecond)

/**
 * Adds a specified number of [days] to [this].
 */
public fun LocalDateTime.plusDays(days: Int, zone: TimeZone): LocalDateTime = plus(days, DateTimeUnit.DAY, zone)

/**
 * Subtracts a specified number of [days] from [this].
 */
public fun LocalDateTime.minusDays(days: Int, zone: TimeZone): LocalDateTime = plus(-days, DateTimeUnit.DAY, zone)

/**
 * Returns a new [LocalDateTime] with the given [month] and everything else of [this].
 */
public fun LocalDateTime.withMonth(month: Month): LocalDateTime =
    LocalDateTime(year, month, day, hour, minute, second, nanosecond)

/**
 * Adds a specified number of [months] to [this]. Returns a new [LocalDateTime]
 */
public fun LocalDateTime.plusMonths(months: Int, zone: TimeZone): LocalDateTime = plus(months, DateTimeUnit.MONTH, zone)

/**
 * Subtracts a specified number of [months] from [this]. Returns a new [LocalDateTime]
 */
public fun LocalDateTime.minusMonths(months: Int, zone: TimeZone): LocalDateTime =
    plus(-months, DateTimeUnit.MONTH, zone)

/**
 * Computes the length of the current month of [LocalDate] in days
 */
public val LocalDate.lengthOfMonth: Int get() = month.length(year)

/**
 * Computes the length of the current month of [LocalDateTime] in days
 */
public val LocalDateTime.lengthOfMonth: Int get() = month.length(year)

/**
 * Creates a new [Instant] and converts it to [LocalDateTime]
 */
public fun LocalDateTime.Companion.now(zone: TimeZone): LocalDateTime =
    Clock.System.now().toLocalDateTime(zone)

/**
 * Returns tru if [this] is on the same day as [other]. Ignores time, but considers year and month.
 */
public fun LocalDateTime.onSameDay(other: LocalDateTime): Boolean = date == other.date

/**
 * Returns a new [LocalDateTime] that has the date adjusted to the date with the given **next** [dayOfWeek].
 * If the date is already on the given [dayOfWeek], returns a date a week later.
 */
public fun LocalDateTime.withNextDayOfWeek(
    dayOfWeek: DayOfWeek,
    zone: TimeZone,
): LocalDateTime {
    val daysDiff = this.dayOfWeek.value - dayOfWeek.value
    val daysToAdd = if (daysDiff >= 0) 7 - daysDiff else -daysDiff
    return plusDays(daysToAdd, zone)
}

/**
 * Returns a new [LocalDateTime] set to the midnight.
 */
public fun LocalDateTime.asMidnight(): LocalDateTime = LocalDateTime(year, month, day, 0, 0, 0, 0)

/**
 * Returns an Instant of Jan 1st 1970  00:00:00.000 UTC.
 */
public val Instant.Companion.EPOCH: Instant
    get() = fromEpochMilliseconds(0)

/**
 * Adds a specified [time] to [this]. Returns a new [Instant]
 */
public operator fun Instant.plus(time: LocalTime): Instant = this + time.asDuration

/**
 * Finds the length of a given month based on the year, in days.
 */
public fun Month.length(year: Int): Int {
    val start = LocalDate(year, this, 1)
    val end = start.plus(1, DateTimeUnit.MONTH)
    return start.until(end, DateTimeUnit.DAY).toInt()
}

/**
 * Get the current date using provided [zone].
 */
public fun LocalDate.Companion.now(zone: TimeZone): LocalDate = LocalDateTime.now(zone).date

/**
 * Returns a new LocalDate with the day of month set to the specified [day]
 */
public fun LocalDate.withDayOfMonth(day: Int): LocalDate = LocalDate(year, month, day)

/**
 * Returns a new LocalDate with specified [days] added
 */
public fun LocalDate.plusDays(days: Int): LocalDate = plus(days, DateTimeUnit.DAY)
