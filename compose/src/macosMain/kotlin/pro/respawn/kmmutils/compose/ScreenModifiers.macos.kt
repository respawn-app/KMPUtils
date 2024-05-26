package pro.respawn.kmmutils.compose

import androidx.compose.runtime.Composable

@Composable
public actual fun KeepScreenOn(enabled: Boolean) {
    // val key = currentCompositeKeyHash
    // DisposableEffect(enabled) {
    //     val ref = CFBridgingRetain(key)
    //     if (!enabled) return@DisposableEffect onDispose { }
    //     IOPMAssertionCreate(
    //         CFBridgingRetain(kIOPMAssertPreventUserIdleDisplaySleep) as CFStringRef,
    //         kIOPMAssertionLevelOn,
    //         ref
    //     )
    //     onDispose {
    //
    //     }
    // }

    // TODO:
    //   currently huge pain and really unsafe to call
}
