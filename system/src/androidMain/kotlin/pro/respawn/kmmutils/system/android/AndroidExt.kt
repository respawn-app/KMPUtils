@file:Suppress("unused")

package pro.respawn.kmmutils.system.android

import android.app.ActivityOptions
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.text.format.DateFormat
import android.view.autofill.AutofillManager
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import androidx.core.net.toUri

/**
 * Get the current system [AutofillManager] if present.
 */
public val Context.autofillManager: AutofillManager?
    get() = withApiLevel(VERSION_CODES.O, below = { null }) { getSystemService<AutofillManager>() }

/**
 * Restart the current activity in a new task.
 */
public fun Context.restartActivity() {
    // Obtain the startup Intent of the application with the package name of the application
    val intent: Intent? = packageManager.getLaunchIntentForPackage(packageName)?.apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}

/**
 * Returns whether the system is set to 24-hour time format right now.
 */
public val Context.isSystem24Hour: Boolean
    get() = runCatching {
        // this trash can throw on Android 15
        // https://issuetracker.google.com/issues/406271687
        DateFormat.is24HourFormat(this)
    }.getOrDefault(true)

/**
 * Use this [Uri] as a deeplink intent, i.e. open this uri in a new task.
 */
public fun Uri.asDeeplinkIntent(
    requestCode: Int,
    context: Context
): PendingIntent = TaskStackBuilder.create(context).run {
    addNextIntentWithParentStack(intent().allowBackgroundActivityStart())
    getPendingIntent(
        requestCode,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )!!
}

/**
 * Get the uri of this app's package.
 */
public val Context.packageUri: Uri get() = "package:$packageName".toUri()

/**
 * Execute [since] if the current sdk version is at least [versionCode]. Version code comes from [Build.VERSION_CODES].
 */
@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
public inline fun withApiLevel(versionCode: Int, since: () -> Unit) {
    if (Build.VERSION.SDK_INT >= versionCode) since()
}

/**
 * Execute [since] if the current sdk version is at least [versionCode], otherwise execute [below]
 *
 * Version code comes from [Build.VERSION_CODES].
 */
@ChecksSdkIntAtLeast(parameter = 0, lambda = 2)
public inline fun <R> withApiLevel(versionCode: Int, below: () -> R, since: () -> R): R =
    if (Build.VERSION.SDK_INT >= versionCode) since() else below()

/**
 * Execute [ifGranted] if the [permission] has been granted, otherwise execute [ifDenied].
 *
 * This function does not request the permission.
 *
 * [permission] parameter is defined in [android.Manifest.permission]
 */
@RequiresApi(VERSION_CODES.M)
public inline fun <T> Context.withPermission(
    permission: String,
    ifDenied: Context.(String) -> T,
    ifGranted: Context.(String) -> T
): T = if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
    ifGranted(permission) else ifDenied(permission)

/**
 * Execute [ifGranted] if the [permission] has been granted, otherwise do nothing.
 *
 * This function does not request the permission.
 *
 * [permission] parameter is defined in [android.Manifest.permission]
 */
@RequiresApi(VERSION_CODES.M)
public inline fun Context.withPermission(
    permission: String,
    ifGranted: Context.(String) -> Unit,
) {
    if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) ifGranted(permission)
}

/**
 * Create a view intent from this uri.
 */
public fun Uri.intent(): Intent = Intent(Intent.ACTION_VIEW, this)

/**
 * Allows background activity start for this intent starting with SDK 34 (Upside down cake).
 * On previous versions, does nothing.
 */
public fun Intent.allowBackgroundActivityStart(): Intent = apply {
    withApiLevel(VERSION_CODES.UPSIDE_DOWN_CAKE) {
        ActivityOptions
            .makeBasic()
            .setPendingIntentCreatorBackgroundActivityStartMode(ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED)
            .toBundle()
            .let(::putExtras)
    }
}

/**
 * Get an uri to a google play store page of this app.
 */
public fun Context.getGooglePlayUri(): Uri =
    "https://play.google.com/store/apps/details?id=${applicationInfo.packageName}".toUri()
