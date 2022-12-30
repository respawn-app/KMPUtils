@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NewApi", "MagicNumber")

package com.nek12.kmmutils.datetime

import kotlin.math.abs

/**
 * A class that represents time, of day as well as a duration.
 * [hour] hour in 24h format
 * [minute] minute
 * [second] second
 * [Time.toString] method returns a string representation for 24-hour format. If you want 12-hour
 * time, use [asString].
 * @throws IllegalArgumentException if the specified values are invalid. Validation happens at
 * object creation time
 */
@Suppress("TooManyFunctions")
open class Time(
    val hour: Int,
    val minute: Int,
    val second: Int = 0,
) : Cloneable, Comparable<Time> {

    init {
        require(hour in 0 until 24 || minute in 0 until 60 || second in 0 until 60) {
            "Invalid time value: $hour:$minute:$second"
        }
    }

    /**
     * Get hour in am/pm format (just the number)
     */
    val hourAs12H: Int
        get() = if (hour % 12 == 0) hour else hour % 12

    /**
     * Get the amount of seconds since midnight. Convenient for storing [Time] as an [Int] value
     */
    val totalSeconds: Int
        get() = hour * 3600 + minute * 60 + second

    /** * Get the amount of minutes since midnight.
     */
    val totalMinutes: Double
        get() = hour * 60 + minute + second.toDouble() / 60

    override fun toString(): String = asString()

    val clock: Clock get() = if (hour >= 12) Clock.PM else Clock.AM

    /**
     * Returns an integer that represents this time, like a string, but without the ":"
     * Examples:
     * - 17:12:45 -> 171245
     * - 00:00:00 -> 0
     * - 05:00:00 -> 50000
     */
    fun toInt(): Int = hour * 10000 + minute * 100 + second

    /**
     * @return the amount of seconds from this object's value [to]
     * The amount is always positive
     */
    fun distanceInSeconds(to: Time): Int =
        abs(to.hour - hour) * 60 * 60 + abs(to.minute - minute) * 60 + abs(to.second - second)

    /** Same as toString but gives you a choice on whether to use 12H scheme.
     * [toString] uses asString(false)
     * @param addSecondsIfZero If the seconds value is equal to 0, should include them in the representation? e.g.:
     * @param use12h whether to represent time in 12-hour format (AM/PM letters not added). Mind that deserializing
     * the resulting string back to Time won't result in a valid value, if you have this parameter true
     * true => "17:00:00", false => "17:00"
     * **/
    fun asString(use12h: Boolean = false, addSecondsIfZero: Boolean = false): String = buildString {
        append("${asString(if (use12h) hourAs12H else hour)}:${asString(minute)}")
        if (addSecondsIfZero || second != 0) append(":${asString(second)}")
        if (use12h) append(" ${clock.value}")
    }

    operator fun plus(other: Time): Time = add(other.hour, other.minute, other.second)

    operator fun minus(other: Time): Time = add(-other.hour, -other.minute, -other.second)

    override operator fun compareTo(other: Time): Int = totalSeconds.compareTo(other.totalSeconds)

    /**
     * Add specified number of [hours], [minutes], or [seconds] to this time.
     * If the argument is not specified, 0 will be added.
     * **If the resulting value is bigger than 23:59:59,
     * the value will be wrapped (e.g. to 00:00:00)**
     * @return A new resulting Time object.
     */
    fun add(hours: Int = 0, minutes: Int = 0, seconds: Int = 0): Time {
        val (normalizedHours, normalizedMinutes, normalizedSeconds) = normalize(hours, minutes, seconds)

        val hDelta =
            (normalizedHours + hour + (minute + normalizedMinutes) / 60 + (second + normalizedSeconds) / 3600) % 24
        val mDelta = (minute + normalizedMinutes + (second + normalizedSeconds) / 60) % 60
        val sDelta = (second + normalizedSeconds) % 60

        val h = (if (hDelta >= 0) hDelta else hDelta + 24) % 24
        val m = if (mDelta >= 0) mDelta else mDelta + 60
        val s = if (sDelta >= 0) sDelta else sDelta + 60
        return Time(h, m, s)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Time

        if (hour != other.hour) return false
        if (minute != other.minute) return false
        if (second != other.second) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hour
        result = 31 * result + minute
        result = 31 * result + second
        return result
    }

    operator fun component1() = hour
    operator fun component2() = minute
    operator fun component3() = second

    operator fun get(index: Int): Int = when (index) {
        0 -> hour
        1 -> minute
        2 -> second
        else -> throw IndexOutOfBoundsException("Only 0, 1 and 2 are valid values")
    }

    fun copy(hour: Int = this.hour, minute: Int = this.minute, second: Int = this.second) = Time(hour, minute, second)

    companion object {

        /**
         * Represents this numeric value as if it is a number on the clock
         * Examples:
         * - 0 -> "00"
         * - 1 -> "01"
         * - 15 -> "15"
         */
        fun asString(value: Int): String = if (value < 10) "0$value" else value.toString()

        /**
         * Parse a new time object using the int representation of it.
         * @see Time.toInt
         */
        fun fromInt(hms: Int): Time = Time(hms / 10000, hms / 100 % 100, hms % 100)

        /**
         * Create a time from milliseconds since midnight.
         * **[millis] is NOT a timestamp**
         */
        fun fromMillisSinceMidnight(millis: Long): Time {
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
        fun fromSecondsSinceMidnight(seconds: Long): Time = fromMillisSinceMidnight(seconds * 1000)

        /**
         * Create a new time using specified [hours], [minutes], or [seconds]
         * If you specify a value bigger than the initial, the remainder will spill into the next
         * value. If you specify a value bigger than [Time.MAX], it will wrap around.
         * Examples:
         * - Time.with(seconds=70) -> 00:01:10
         * - Time.with(hours=25, minutes=60) -> 02:00:00
         */
        fun with(hours: Int = 0, minutes: Int = 0, seconds: Int = 0) = MIN.add(hours, minutes, seconds)

        /** example: 12:45:00, 4:30, 7:00 AM, 24 or 12h format, word separator is " ".
         * On a value that is not a valid time, will throw.
         * **/
        fun of(s: String): Time {
            try {
                val words = s.split(" ")
                require(words.size in 1..2) { "Not a time" }

                val parts = words.first().split(':', '.', '-', ' ', ',', '_', ignoreCase = true)
                require(parts.size in 2..3) { "Invalid delimiter count" }

                val hours = parts[0].toInt() + if (words.size == 2 && words[1] == Clock.PM.value) 12 else 0

                val minutes = parts[1].toInt()

                val seconds = if (parts.size == 3) parts[2].toInt() else 0

                return Time(hours, minutes, seconds)
            } catch (expected: Exception) {
                throw IllegalArgumentException("Couldn't parse time", expected)
            }
        }

        /**
         * 23:59:59
         */
        val MAX: Time
            get() = Time(23, 59, 59)

        /**
         * 00:00:00
         */
        val MIN: Time
            get() = Time(0, 0)

        /** Normalize a set of values for hours, minutes, or seconds, to a valid time value.
         *
         * @returns values of hours, minutes and seconds adjusted if necessary to fit into their respective
         * ranges. Any excess is added to the value of the next order. The values can also wrap
         * around if the resulting time is bigger than [Time.MAX]
         * Example: normalize(25,70,100) -> (2,11,40)
         */
        fun normalize(hours: Int = 0, minutes: Int = 0, seconds: Int = 0): Triple<Int, Int, Int> {
            val normalizedHours = hours + (minutes + seconds / 60) / 60
            val normalizedMinutes = (minutes + seconds / 60) % 60
            val normalizedSeconds = seconds % 60
            return Triple(normalizedHours % 24, normalizedMinutes, normalizedSeconds)
        }

        /**
         * @return whether this [text] is a valid [Time] representation
         */
        fun isValid(text: String?): Boolean {
            if (text.isNullOrBlank()) return false
            return try {
                of(text)
                true
            } catch (ignored: Exception) {
                false
            }
        }

        private const val serialVersionUID = -29334L
    }

    enum class Clock(val value: String) {
        AM("AM"), PM("PM")
    }
}
