@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NewApi", "MagicNumber")

package pro.respawn.kmmutils.datetime

internal const val DeprecationMessage = """
All functionality of the Time class was ported to kotlinx.datetime.LocalTime as extension functions and properties.
You can simply replace this with LocalTime and expect similar functionality to be present.
"""

/**
 * A class that represents time, of day as well as a asDuration.
 * [hour] hour in 24h format
 * [minute] minute
 * [second] second
 * [Time.toString] method returns a string representation for 24-hour format. If you want 12-hour
 * time, use [asString].
 * @throws IllegalArgumentException if the specified values are invalid. Validation happens at
 * object creation time
 */
@Deprecated(DeprecationMessage, ReplaceWith("LocalTime", "kotlinx.datetime.LocalTime"))
public data class Time @Throws(IllegalArgumentException::class) constructor(
    val hour: Int,
    val minute: Int,
    val second: Int = 0,
) : Comparable<Time> {

    init {
        require(hour in 0..<24 || minute in 0..<60 || second in 0..<60) {
            "Invalid time value: $hour:$minute:$second"
        }
    }

    override fun toString(): String = asString()

    /** Same as toString but gives you a choice on whether to use 12H scheme.
     * [toString] uses asString(false).
     *
     * @param addSecondsIfZero If the seconds value is equal to 0, should include them in the representation? e.g.:
     * @param use12h whether to represent time in 12-hour format (AM/PM letters not added). Mind that deserializing
     * the resulting string back to Time won't result in a valid value, if you have this parameter true
     * true => "17:00:00", false => "17:00".
     *
     * @return a string representation of this time
     * **/
    public fun asString(use12h: Boolean = false, addSecondsIfZero: Boolean = false): String = buildString {
        append("${asTimeNumber(if (use12h) hourAs12H else hour)}:${asTimeNumber(minute)}")
        if (addSecondsIfZero || second != 0) append(":${asTimeNumber(second)}")
        if (use12h) append(" ${if (isPM) "PM" else "AM"}")
    }

    /**
     * Create a new time with the result of adding [Time] to [other]
     */
    public operator fun plus(other: Time): Time = add(other.hour, other.minute, other.second)

    /**
     * Subtract [other] from [Time]
     */
    public operator fun minus(other: Time): Time = add(-other.hour, -other.minute, -other.second)

    override operator fun compareTo(other: Time): Int = totalSeconds.compareTo(other.totalSeconds)

    /**
     * Get either the hour or minute or second value of this time.
     * Values more than 2 will throw an [IndexOutOfBoundsException]
     */
    public operator fun get(index: Int): Int = when (index) {
        0 -> hour
        1 -> minute
        2 -> second
        else -> throw IndexOutOfBoundsException("Only 0, 1 and 2 are valid values")
    }

    @Deprecated(DeprecationMessage, ReplaceWith("LocalTime", "kotlinx.datetime.LocalTime"))
    public companion object {

        /** example: 12:45:00, 4:30, 7:00 AM, 24 or 12h format, word separator is " ".
         * On a value that is not a valid time, will throw.
         * **/
        @Throws(IllegalArgumentException::class)
        public fun parse(s: String): Time {
            try {
                require(s.isNotBlank()) { "Empty time string" }

                val words = s.split(" ")
                require(words.size in 1..2) { "Not a time" }

                val parts = words.first().split(':', '.', '-', ' ', ',', '_', ignoreCase = true)
                require(parts.size in 2..3) { "Invalid delimiter count" }

                val hours = parts[0].toInt() + if (words.size == 2 && words[1] == "PM") 12 else 0

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
        public val MAX: Time = Time(23, 59, 59)

        /**
         * 00:00:00
         */
        public val MIN: Time = Time(0, 0)

        /** Normalize a set of values for hours, minutes, or seconds, to a valid time value.
         *
         * @returns values of hours, minutes and seconds adjusted if necessary to fit into their respective
         * ranges. Any excess is added to the value of the next order. The values can also wrap
         * around if the resulting time is bigger than [Time.MAX]
         * Example: normalize(25,70,100) -> (2,11,40)
         */
        internal fun normalize(hours: Int = 0, minutes: Int = 0, seconds: Int = 0): Triple<Int, Int, Int> {
            val normalizedHours = hours + (minutes + seconds / 60) / 60
            val normalizedMinutes = (minutes + seconds / 60) % 60
            val normalizedSeconds = seconds % 60
            return Triple(normalizedHours % 24, normalizedMinutes, normalizedSeconds)
        }
    }
}
