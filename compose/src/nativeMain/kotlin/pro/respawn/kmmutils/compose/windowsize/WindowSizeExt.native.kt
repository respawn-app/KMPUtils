package pro.respawn.kmmutils.compose.windowsize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntSize

/**
 * Get the window size of the current window in pixels.
 * This matches closely width screen size on mobile devices most of the time, but windows can be resized by the user.
 */
@OptIn(ExperimentalComposeUiApi::class)
public actual val windowSizePx: IntSize
    @ReadOnlyComposable
    @Composable get() = LocalWindowInfo.current.containerSize
