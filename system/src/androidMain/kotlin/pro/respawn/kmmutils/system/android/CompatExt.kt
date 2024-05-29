package pro.respawn.kmmutils.system.android

import android.Manifest
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission

/**
 * Vibrates using this [Vibrator] for [lengthMs] milliseconds.
 *
 * * Requires a [Manifest.permission.VIBRATE] to function.
 * * Works on versions of android down to 21.
 *
 * @param usage required which is defined in [VibrationAttributes].
 * @param amplitude is defined in [VibrationEffect]. When null, [VibrationEffect.DEFAULT_AMPLITUDE] will be used.
 */
@Suppress("DEPRECATION")
@RequiresPermission(Manifest.permission.VIBRATE)
public fun Vibrator.vibrateCompat(
    lengthMs: Long,
    usage: Int,
    amplitude: Int? = null,
): Unit = when (Build.VERSION.SDK_INT) {
    Build.VERSION_CODES.TIRAMISU -> vibrate(
        VibrationEffect.createOneShot(lengthMs, amplitude ?: VibrationEffect.DEFAULT_AMPLITUDE),
        VibrationAttributes.createForUsage(usage)
    )
    Build.VERSION_CODES.Q -> vibrate(VibrationEffect.createOneShot(lengthMs, VibrationEffect.DEFAULT_AMPLITUDE))
    else -> vibrate(lengthMs)
}

/**
 * Vibrates using this [Vibrator] using provided [waveform].
 *
 * In effect, the timings array represents the number of milliseconds before turning the vibrator on,
 * followed by the number of milliseconds to keep the vibrator on, then the number of milliseconds turned off,
 * and so on. Consequently, the first timing value will often be 0, so that the effect will start vibrating immediately.
 *
 * * Requires a [Manifest.permission.VIBRATE] to function.
 * * Works on versions of android down to 21.
 *
 * @param usage required which is defined in [VibrationAttributes].
 * @param repeat the index into the [waveform], at which to start indefinite repetitions of the vibration,
 * where -1 means no repeat. If you pass anything but -1 there, you **must** cancel the vibration manually by calling
 * [Vibrator.cancel]
 */
@Suppress("DEPRECATION")
@RequiresPermission(Manifest.permission.VIBRATE)
public fun Vibrator.vibrateCompat(
    waveform: LongArray,
    usage: Int,
    repeat: Int = -1,
): Unit = when (Build.VERSION.SDK_INT) {
    Build.VERSION_CODES.TIRAMISU -> vibrate(
        VibrationEffect.createWaveform(waveform, repeat),
        VibrationAttributes.createForUsage(usage)
    )
    Build.VERSION_CODES.Q -> vibrate(VibrationEffect.createWaveform(waveform, repeat))
    else -> vibrate(waveform, repeat)
}

/**
 *
 * This function will query the [ApplicationInfo] of the [packageName] provided.
 *
 * Will work only if [Manifest.permission.QUERY_ALL_PACKAGES] permission is present
 *
 * @param flags are [PackageManager.ApplicationInfoFlags] and are listed as members of the [PackageManager] class.
 */
@RequiresPermission(Manifest.permission.QUERY_ALL_PACKAGES)
public fun PackageManager.getAppInfoCompat(packageName: String, flags: Int): ApplicationInfo = withApiLevel(
    versionCode = Build.VERSION_CODES.TIRAMISU,
    below = { getApplicationInfo(packageName, flags) },
    since = { getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(flags.toLong())) }
)

/**
 * This function will query the resolvable (exported) activities of the target [intent] using this [PackageManager].
 * This is useful when you want to start another app.
 *
 * Will work only if [Manifest.permission.QUERY_ALL_PACKAGES] permission is present.
 *
 * @param flags are [PackageManager.ResolveInfoFlags] and are listed as members of the [PackageManager] class.
 * @return the [List] of [ResolveInfo]s for launchable activities.
 */
@RequiresPermission(Manifest.permission.QUERY_ALL_PACKAGES)
public fun PackageManager.queryIntentActivitiesCompat(
    intent: Intent,
    flags: Int = 0,
): List<ResolveInfo> = withApiLevel(
    versionCode = Build.VERSION_CODES.TIRAMISU,
    below = { queryIntentActivities(intent, flags) },
    since = { queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(flags.toLong())) },
)

/**
 * Will get a full list of userspace apps installed on the device + some of the system apps that can be resolved.
 *
 * @param flags are [PackageManager.PackageInfoFlags] listed in the [PackageManager] class.
 */
@RequiresPermission(Manifest.permission.QUERY_ALL_PACKAGES)
public fun PackageManager.getInstalledPackagesCompat(
    flags: Int = 0,
): List<PackageInfo> = withApiLevel(
    versionCode = Build.VERSION_CODES.TIRAMISU,
    below = { getInstalledPackages(flags) },
    since = { getInstalledPackages(PackageManager.PackageInfoFlags.of(flags.toLong())) }
)
