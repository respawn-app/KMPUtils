@file:Suppress("unused")

package pro.respawn.kmmutils.system.android

import android.graphics.Color
import android.text.Spanned
import androidx.core.text.HtmlCompat
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Locale

/**
 * If this is a valid hex color string representation, returns its R, G and B components
 * @throws IllegalArgumentException if the color string is invalid
 *
 */
public fun String.hexToRGB(): Triple<Int, Int, Int> {
    var name = this
    if (!name.startsWith("#")) {
        name = "#$this"
    }
    val color = Color.parseColor(name)
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)
    return Triple(red, green, blue)
}

/**
 * If this is a color int, turns it into a hex string.
 */
public fun Int.colorToHexString(): String = String.format(Locale.ROOT, "#%06X", -0x1 and this).replace("#FF", "#")

/**
 * Create an android [Spanned] from this string by parsing it as an html text
 */
public val String.asHTML: Spanned
    get() = HtmlCompat.fromHtml(this, 0)

/**
 * @param algorithm – the name of the algorithm requested.
 * See the [MessageDigest] for information about standard algorithm names and supported API levels
 */
public fun ByteArray.hash(algorithm: String): ByteArray? = try {
    MessageDigest.getInstance(algorithm).run {
        update(this@hash)
        digest()
    }
} catch (expected: NoSuchAlgorithmException) {
    null
}
