package pro.respawn.kmmutils.compose

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

/**
 * @returns whether Blur composition is currently supported.
 * Currently returns `false` on all platforms except android as this is not yet implemented in Compose.
 */
@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
public val supportsBlur: Boolean get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

/**
 * @return whether dynamic colors are supported. Only supported on Android since S, all other platforms return false
 */
@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
public val supportsDynamicColors: Boolean get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

/**
 * @return whether dynamic colors are supported. Only supported on Android since S, all other platforms return false
 */
@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
public val supportsShaders: Boolean get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
