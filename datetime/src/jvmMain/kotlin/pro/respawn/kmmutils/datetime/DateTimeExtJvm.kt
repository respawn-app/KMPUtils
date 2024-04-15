@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NewApi", "TooManyFunctions")

package pro.respawn.kmmutils.datetime

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalTime
import java.time.Month
import java.time.Year
import java.time.ZoneId
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

/**
 * Returns a new [ZonedDateTime] with [this]'s time set to midnight.
 */
public val ZonedDateTime.midnight: ZonedDateTime get() = truncatedTo(ChronoUnit.DAYS)

/**
 * Whether [this] is on the same day as [other], using [ZoneId] of [this] and [other].
 * It's important that if the [ZonedDateTime.zone]s are different, the values can still be on the same day
 */
public fun ZonedDateTime.onSameLocalDay(other: ZonedDateTime): Boolean =
    truncatedTo(ChronoUnit.DAYS) == other.truncatedTo(ChronoUnit.DAYS)

/**
 * Returns true if [this] is on the same day as [other], not considering time zones
 */
public fun Instant.onSameUTCDay(other: Instant): Boolean =
    truncatedTo(ChronoUnit.DAYS) == other.truncatedTo(ChronoUnit.DAYS)

/**
 * Returns true if [this] is on the same day as [ZonedDateTime.now] using local time zone
 */
public val ZonedDateTime.isToday: Boolean get() = onSameLocalDay(ZonedDateTime.now())

/**
 * Returns a new [ZonedDateTime] with it's day of week set to the first using [this]'s [ZonedDateTime.zone]
 */
public val ZonedDateTime.asStartOfWeek: ZonedDateTime get() = with(ChronoField.DAY_OF_WEEK, 1)

/**
 * returns the [DayOfWeek.getValue] of [this]
 */
public val ZonedDateTime.dayOfWeekValue: Int get() = dayOfWeek.value

/**
 * Returns a new [Instant] with given number of [days] added
 */
public fun Instant.plusDays(days: Long): Instant = plus(days, ChronoUnit.DAYS)

/**
 * Returns a new [Instant] with given number of [days] subtracted
 */
public fun Instant.minusDays(days: Long): Instant = minus(days, ChronoUnit.DAYS)

/**
 * Formats [this] using [formatter]
 */
public fun Instant.asString(formatter: DateTimeFormatter): String = formatter.format(this)

/**
 * Gets the [DayOfWeek.getDisplayName] of [this] using system default locale
 * @return ISO8601 -> Monday = 1
 */

public fun DayOfWeek.asString(): String = getDisplayName(TextStyle.FULL, Locale.getDefault())

/**
 * Returns a string representation of [this] using specified [textStyle] and [locale]
 */
public fun Month.asString(textStyle: TextStyle = TextStyle.FULL, locale: Locale = Locale.getDefault()): String =
    getDisplayName(textStyle, locale)

/**
 * Resets a given [Calendar] to the start of the day. Modifies the original object.
 */
public fun Calendar.setToMidnight() {
    set(HOUR_OF_DAY, 0)
    set(MINUTE, 0)
    set(SECOND, 0)
    set(MILLISECOND, 0)
}

/**
 * Creates a new [Calendar] using [this] as the time in milliseconds.
 */
public fun Long.toCalendar(
    timeZone: TimeZone = TimeZone.getDefault(),
    locale: Locale = Locale.getDefault()
): Calendar = getInstance(timeZone, locale).also { it.timeInMillis = this }

/**
 * Creates a new [Calendar] using [this] as the date.
 */
public fun Date.toCalendar(
    timeZone: TimeZone = TimeZone.getDefault(),
    locale: Locale = Locale.getDefault()
): Calendar = time.toCalendar(timeZone, locale)

/**
 * Returns tru if [this] calendar is set to current month, computed using local date and timezone of
 * the original calendar.
 */
public fun Calendar.isCurrentMonth(): Boolean {
    val now = getInstance(timeZone)
    return now[MONTH] == this[MONTH] && now[YEAR] == this[YEAR]
}

/**
 * Returns tru if [this] calendar is set to current year, computed using local date and timezone
 * of the original calendar
 */
public fun Calendar.isCurrentYear(): Boolean = getInstance(timeZone)[YEAR] == this[YEAR]

/**
 * Resets this calendar to the start of the day. Modifies the original object.
 * @see setToMidnight
 */
public fun Calendar.resetToStartOfDay() {
    setToMinimum(HOUR_OF_DAY)
    setToMinimum(MINUTE)
    setToMinimum(SECOND)
    setToMinimum(MILLISECOND)
}

/**
 * Resets [this] to the last millisecond of the day.
 */
public fun Calendar.resetToEndOfDay() {
    setToMaximum(HOUR_OF_DAY)
    setToMaximum(MINUTE)
    setToMaximum(SECOND)
    setToMaximum(MILLISECOND)
}

/**
 * Resets [this] to the start of the month and midnight.
 * @see resetToStartOfDay
 */
public fun Calendar.resetToStartOfMonth() {
    setToMinimum(DAY_OF_MONTH)
    resetToStartOfDay()
}

/**
 * Resets [this] to the last day of the month and the last millisecond of the day
 * @see resetToEndOfDay
 */
public fun Calendar.resetToEndOfMonth() {
    setToMaximum(DAY_OF_MONTH)
    resetToEndOfDay()
}

/**
 * Sets [field] to its minimum value
 */
public fun Calendar.setToMinimum(field: Int) {
    set(field, getActualMinimum(field))
}

/**
 * Sets [field] to its maximum value
 */
public fun Calendar.setToMaximum(field: Int) {
    set(field, getActualMaximum(field))
}

/**
 * Computes the length of month of this zoned datetime.
 */
public val ZonedDateTime.lengthOfMonth: Int get() = month.length(Year.isLeap(year.toLong()))

/**
 * Sets [this]'s time to [time]. Returns a new [ZonedDateTime]
 */
@Deprecated("Time class is deprecated")
public fun ZonedDateTime.withTime(time: kotlinx.datetime.LocalTime): ZonedDateTime =
    ZonedDateTime.of(year, monthValue, dayOfMonth, time.hour, time.minute, time.second, nano, zone)

/**
 * Sets [this]'s time to [time]. Returns a new [ZonedDateTime]
 */
public fun ZonedDateTime.withTime(time: LocalTime): ZonedDateTime =
    ZonedDateTime.of(year, monthValue, dayOfMonth, time.hour, time.minute, time.second, nano, zone)

/**
 * Sorts [DayOfWeek]s according to the [locale]. I.e. will affect the first value (Monday / Sunday etc.)
 */
public fun Iterable<DayOfWeek>.sortedByLocale(locale: Locale = Locale.getDefault()): List<DayOfWeek> {
    val first = WeekFields.of(locale).firstDayOfWeek
    val all = this.toMutableList()
    if (all.remove(first))
        all.add(0, first)
    return all
}

/**
 * Creates a new [ZonedDateTime] using [this] Instant and a given [zoneId]
 */
public fun Instant.toZDT(
    zoneId: ZoneId,
): ZonedDateTime = ZonedDateTime.ofInstant(this, zoneId)
