package pro.respawn.kmmutils.compose.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer

// srcAtop only works when alpha != 1f
public fun Modifier.tint(brush: Brush): Modifier = graphicsLayer(alpha = 0.99f)
    .drawWithCache {
        onDrawWithContent {
            drawContent()
            drawRect(brush, size = size, blendMode = BlendMode.SrcAtop)
        }
    }
