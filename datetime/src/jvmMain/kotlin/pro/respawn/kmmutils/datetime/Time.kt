package pro.respawn.kmmutils.datetime

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Returns a new [Time] with values of [this]
 */
public fun LocalTime.toTime(): Time = Time(hour, minute, second)

/**
 * Returns a new [LocalTime] with values of [this]
 */
public fun Time.toLocalTime(): LocalTime = LocalTime.of(hour, minute, second)

/**
 * Creates a new [Time] using values of [this] zoned date time.
 */
public val ZonedDateTime.time: Time get() = Time.fromZDT(this)

/**
 * Creates a new [ZonedDateTime] using [this] Instant and a given [zoneId]
 */
public fun Instant.toZDT(
    zoneId: ZoneId,
): ZonedDateTime = ZonedDateTime.ofInstant(this, zoneId)

/**
 * Gets a time from this [zdt]. Uses [ZoneId.systemDefault] by default.
 * @see now
 */
public fun Time.Companion.fromZDT(zdt: ZonedDateTime): Time = Time(zdt.hour, zdt.minute, zdt.second)

/**
 * Creates a new [Time] using [instant] and a given [zone]
 */
public operator fun Time.Companion.invoke(instant: Instant, zone: ZoneId): Time = fromZDT(instant.toZDT(zone))

/**
 * Get current local time.
 * @return A new Time using current timezone and timestamp.
 * @see fromZDT
 */
public fun Time.Companion.now(zoneId: ZoneId): Time = Time.fromZDT(ZonedDateTime.now(zoneId))
