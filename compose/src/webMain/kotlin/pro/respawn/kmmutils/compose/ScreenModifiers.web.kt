package pro.respawn.kmmutils.compose

import androidx.compose.runtime.Composable

/**
 * Keeps the device screen on while the current composable is in the composition.
 *
 * Currently supported only on iOS and Android.
 * No-op on other platforms
 */
@Composable
public actual fun KeepScreenOn(enabled: Boolean): Unit = Unit
