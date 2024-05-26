package pro.respawn.kmmutils.compose.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawTransform
import androidx.compose.ui.graphics.drawscope.withTransform

public enum class FlipDirection(internal val x: Float, internal val y: Float) {
    Vertical(1f, -1f),
    Horizontal(-1f, 1f)
}

public fun Modifier.flip(direction: FlipDirection): Modifier = scale(direction.x, direction.y)

public fun DrawTransform.flip(direction: FlipDirection?, pivot: Offset = center) {
    direction?.run { scale(x, y, pivot) }
}

public inline fun DrawScope.flip(
    direction: FlipDirection?,
    pivot: Offset = center,
    block: DrawScope.() -> Unit
): Unit = withTransform(
    transformBlock = { flip(direction, pivot) },
    drawBlock = block
)
