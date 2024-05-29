package pro.respawn.kmmutils.compose.windowsize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

/**
 * Get the window size of the current window in pixels.
 * This matches closely width screen size on mobile devices most of the time, but windows can be resized by the user.
 */
@ExperimentalComposeUiApi
public actual val windowSizePx: IntSize
    @ReadOnlyComposable
    @Composable get() = with(LocalDensity.current) {
        with(LocalConfiguration.current) {
            IntSize(screenWidthDp.dp.roundToPx(), screenHeightDp.dp.roundToPx())
        }
    }
