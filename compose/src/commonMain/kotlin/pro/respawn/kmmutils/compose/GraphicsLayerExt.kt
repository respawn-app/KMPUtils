package pro.respawn.kmmutils.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.GraphicsLayerScope

/**
 * Scale the element uniformly. Alias for [GraphicsLayerScope.scaleX] and [GraphicsLayerScope.scaleY].
 * Getter returns the average value of the scale
 */
public var GraphicsLayerScope.scale: Float
    get() = (scaleX + scaleY) / 2f
    set(value) {
        scaleX = value
        scaleY = value
    }

/**
 * Inverts the X and Y coordinates of a given offset
 */
public inline val Offset.inverted: Offset get() = Offset(-x, -y)

/**
 * Inverts the X coordinate of a given offset
 */
public inline val Offset.invertedX: Offset get() = copy(x = -x)

/**
 * Inverts the Y coordinate of a given offset
 */
public inline val Offset.invertedY: Offset get() = copy(x = -x)

/**
 * Offset expressed as a fraction of the size of the widget.
 */
public typealias FractionalOffset = Offset

/**
 * Converts fractional offset to absolute offset
 */
public fun FractionalOffset.asAbsolute(size: Size): Offset = Offset(x * size.width, y * size.height)

/**
 * Converts absolute offset to fractional offset
 */
public fun Offset.asFractional(size: Size): FractionalOffset = Offset(x / size.width, y / size.height)

/**
 * Applies an [offset] to this [GraphicsLayerScope]
 */
public fun GraphicsLayerScope.offset(offset: Offset) {
    translationX = offset.x
    translationY = offset.y
}
