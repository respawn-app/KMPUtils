package pro.respawn.kmmutils.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

/**
 * Registers a new lifecycle observer for the lifetime of the composition of this function, then clears it.
 */
@Composable
public fun ObserveLifecycle(onEvent: (event: Lifecycle.Event) -> Unit) {
    val lifecycle = LocalLifecycleOwner.current
    val action by rememberUpdatedState(onEvent)
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event -> action(event) }

        lifecycle.lifecycle.addObserver(observer)

        onDispose {
            lifecycle.lifecycle.removeObserver(observer)
        }
    }
}

/**
 * Keeps the device screen on while the current composable is in the composition.
 *
 * Currently supported only on iOS and Android.
 * No-op on other platforms
 */
@Composable
public expect fun KeepScreenOn(enabled: Boolean = true)
