@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NewApi", "MagicNumber")

package pro.respawn.kmmutils.datetime

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
@OptIn(ExperimentalStdlibApi::class)
public data class Time @Throws(IllegalArgumentException::class) constructor(
    val hour: Int,
    val minute: Int,
    val second: Int = 0,
) : Comparable<Time> {

    init {
        require(hour in 0 ..< 24 || minute in 0 ..< 60 || second in 0 ..< 60) {
            "Invalid time value: $hour:$minute:$second"
        }
    }

    override fun toString(): String = asString()

    /** Same as toString but gives you a choice on whether to use 12H scheme.
     * [toString] uses asString(false)
     * @param addSecondsIfZero If the seconds value is equal to 0, should include them in the representation? e.g.:
     * @param use12h whether to represent time in 12-hour format (AM/PM letters not added). Mind that deserializing
     * the resulting string back to Time won't result in a valid value, if you have this parameter true
     * true => "17:00:00", false => "17:00"
     * **/
    public fun asString(use12h: Boolean = false, addSecondsIfZero: Boolean = false): String = buildString {
        append("${asString(if (use12h) hourAs12H else hour)}:${asString(minute)}")
        if (addSecondsIfZero || second != 0) append(":${asString(second)}")
        if (use12h) append(" ${if (isPM) "PM" else "AM"}")
    }

    public operator fun plus(other: Time): Time = add(other.hour, other.minute, other.second)

    public operator fun minus(other: Time): Time = add(-other.hour, -other.minute, -other.second)

    override operator fun compareTo(other: Time): Int = totalSeconds.compareTo(other.totalSeconds)

    public operator fun get(index: Int): Int = when (index) {
        0 -> hour
        1 -> minute
        2 -> second
        else -> throw IndexOutOfBoundsException("Only 0, 1 and 2 are valid values")
    }

    public companion object {

        /**
         * Represents this numeric value as if it is a number on the clock
         * Examples:
         * - 0 -> "00"
         * - 1 -> "01"
         * - 15 -> "15"
         */
        public fun asString(value: Int): String = if (value < 10) "0$value" else value.toString()

        /**
         * Parse a new time object using the int representation of it.
         * @see Time.toInt
         */
        public fun fromInt(hms: Int): Time = Time(hms / 10000, hms / 100 % 100, hms % 100)

        /**
         * Create a time from milliseconds since midnight.
         * **[millis] is NOT a timestamp**
         */
        public fun fromMillisSinceMidnight(millis: Long): Time {
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
        public fun fromSecondsSinceMidnight(seconds: Int): Time = fromMillisSinceMidnight(seconds * 1000L)

        /**
         * Create a new time using specified [hours], [minutes], or [seconds]
         * If you specify a value bigger than the initial, the remainder will spill into the next
         * value. If you specify a value bigger than [Time.MAX], it will wrap around.
         * Examples:
         * - Time.with(seconds=70) -> 00:01:10
         * - Time.with(hours=25, minutes=60) -> 02:00:00
         */
        public fun with(hours: Int = 0, minutes: Int = 0, seconds: Int = 0): Time = MIN.add(hours, minutes, seconds)

        /** example: 12:45:00, 4:30, 7:00 AM, 24 or 12h format, word separator is " ".
         * On a value that is not a valid time, will throw.
         * **/
        @Throws(IllegalArgumentException::class)
        public fun parse(s: String): Time {
            try {
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

        /**
         * @return whether this [text] is a valid [Time] representation
         */
        public fun isValid(text: String?): Boolean {
            if (text.isNullOrBlank()) return false
            return try {
                parse(text)
                true
            } catch (ignored: Exception) {
                false
            }
        }
    }
}
