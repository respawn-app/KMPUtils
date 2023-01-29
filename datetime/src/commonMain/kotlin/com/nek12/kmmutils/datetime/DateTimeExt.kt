@file:Suppress("TooManyFunctions", "unused")

package com.nek12.kmmutils.datetime

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

fun LocalDateTime.withPreviousOrSameDayOfWeek(
    dayOfWeek: DayOfWeek,
    zone: TimeZone,
): LocalDateTime {
    val daysDiff = dayOfWeek.value - this.dayOfWeek.value
    if (daysDiff == 0) return this

    val daysToAdd = if (daysDiff >= 0) 7 - daysDiff else -daysDiff
    return minusDays(daysToAdd, zone)
}

fun LocalDateTime.asStartOfWeek(zone: TimeZone) =
    withPreviousOrSameDayOfWeek(DayOfWeek.MONDAY, zone).asMidnight()

fun LocalDateTime.asStartOfMonth() = LocalDateTime(year, month, 1, 0, 0, 0, 0)

fun LocalDateTime.asStartOfYear() = LocalDateTime(year, Month.JANUARY, 1, 0, 0, 0, 0)

fun LocalDateTime.plus(value: Int, unit: DateTimeUnit, zone: TimeZone) =
    toInstant(zone).plus(value, unit, zone).toLocalDateTime(zone)

fun LocalDateTime.withDayOfMonth(day: Int) = LocalDateTime(year, month, day, hour, minute, second, nanosecond)

fun LocalDateTime.plusDays(days: Int, zone: TimeZone) = plus(days, DateTimeUnit.DAY, zone)

fun LocalDateTime.minusDays(days: Int, zone: TimeZone) = plus(-days, DateTimeUnit.DAY, zone)

fun LocalDateTime.withMonth(month: Month) = LocalDateTime(year, month, dayOfMonth, hour, minute, second, nanosecond)

fun LocalDateTime.plusMonths(months: Int, zone: TimeZone) = plus(months, DateTimeUnit.MONTH, zone)

fun LocalDateTime.minusMonths(months: Int, zone: TimeZone) = plus(-months, DateTimeUnit.MONTH, zone)

val LocalDate.lengthOfMonth get() = month.length(year)
val LocalDateTime.lengthOfMonth get() = month.length(year)

fun LocalDateTime.Companion.now(zone: TimeZone) =
    Clock.System.now().toLocalDateTime(zone)

fun LocalDateTime.onSameDay(other: LocalDateTime): Boolean = date == other.date

fun LocalDateTime.withNextDayOfWeek(
    dayOfWeek: DayOfWeek,
    zone: TimeZone,
): LocalDateTime {
    val daysDiff = this.dayOfWeek.value - dayOfWeek.value
    val daysToAdd = if (daysDiff >= 0) 7 - daysDiff else -daysDiff
    return plusDays(daysToAdd, zone)
}

fun LocalDateTime.asMidnight() = LocalDateTime(year, month, dayOfMonth, 0, 0, 0, 0)

val Instant.Companion.EPOCH: Instant
    get() = fromEpochMilliseconds(0)

operator fun Instant.plus(time: Time) = this + time.duration

fun Month.length(year: Int): Int {
    val start = LocalDate(year, this, 1)
    val end = start.plus(DateTimeUnit.MONTH)
    return start.until(end, DateTimeUnit.DAY)
}
