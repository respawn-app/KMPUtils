package pro.respawn.kmmutils.system.android

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.annotation.ColorInt

/**
 * Create a span with a [clickablePart] of the text, and invokes the [onClickListener] on click.
 */
public fun SpannableString.withClickableSpan(
    clickablePart: String,
    onClickListener: () -> Unit
): SpannableString {
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) = onClickListener.invoke()
    }
    val clickablePartStart = indexOf(clickablePart)
    setSpan(
        clickableSpan,
        clickablePartStart,
        clickablePartStart + clickablePart.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return this
}

/**
 * [span] is a ..Span object like a [ForegroundColorSpan] or a [SuperscriptSpan]
 * Spans this whole string
 */
public fun CharSequence.span(span: Any): SpannableString = SpannableString(this).setSpan(span)

/**
 * Create a [SpannableStringBuilder] from this
 */
public fun CharSequence.buildSpan(): SpannableStringBuilder = SpannableStringBuilder(this)

/**
 * Create a spannable string
 */
public val CharSequence.spannable: SpannableString get() = SpannableString(this)

/**
 * [span] is a ..Span object like a [ForegroundColorSpan] or a [SuperscriptSpan]
 * Spans this whole string
 */
public fun SpannableString.setSpan(span: Any?): SpannableString = apply {
    setSpan(span, 0, length, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
}

/**
 * Changes the color of the text to [color].
 *
 * @returns a [SpannableString] that can be applied to XML views
 */
public fun CharSequence.foregroundColor(@ColorInt color: Int): SpannableString = span(ForegroundColorSpan(color))

/**
 * Changes the background color of the text to [color].
 *
 * @returns a [SpannableString] that can be applied to XML views
 */
public fun CharSequence.backgroundColor(@ColorInt color: Int): SpannableString = span(BackgroundColorSpan(color))

/**
 * Sets the size of this string to relative size [size], which is a multiplier fraction larger than 0
 *
 * @returns a [SpannableString] that can be applied to XML views
 */
public fun CharSequence.relativeSize(size: Float): SpannableString = span(RelativeSizeSpan(size))

/**
 * Sets this string to be drawn as a superscript (smaller font aligned to the top of the text, such as numeric power)
 *
 * @returns a [SpannableString] that can be applied to XML views
 */
public fun CharSequence.superscript(): SpannableString = span(SuperscriptSpan())

/**
 * Sets this string to be drawn as a superscript (smaller font aligned to the bottom of the text)
 *
 * @returns a [SpannableString] that can be applied to XML views
 */
public fun CharSequence.subscript(): SpannableString = span(SubscriptSpan())

/**
 * Strikes this string through
 *
 * @returns a [SpannableString] that can be applied to XML views
 */
public fun CharSequence.strike(): SpannableString = span(StrikethroughSpan())

/**
 * Applies bold style to this string
 *
 * @returns a [SpannableString] that can be applied to XML views
 */
public fun CharSequence.bold(): SpannableString = span(StyleSpan(Typeface.BOLD))

/**
 * Applies italic style to this string
 *
 * @returns a [SpannableString] that can be applied to XML views
 */
public fun CharSequence.italic(): SpannableString = span(StyleSpan(Typeface.ITALIC))

/**
 * Adds an underline to this string
 *
 * @returns a [SpannableString] that can be applied to XML views
 */
public fun CharSequence.underline(): SpannableString = span(UnderlineSpan())
