package pro.respawn.kmmutils.compose

import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import pro.respawn.kmmutils.system.android.setFullScreen

/**
 * Keeps the screen of the current activity on while this composable is in composition.
 */
@Composable
public actual fun KeepScreenOn(enabled: Boolean) {
    val activity = LocalActivity.current
    DisposableEffect(enabled) {
        if (!enabled) return@DisposableEffect onDispose { }
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

/**
 * Put the current into full screen mode based on [enabled]
 */
@Composable
public fun FullScreenMode(enabled: Boolean) {
    val activity = LocalActivity.current
    DisposableEffect(enabled) {
        if (!enabled || activity == null) return@DisposableEffect onDispose { }
        activity.setFullScreen(true)
        onDispose { activity.setFullScreen(false) }
    }
}
