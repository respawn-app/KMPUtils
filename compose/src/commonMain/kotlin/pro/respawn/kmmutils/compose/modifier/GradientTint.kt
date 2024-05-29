package pro.respawn.kmmutils.compose.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Tint the composable with the provided [brush]. This will draw over any pixels that are not completely transparent in
 * the source composable. For example, this can be used to tint over icons or any other complex shapes.
 */
// srcAtop only works when alpha != 1f
public fun Modifier.tint(brush: Brush): Modifier = graphicsLayer(alpha = 0.99f)
    .drawWithCache {
        onDrawWithContent {
            drawContent()
            drawRect(brush, size = size, blendMode = BlendMode.SrcAtop)
        }
    }

/**
 * Tint the composable with the provided [color]. This will draw over any pixels that are not completely transparent in
 * the source composable. For example, this can be used to tint over icons or any other complex shapes.
 */
public fun Modifier.tint(color: Color): Modifier = graphicsLayer(alpha = 0.99f)
    .drawWithCache {
        onDrawWithContent {
            drawContent()
            drawRect(color = color, size = size, blendMode = BlendMode.SrcAtop)
        }
    }
