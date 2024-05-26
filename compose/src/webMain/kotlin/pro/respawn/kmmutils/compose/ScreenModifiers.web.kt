package pro.respawn.kmmutils.compose

import androidx.compose.runtime.Composable

/**
 * Keeps the device screen on while the current composable is in the composition. Not supported on all platforms
 */
@Composable
public actual fun KeepScreenOn(enabled: Boolean): Unit = Unit
