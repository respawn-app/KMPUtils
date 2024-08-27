package pro.respawn.kmmutils.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit

/**
 * Annotates this string with the [spanStyle] provided. The whole string is annotated.
 *
 * @return the [AnnotatedString] created
 */
public fun String.annotate(spanStyle: SpanStyle): AnnotatedString = buildAnnotatedString {
    withStyle(spanStyle) {
        append(this@annotate)
    }
}

/**
 * Produces an annotated string identical to the original string
 * For those times when an api requires AnnotatedString but you don't want to build one
 */
public fun String.annotate(): AnnotatedString = AnnotatedString(this)

/**
 * Annotates this string using the [builder] provided. The string is the argument of the lambda.
 *
 * @return the [AnnotatedString] created
 */
public inline fun String.annotate(
    builder: AnnotatedString.Builder.(String) -> Unit
): AnnotatedString = buildAnnotatedString { builder(this@annotate) }

/**
 * Applies [FontWeight.Bold] to `this` string
 *
 * @return the [AnnotatedString] created
 */
public fun String.bold(): AnnotatedString = weight(FontWeight.Bold)

/**
 * Applies [FontStyle.Italic] to `this` string
 *
 * @return the [AnnotatedString] created
 */
public fun String.italic(): AnnotatedString = style(FontStyle.Italic)

/**
 * Strikes through this string
 *
 * @return the [AnnotatedString] created
 */
public fun String.strike(): AnnotatedString = annotate(SpanStyle(textDecoration = TextDecoration.LineThrough))

/**
 * Adds an underline to this string
 *
 * @return the [AnnotatedString] created
 */
public fun String.underline(): AnnotatedString = annotate(SpanStyle(textDecoration = TextDecoration.Underline))

/**
 * Adds text [decorations] provided to this string
 *
 * @return the [AnnotatedString] created
 */
public fun String.decorate(
    vararg decorations: TextDecoration
): AnnotatedString = annotate(SpanStyle(textDecoration = TextDecoration.combine(decorations.asList())))

/**
 * Applies absolute [size] to the text
 *
 * @return the [AnnotatedString] created
 */
public fun String.size(size: TextUnit): AnnotatedString = annotate(SpanStyle(fontSize = size))

/**
 * Sets the background [color] to this string
 *
 * @return the [AnnotatedString] created
 */
public fun String.background(color: Color): AnnotatedString = annotate(SpanStyle(background = color))

/**
 * Sets the foreground [color] of this string
 *
 * @return the [AnnotatedString] created
 */
public fun String.color(color: Color): AnnotatedString = annotate(SpanStyle(color = color))

/**
 * Sets the font [weight] for this string
 *
 * @return the [AnnotatedString] created
 */
public fun String.weight(weight: FontWeight): AnnotatedString = annotate(SpanStyle(fontWeight = weight))

/**
 * Applies a [style] [FontStyle] to this string
 *
 * @return the [AnnotatedString] created
 */
public fun String.style(style: FontStyle): AnnotatedString = annotate(SpanStyle(fontStyle = style))

/**
 * Adds a shadow to `this` string. The shadow has a [color], an [offset] and a [blurRadius]
 *
 * @return the [AnnotatedString] created
 */
public fun String.shadow(
    color: Color,
    offset: Offset = Offset.Zero,
    blurRadius: Float = 0.0f
): AnnotatedString = annotate(SpanStyle(shadow = Shadow(color, offset, blurRadius)))

/**
 * Changes the [fontFamily] of this string
 *
 * @return the [AnnotatedString] created
 */
public fun String.font(fontFamily: FontFamily): AnnotatedString = annotate(SpanStyle(fontFamily = fontFamily))

public fun String.clickable(onClick: () -> Unit): AnnotatedString = annotate {
    pushLink(LinkAnnotation.Clickable("clickable") { onClick() })
    pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
    append(this@clickable)
    pop()
    pop()
}
