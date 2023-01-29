package pro.respawn.kmmutils.common

import java.math.BigDecimal
import java.util.Locale
import java.util.UUID

/**
 * Formats this Float to string  using specified number of [digits] and a given [locale]
 */
public fun Float.format(digits: Int, locale: Locale? = null): String = "%.${digits}f".format(locale, this)

/**
 * Tries to parse [this] as an UUID
 * @throws IllegalArgumentException on failure
 */
public val String.asUUID: UUID
    get() = UUID.fromString(this)

/**
 * Returns the sign of [this].
 */
public val BigDecimal.sign: String
    get() = when (signum()) {
        -1 -> "-"
        1 -> "+"
        else -> ""
    }
