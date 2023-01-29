package pro.respawn.kmmutils.common

import java.math.BigDecimal
import java.util.Locale
import java.util.UUID

public fun Float.format(digits: Int, locale: Locale? = null): String = "%.${digits}f".format(locale, this)

public val String.asUUID: UUID
    get() = UUID.fromString(this)

public val BigDecimal.sign: String get() = if (signum() < 0) "–" else ""
