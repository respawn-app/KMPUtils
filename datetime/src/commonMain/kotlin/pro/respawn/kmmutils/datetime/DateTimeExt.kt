@file:Suppress("TooManyFunctions", "unused")

package pro.respawn.kmmutils.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until

/**
 *  ISO8601 value of the day of the week, from 1 (Monday) to 7 (Sunday).
 */
public val DayOfWeek.value: Int get() = ordinal + 1

public fun LocalDateTime.withPreviousOrSameDayOfWeek(
    dayOfWeek: DayOfWeek,
    zone: TimeZone,
): LocalDateTime {
    val daysDiff = dayOfWeek.value - this.dayOfWeek.value
    if (daysDiff == 0) return this

    val daysToAdd = if (daysDiff >= 0) 7 - daysDiff else -daysDiff
    return minusDays(daysToAdd, zone)
}

public fun LocalDateTime.asStartOfWeek(zone: TimeZone): LocalDateTime =
    withPreviousOrSameDayOfWeek(DayOfWeek.MONDAY, zone).asMidnight()

public fun LocalDateTime.asStartOfMonth(): LocalDateTime = LocalDateTime(year, month, 1, 0, 0, 0, 0)

public fun LocalDateTime.asStartOfYear(): LocalDateTime = LocalDateTime(year, Month.JANUARY, 1, 0, 0, 0, 0)

public fun LocalDateTime.plus(value: Int, unit: DateTimeUnit, zone: TimeZone): LocalDateTime =
    toInstant(zone).plus(value, unit, zone).toLocalDateTime(zone)

public fun LocalDateTime.withDayOfMonth(day: Int): LocalDateTime =
    LocalDateTime(year, month, day, hour, minute, second, nanosecond)

public fun LocalDateTime.plusDays(days: Int, zone: TimeZone): LocalDateTime = plus(days, DateTimeUnit.DAY, zone)

public fun LocalDateTime.minusDays(days: Int, zone: TimeZone): LocalDateTime = plus(-days, DateTimeUnit.DAY, zone)

public fun LocalDateTime.withMonth(month: Month): LocalDateTime =
    LocalDateTime(year, month, dayOfMonth, hour, minute, second, nanosecond)

public fun LocalDateTime.plusMonths(months: Int, zone: TimeZone): LocalDateTime = plus(months, DateTimeUnit.MONTH, zone)

public fun LocalDateTime.minusMonths(months: Int, zone: TimeZone): LocalDateTime =
    plus(-months, DateTimeUnit.MONTH, zone)

public val LocalDate.lengthOfMonth: Int get() = month.length(year)
public val LocalDateTime.lengthOfMonth: Int get() = month.length(year)

public fun LocalDateTime.Companion.now(zone: TimeZone): LocalDateTime =
    Clock.System.now().toLocalDateTime(zone)

public fun LocalDateTime.onSameDay(other: LocalDateTime): Boolean = date == other.date

public fun LocalDateTime.withNextDayOfWeek(
    dayOfWeek: DayOfWeek,
    zone: TimeZone,
): LocalDateTime {
    val daysDiff = this.dayOfWeek.value - dayOfWeek.value
    val daysToAdd = if (daysDiff >= 0) 7 - daysDiff else -daysDiff
    return plusDays(daysToAdd, zone)
}

public fun LocalDateTime.asMidnight(): LocalDateTime = LocalDateTime(year, month, dayOfMonth, 0, 0, 0, 0)

public val Instant.Companion.EPOCH: Instant
    get() = fromEpochMilliseconds(0)

public operator fun Instant.plus(time: Time): Instant = this + time.duration

public fun Month.length(year: Int): Int {
    val start = LocalDate(year, this, 1)
    val end = start.plus(DateTimeUnit.MONTH)
    return start.until(end, DateTimeUnit.DAY)
}
