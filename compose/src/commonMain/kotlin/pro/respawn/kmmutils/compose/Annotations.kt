package pro.respawn.kmmutils.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit

public fun String.span(spanStyle: SpanStyle): AnnotatedString = buildAnnotatedString {
    withStyle(spanStyle) {
        append(this@span)
    }
}

public fun String.bold(): AnnotatedString = span(SpanStyle(fontWeight = FontWeight.Bold))

public fun String.italic(): AnnotatedString = span(SpanStyle(fontStyle = FontStyle.Italic))

public fun String.strike(): AnnotatedString = span(SpanStyle(textDecoration = TextDecoration.LineThrough))

public fun String.underline(): AnnotatedString = span(SpanStyle(textDecoration = TextDecoration.Underline))

public fun String.decorate(
    vararg decorations: TextDecoration
): AnnotatedString = span(SpanStyle(textDecoration = TextDecoration.combine(decorations.asList())))

public fun String.size(size: TextUnit): AnnotatedString = span(SpanStyle(fontSize = size))

public fun String.background(color: Color): AnnotatedString = span(SpanStyle(background = color))

public fun String.color(color: Color): AnnotatedString = span(SpanStyle(color = color))

public fun String.weight(weight: FontWeight): AnnotatedString = span(SpanStyle(fontWeight = weight))

public fun String.style(style: FontStyle): AnnotatedString = span(SpanStyle(fontStyle = style))

public fun String.shadow(
    color: Color,
    offset: Offset = Offset.Zero,
    blurRadius: Float = 0.0f
): AnnotatedString = span(SpanStyle(shadow = Shadow(color, offset, blurRadius)))

public fun String.fontFamily(fontFamily: FontFamily): AnnotatedString = span(SpanStyle(fontFamily = fontFamily))

/**
 * Produces an annotated string identical to the original string
 * For those times when an api requires AnnotatedString but you don't want to build one
 */
public fun String.annotate(): AnnotatedString = AnnotatedString(this)

@Suppress("ComposableEventParameterNaming")
public inline fun String.annotate(builder: AnnotatedString.Builder.(String) -> Unit): AnnotatedString =
    buildAnnotatedString { builder(this@annotate) }

// TODO: Waiting for compose update

// public fun String.clickable(onClick: () -> Unit): AnnotatedString = annotate {
//     pushLink(LinkAnnotation.Clickable("clickable") { onClick() })
//     pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
//     append(this@clickable)
//     pop()
//     pop()
// }
