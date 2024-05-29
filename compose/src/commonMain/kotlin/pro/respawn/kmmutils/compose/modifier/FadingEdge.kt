package pro.respawn.kmmutils.compose.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * The edge to use with [fadingEdge] modifier.
 */
public enum class FadingEdge {
    Start, End, Top, Bottom
}

private const val EdgeAlpha = 0.99f

/**
 * Makes this widget "fade out" into transparency over specified [size].
 * The edge specified by [fadingEdge] will be faded only.
 *
 * For RTL layouts, if [rtlAware] is true, automatically inverts the fading edge direction.
 */
public fun Modifier.fadingEdge(
    fadingEdge: FadingEdge,
    size: Dp,
    rtlAware: Boolean = false,
): Modifier = composed {
    val direction = LocalLayoutDirection.current
    val invert = direction == LayoutDirection.Rtl && rtlAware
    val edge = when (fadingEdge) {
        FadingEdge.Top, FadingEdge.Bottom -> fadingEdge
        FadingEdge.Start -> if (invert) FadingEdge.End else FadingEdge.Start
        FadingEdge.End -> if (invert) FadingEdge.Start else FadingEdge.End
    }
    graphicsLayer { alpha = EdgeAlpha }.drawWithCache {
        val colors = listOf(Color.Transparent, Color.Black)
        val sizePx = size.toPx()
        val brush = when (edge) {
            FadingEdge.Start -> Brush.horizontalGradient(colors, startX = 0f, endX = sizePx)
            FadingEdge.End -> Brush.horizontalGradient(
                colors.reversed(),
                startX = this.size.width - sizePx,
                endX = this.size.width
            )

            FadingEdge.Top -> Brush.verticalGradient(colors, startY = 0f, endY = sizePx)
            FadingEdge.Bottom -> Brush.verticalGradient(
                colors.reversed(),
                startY = this.size.height - sizePx,
                endY = this.size.height
            )
        }
        onDrawWithContent {
            drawContent()
            drawRect(
                brush = brush,
                blendMode = BlendMode.DstIn
            )
        }
    }
}
