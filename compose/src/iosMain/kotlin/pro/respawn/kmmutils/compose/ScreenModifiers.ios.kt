package pro.respawn.kmmutils.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import platform.UIKit.UIApplication

/**
 * Keeps the device screen on while the current composable is in the composition.
 *
 * Currently supported only on iOS and Android.
 * No-op on other platforms
 */
@Composable
public actual fun KeepScreenOn(enabled: Boolean): Unit = DisposableEffect(enabled) {
    if (!enabled) return@DisposableEffect onDispose { }
    val previous = UIApplication.sharedApplication.isIdleTimerDisabled()
    UIApplication.sharedApplication.idleTimerDisabled = true
    onDispose { UIApplication.sharedApplication.idleTimerDisabled = previous }
}
