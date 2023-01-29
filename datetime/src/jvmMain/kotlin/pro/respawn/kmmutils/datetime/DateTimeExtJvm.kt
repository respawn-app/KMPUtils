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

public val ZonedDateTime.midnight: ZonedDateTime get() = truncatedTo(ChronoUnit.DAYS)

public fun ZonedDateTime.onSameLocalDay(other: ZonedDateTime): Boolean =
    truncatedTo(ChronoUnit.DAYS) == other.truncatedTo(ChronoUnit.DAYS)

public fun Instant.onSameUTCDay(other: Instant): Boolean = truncatedTo(ChronoUnit.DAYS) == other.truncatedTo(ChronoUnit.DAYS)

public val ZonedDateTime.isToday: Boolean get() = onSameLocalDay(ZonedDateTime.now())

public val ZonedDateTime.weekStart: ZonedDateTime get() = with(ChronoField.DAY_OF_WEEK, 1)

public val Instant.localMonthDay: Int get() = toZDT().dayOfMonth

public val ZonedDateTime.localWeekDay: Int get() = dayOfWeek.value

public fun Instant.plusDays(offset: Long): Instant = plus(offset, ChronoUnit.DAYS)

public fun Instant.minusDays(offset: Long): Instant = minus(offset, ChronoUnit.DAYS)

public fun Instant.asString(formatter: DateTimeFormatter): String = formatter.format(this)

/**
 * @return ISO8601 -> Monday = 1
 */

public fun DayOfWeek.asString(): String = getDisplayName(TextStyle.FULL, Locale.getDefault())

public fun Month.asString(textStyle: TextStyle = TextStyle.FULL, locale: Locale = Locale.getDefault()): String =
    getDisplayName(textStyle, locale)

public fun Calendar.setToMidnight() {
    set(HOUR_OF_DAY, 0)
    set(MINUTE, 0)
    set(SECOND, 0)
    set(MILLISECOND, 0)
}

public fun Long.toCalendar(
    timeZone: TimeZone = TimeZone.getDefault(),
    locale: Locale = Locale.getDefault()
): Calendar = getInstance(timeZone, locale).also { it.timeInMillis = this }

public fun Date.toCalendar(
    timeZone: TimeZone = TimeZone.getDefault(),
    locale: Locale = Locale.getDefault()
): Calendar = time.toCalendar(timeZone, locale)

public fun Calendar.isCurrentMonth(): Boolean {
    val now = getInstance(timeZone)
    return now[MONTH] == this[MONTH] && now[YEAR] == this[YEAR]
}

public fun Calendar.isCurrentYear(): Boolean = getInstance(timeZone)[YEAR] == this[YEAR]

public fun Calendar.resetToStartOfDay() {
    setToMinimum(HOUR_OF_DAY)
    setToMinimum(MINUTE)
    setToMinimum(SECOND)
    setToMinimum(MILLISECOND)
}

public fun Calendar.resetToEndOfDay() {
    setToMaximum(HOUR_OF_DAY)
    setToMaximum(MINUTE)
    setToMaximum(SECOND)
    setToMaximum(MILLISECOND)
}

public fun Calendar.resetToStartOfMonth() {
    setToMinimum(DAY_OF_MONTH)
    resetToStartOfDay()
}

public fun Calendar.resetToEndOfMonth() {
    setToMaximum(DAY_OF_MONTH)
    resetToEndOfDay()
}

public fun Calendar.setToMinimum(field: Int) {
    set(field, getActualMinimum(field))
}

public fun Calendar.setToMaximum(field: Int) {
    set(field, getActualMaximum(field))
}

public val ZonedDateTime.lengthOfMonth: Int get() = month.length(Year.isLeap(year.toLong()))

public fun ZonedDateTime.withTime(time: Time): ZonedDateTime =
    ZonedDateTime.of(year, monthValue, dayOfMonth, time.hour, time.minute, time.second, nano, zone)

public fun List<DayOfWeek>.sortedByLocale(locale: Locale = Locale.getDefault()): List<DayOfWeek> {
    val first = WeekFields.of(locale).firstDayOfWeek
    val all = this.toMutableList()
    if (all.remove(first))
        all.add(0, first)
    return all
}
