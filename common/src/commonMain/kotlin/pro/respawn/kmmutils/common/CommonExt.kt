@file:Suppress("unused")

package pro.respawn.kmmutils.common

/**
 * @return Whether this string is valid
 *
 * Examples:
 * - null -> false
 * - "null" -> false
 * - "" -> false
 * - "NULL" -> false
 * - "  " -> false
 */
public val String?.isValid: Boolean
    get() = !isNullOrBlank() && !equals("null", true)

/**
 * Takes this string only if it [isValid]
 * @see isValid
 */
public fun String?.takeIfValid(): String? = if (isValid) this else null

/**
 * Check if this String has length in [range]
 */
public infix fun String.spans(range: IntRange): Boolean = length in range

/**
 * Returns true if all chars in [this] are ASCII symbols
 */
public val String.isAscii: Boolean get() = toCharArray().none { it < ' ' || it > '~' }

/**
 * Uses [LazyThreadSafetyMode.NONE] to provide values quicker.
 */
public fun <T> fastLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

/**
 * Filter [this] by searching for elements that contain [substring],
 * or if string is not [String.isValid], the list itself
 * @param substring a string, that must be [String.isValid]
 * @return a resulting list
 */
public fun Iterable<String>.filterBySubstring(
    substring: String?,
    ignoreCase: Boolean = false
): List<String> = if (!substring.isValid) toList() else asSequence()
    .filter { it.contains(substring!!, ignoreCase) }
    .toList()

/**
 * Returns the sign of the number, as a char
 * @return either +, - or "" (empty string) if this is 0
 */
public val Int.signChar: String
    get() = when {
        this < 0 -> "-"
        this > 0 -> "+"
        else -> ""
    }

/**
 * Returns the sign of the number, as a char
 * @return either +, - or "" (empty string) if this is 0
 */
public val Float.signChar: String
    get() = when {
        this < 0f -> "-"
        this > 0f -> "+"
        else -> ""
    }

/**
 * Returns the sign of the number, as a char
 * @return either +, - or "" (empty string) if this is 0
 */
public val Double.signChar: String
    get() = when {
        this < 0.0 -> "-"
        this > 0.0 -> "+"
        else -> ""
    }

/**
 * Returns the sign of the number, as a char
 * @return either +, - or "" (empty string) if this is 0
 */
public val Long.signChar: String
    get() = when {
        this < 0L -> "-"
        this > 0L -> "+"
        else -> ""
    }
