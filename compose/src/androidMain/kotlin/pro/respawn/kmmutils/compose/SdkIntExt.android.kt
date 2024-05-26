package pro.respawn.kmmutils.compose

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
public actual val supportsBlur: Boolean get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
public actual val supportsDynamicColors: Boolean get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
public actual val supportsShaders: Boolean get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
