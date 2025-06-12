package pro.respawn.kmmutils.system.android

import android.app.Activity
import android.os.Build
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Set the activity to display immersively on all versions of android
 */
public fun Activity.setFullScreen(enabled: Boolean) {
    withApiLevel(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        requestFullscreenMode(
            if (enabled) Activity.FULLSCREEN_MODE_REQUEST_ENTER else Activity.FULLSCREEN_MODE_REQUEST_EXIT,
            null
        )
    }
    val window = window ?: return
    with(WindowCompat.getInsetsController(window, window.decorView)) {
        if (enabled) {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            show(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        }
    }
}
