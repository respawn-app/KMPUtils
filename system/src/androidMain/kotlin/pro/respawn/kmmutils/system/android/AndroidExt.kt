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
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import androidx.core.net.toUri

@PublishedApi
internal const val EXTRA_SYSTEM_ALERT_WINDOW: String = "system_alert_window"

public val Context.autofillManager: AutofillManager?
    get() = withApiLevel(VERSION_CODES.O, below = { null }) { getSystemService<AutofillManager>() }

public fun Context.restartApplication() {
    // Obtain the startup Intent of the application with the package name of the application
    val intent: Intent? = packageManager.getLaunchIntentForPackage(packageName)?.apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}

public val Context.isSystem24Hour: Boolean get() = DateFormat.is24HourFormat(this)

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

public val Context.packageUri: Uri get() = "package:$packageName".toUri()

@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
public inline fun withApiLevel(versionCode: Int, since: () -> Unit) {
    if (Build.VERSION.SDK_INT >= versionCode) since()
}

@ChecksSdkIntAtLeast(parameter = 0, lambda = 2)
public inline fun <R> withApiLevel(versionCode: Int, below: () -> R, since: () -> R): R =
    if (Build.VERSION.SDK_INT >= versionCode) since() else below()

public inline fun <T> Context.withPermission(
    permission: String,
    ifDenied: Context.(String) -> T,
    ifGranted: Context.(String) -> T
): T = if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
    ifGranted(permission) else ifDenied(permission)

public inline fun Context.withPermission(
    permission: String,
    ifGranted: Context.(String) -> Unit,
) {
    if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) ifGranted(permission)
}

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

public fun Context.getGooglePlayUri(): Uri =
    "https://play.google.com/store/apps/details?id=${applicationInfo.packageName}".toUri()
