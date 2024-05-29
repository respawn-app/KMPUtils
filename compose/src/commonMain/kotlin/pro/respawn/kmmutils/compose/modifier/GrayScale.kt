package pro.respawn.kmmutils.compose.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas

/**
 * Apply 0-saturation color transformation to this composable, rendering it grayscale.
 */
public fun Modifier.grayScale(): Modifier = drawWithCache {
    val saturationMatrix = ColorMatrix().apply { setToSaturation(0f) }
    val saturationFilter = ColorFilter.colorMatrix(saturationMatrix)
    val paint = Paint().apply { colorFilter = saturationFilter }
    val canvasBounds = Rect(Offset.Zero, size)
    onDrawWithContent {
        drawIntoCanvas {
            it.saveLayer(canvasBounds, paint)
            drawContent()
            it.restore()
        }
    }
}
