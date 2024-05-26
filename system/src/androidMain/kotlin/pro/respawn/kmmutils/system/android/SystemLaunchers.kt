package pro.respawn.kmmutils.system.android

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.provider.Settings
import android.webkit.CookieManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat

public inline fun Context.startActivityCatching(intent: Intent, onNotFound: (ActivityNotFoundException) -> Unit) {
    try {
        startActivity(intent)
    } catch (expected: ActivityNotFoundException) {
        onNotFound(expected)
    }
}

public inline fun <I> ActivityResultLauncher<I>.launchCatching(
    input: I,
    options: ActivityOptionsCompat? = null,
    onNotFound: (ActivityNotFoundException) -> Unit,
) {
    try {
        launch(input, options)
    } catch (e: ActivityNotFoundException) {
        onNotFound(e)
    }
}

/**
 * @param numberUri uri of the form tel:+1234567890, containing countryCode
 */
public inline fun Context.dialNumber(numberUri: Uri, onNotFound: (e: ActivityNotFoundException) -> Unit) {
    val intent = Intent(Intent.ACTION_DIAL, numberUri)
    startActivityCatching(intent, onNotFound)
}

/**
 * Saves file using [DownloadManager] to users /sdcard/Downloads/
 * @param onFailure is called if there was an exception. Possible exceptions are:
 *     - ActivityNotFoundException
 *     - SecurityException - when permission to write to storage was not granted
 *     - IllegalStateException - when provided parameters are invalid (i.e. download directory can't be created)
 * */
public inline fun Context.downloadFile(
    url: Uri,
    fileName: String,
    userAgent: String? = null,
    description: String? = null,
    mimeType: String? = null,
    onFailure: (e: Exception) -> Unit,
) {
    val request = DownloadManager.Request(url).apply {
        val cookies = CookieManager.getInstance().getCookie(url.toString())
        addRequestHeader("cookie", cookies)
        addRequestHeader("User-Agent", userAgent)
        if (description != null) setDescription(description)
        setTitle(fileName)
        setMimeType(mimeType)
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_DOWNLOADS, fileName)
    }
    try {
        ContextCompat.getSystemService(this, DownloadManager::class.java)
            ?.enqueue(request)
            ?: throw ActivityNotFoundException("DownloadManager not found")
    } catch (expected: Exception) {
        onFailure(expected)
        return
    }
}

public inline fun Context.openBrowser(url: Uri, onAppNotFound: (e: ActivityNotFoundException) -> Unit) {
    val intent = Intent(Intent.ACTION_VIEW, url).apply {
        addCategory(Intent.CATEGORY_BROWSABLE)
    }
    startActivityCatching(intent, onAppNotFound)
}

public inline fun Context.shareAsText(text: String, onAppNotFound: (e: ActivityNotFoundException) -> Unit) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(intent, null)
    startActivityCatching(shareIntent, onAppNotFound)
}

public inline fun Context.sendEmail(mail: Email, onAppNotFound: (e: ActivityNotFoundException) -> Unit) {
    // Use SENDTO to avoid showing pickers and letting non-email apps interfere
    val sendIntent: Intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        // EXTRA_EMAIL should be array
        putExtra(Intent.EXTRA_EMAIL, mail.recipients?.toTypedArray())
        putExtra(Intent.EXTRA_SUBJECT, mail.subject)
        putExtra(Intent.EXTRA_TEXT, mail.body)
    }
    startActivityCatching(sendIntent, onAppNotFound)
}

@RequiresApi(VERSION_CODES.O)
public fun Context.openNotificationSettings(onError: (ActivityNotFoundException) -> Unit): Unit = startActivityCatching(
    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    },
    onNotFound = onError,
)

public fun Context.openAppDetails(onError: (Exception) -> Unit): Unit = startActivityCatching(
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri),
    onNotFound = onError,
)

public inline fun Context.openSystemOverlaysSettings(onError: (Exception) -> Unit) {
    startActivityCatching(
        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageUri),
        onNotFound = onError,
    )
}

@SuppressLint("BatteryLife")
public inline fun Context.requestIgnoreBatteryOptimization(onError: (Exception) -> Unit): Unit = startActivityCatching(
    Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, packageUri),
    onNotFound = onError,
)

@RequiresApi(VERSION_CODES.S)
public inline fun Context.requestExactAlarmPermission(
    onError: (ActivityNotFoundException) -> Unit
): Unit = startActivityCatching(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM), onError)

public inline fun Context.openAppPlayStorePage(onNotFound: (e: Exception) -> Unit): Unit =
    openBrowser(getGooglePlayUri(), onNotFound)
