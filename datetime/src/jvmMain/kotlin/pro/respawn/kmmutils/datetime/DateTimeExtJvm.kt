@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NewApi")

package pro.respawn.kmmutils.datetime

import java.time.DayOfWeek
import java.time.Instant
import java.time.Month
import java.time.Year
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.*
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MILLISECOND
import java.util.Calendar.MINUTE
import java.util.Calendar.MONTH
import java.util.Calendar.SECOND
import java.util.Calendar.YEAR
import java.util.Calendar.getInstance

val ZonedDateTime.midnight: ZonedDateTime get() = truncatedTo(ChronoUnit.DAYS)

fun ZonedDateTime.onSameLocalDay(other: ZonedDateTime): Boolean =
    truncatedTo(ChronoUnit.DAYS) == other.truncatedTo(ChronoUnit.DAYS)

fun Instant.onSameUTCDay(other: Instant): Boolean = truncatedTo(ChronoUnit.DAYS) == other.truncatedTo(ChronoUnit.DAYS)

val ZonedDateTime.isToday: Boolean get() = onSameLocalDay(ZonedDateTime.now())

val ZonedDateTime.weekStart: ZonedDateTime get() = with(ChronoField.DAY_OF_WEEK, 1)

val Instant.localMonthDay get() = toZDT().dayOfMonth

val ZonedDateTime.localWeekDay: Int get() = dayOfWeek.value

fun Instant.plusDays(offset: Long): Instant = plus(offset, ChronoUnit.DAYS)

fun Instant.minusDays(offset: Long): Instant = minus(offset, ChronoUnit.DAYS)

fun Instant.asString(formatter: DateTimeFormatter): String = formatter.format(this)

/**
 * @return ISO8601 -> Monday = 1
 */

fun DayOfWeek.asString(): String = getDisplayName(TextStyle.FULL, Locale.getDefault())

fun Month.asString(textStyle: TextStyle = TextStyle.FULL, locale: Locale = Locale.getDefault()): String =
    getDisplayName(textStyle, locale)

fun Calendar.setToMidnight() {
    set(HOUR_OF_DAY, 0)
    set(MINUTE, 0)
    set(SECOND, 0)
    set(MILLISECOND, 0)
}

fun Long.toCalendar(
    timeZone: TimeZone = TimeZone.getDefault(),
    locale: Locale = Locale.getDefault()
): Calendar = getInstance(timeZone, locale).also { it.timeInMillis = this }

fun Date.toCalendar(
    timeZone: TimeZone = TimeZone.getDefault(),
    locale: Locale = Locale.getDefault()
): Calendar = time.toCalendar(timeZone, locale)

fun Calendar.isCurrentMonth(): Boolean {
    val now = getInstance(timeZone)
    return now[MONTH] == this[MONTH] && now[YEAR] == this[YEAR]
}

fun Calendar.isCurrentYear(): Boolean = getInstance(timeZone)[YEAR] == this[YEAR]

fun Calendar.resetToStartOfDay() {
    setToMinimum(HOUR_OF_DAY)
    setToMinimum(MINUTE)
    setToMinimum(SECOND)
    setToMinimum(MILLISECOND)
}

fun Calendar.resetToEndOfDay() {
    setToMaximum(HOUR_OF_DAY)
    setToMaximum(MINUTE)
    setToMaximum(SECOND)
    setToMaximum(MILLISECOND)
}

fun Calendar.resetToStartOfMonth() {
    setToMinimum(DAY_OF_MONTH)
    resetToStartOfDay()
}

fun Calendar.resetToEndOfMonth() {
    setToMaximum(DAY_OF_MONTH)
    resetToEndOfDay()
}

fun Calendar.setToMinimum(field: Int) {
    set(field, getActualMinimum(field))
}

fun Calendar.setToMaximum(field: Int) {
    set(field, getActualMaximum(field))
}

val ZonedDateTime.lengthOfMonth get() = month.length(Year.isLeap(year.toLong()))

fun ZonedDateTime.withTime(time: Time): ZonedDateTime =
    ZonedDateTime.of(year, monthValue, dayOfMonth, time.hour, time.minute, time.second, nano, zone)

fun List<DayOfWeek>.sortedByLocale(locale: Locale = Locale.getDefault()): List<DayOfWeek> {
    val first = WeekFields.of(locale).firstDayOfWeek
    val all = this.toMutableList()
    if (all.remove(first))
        all.add(0, first)
    return all
}
