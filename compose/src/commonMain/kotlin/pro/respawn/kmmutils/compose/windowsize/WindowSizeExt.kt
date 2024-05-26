package pro.respawn.kmmutils.compose.windowsize

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

/**
 * Whether the device's width is long (longer than most phones **in portrait**)
 * @see WindowWidthSizeClass
 */
public inline val isWideScreen: Boolean
    @Composable get() = calculateWindowSizeClass().widthSizeClass > WindowWidthSizeClass.Compact

/**
 * Whether the device's height is long (longer than most phones **in landscape** have)
 * @see WindowHeightSizeClass
 */
public inline val isLongScreen: Boolean
    @Composable get() = calculateWindowSizeClass().heightSizeClass > WindowHeightSizeClass.Compact

@ExperimentalComposeUiApi
public val screenWidth: Dp @Composable get() = withDensity { screenWidthPx.toDp() }

@ExperimentalComposeUiApi
public val screenHeight: Dp @Composable get() = withDensity { screenWidthPx.toDp() }

@ExperimentalComposeUiApi
public val screenWidthPx: Int @Composable get() = LocalWindowInfo.current.containerSize.width

@ExperimentalComposeUiApi
public val screenHeightPx: Int @Composable get() = LocalWindowInfo.current.containerSize.height

@Composable
public inline fun <T> withDensity(block: @Composable Density.() -> T): T = with(LocalDensity.current) { block() }
