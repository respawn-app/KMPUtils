package com.nek12.kmmutils.datetime

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun LocalTime.toTime() = Time(hour, minute, second)

fun Time.toLocalTime(): LocalTime = LocalTime.of(hour, minute, second)

val ZonedDateTime.time get() = Time.fromZDT(this)

fun Instant.toZDT(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime = ZonedDateTime.ofInstant(this, zoneId)

/**
 * Gets a time from this [zdt]. Uses [ZoneId.systemDefault] by default.
 * @see now
 */
fun Time.Companion.fromZDT(zdt: ZonedDateTime): Time = Time(zdt.hour, zdt.minute, zdt.second)

fun Time.Companion.fromInstant(instant: Instant, zone: ZoneId): Time = fromZDT(instant.toZDT(zone))

/**
 * Get current local time.
 * @return A new Time using current timezone and timestamp.
 * @see fromZDT
 */
fun now(zoneId: ZoneId = ZoneId.systemDefault()) = Time.fromZDT(ZonedDateTime.now(zoneId))
