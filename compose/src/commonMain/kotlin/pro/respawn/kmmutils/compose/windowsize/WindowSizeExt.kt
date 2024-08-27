package pro.respawn.kmmutils.compose.windowsize

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize

/**
 * Calculates the window's [WindowSizeClass].
 *
 * A new [WindowSizeClass] will be returned whenever a change causes the width or
 * height of the window to cross a breakpoint, such as when the device is rotated or the window
 * is resized.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
@ReadOnlyComposable
public fun calculateWindowSizeClass(): WindowSizeClass = WindowSizeClass.calculateFromSize(size = windowSize)

/**
 * Whether the window width is long (longer than most phones **in portrait**).
 *
 * This is not a real device size (although it matches closely on mobile platforms sometimes), but an app window size,
 * which can be different when resizing.
 *
 * @see WindowWidthSizeClass
 */
public inline val isWideScreen: Boolean
    @Composable get() = calculateWindowSizeClass().widthSizeClass > WindowWidthSizeClass.Compact

/**
 * Whether the window height is long (longer than most phones **in portrait**).
 *
 * This is not a real device size (although it matches closely on mobile platforms sometimes), but an app window size
 * which can be different when resizing.
 *
 * @see WindowWidthSizeClass
 */
public inline val isLongScreen: Boolean
    @Composable get() = calculateWindowSizeClass().heightSizeClass > WindowHeightSizeClass.Compact

/**
 * Get the window size of the current window in pixels.
 * This matches closely width screen size on mobile devices most of the time, but windows can be resized by the user.
 */
public expect val windowSizePx: IntSize @Composable @ReadOnlyComposable get

/**
 * Get the window size of the current window in dp.
 * This matches closely width screen size on mobile devices most of the time, but windows can be resized by the user.
 */
public val windowSize: DpSize
    @Composable @ReadOnlyComposable get() = with(LocalDensity.current) {
        windowSizePx.toSize()
            .toDpSize()
    }

/**
 * Get the window width of the current window.
 * This matches closely width screen size on mobile devices most of the time, but windows can be resized by the user.
 */
public val windowWidth: Dp @Composable get() = with(LocalDensity.current) { windowSizePx.width.toDp() }

/**
 * Get the window height of the current window.
 * This matches closely width screen size on mobile devices most of the time, but windows can be resized by the user.
 */
public val windowHeight: Dp @Composable get() = with(LocalDensity.current) { windowSizePx.height.toDp() }
