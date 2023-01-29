package pro.respawn.kmmutils.datetime

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

public fun LocalTime.toTime(): Time = Time(hour, minute, second)

public fun Time.toLocalTime(): LocalTime = LocalTime.of(hour, minute, second)

public val ZonedDateTime.time: Time get() = Time.fromZDT(this)

public fun Instant.toZDT(
    zoneId: ZoneId,
): ZonedDateTime = ZonedDateTime.ofInstant(this, zoneId)

/**
 * Gets a time from this [zdt]. Uses [ZoneId.systemDefault] by default.
 * @see now
 */
public fun Time.Companion.fromZDT(zdt: ZonedDateTime): Time = Time(zdt.hour, zdt.minute, zdt.second)

public fun Time.Companion.fromInstant(instant: Instant, zone: ZoneId): Time = fromZDT(instant.toZDT(zone))

/**
 * Get current local time.
 * @return A new Time using current timezone and timestamp.
 * @see fromZDT
 */
public fun Time.Companion.now(zoneId: ZoneId): Time = Time.fromZDT(ZonedDateTime.now(zoneId))
