package pro.respawn.kmmutils.system.android

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.Settings
import android.webkit.CookieManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat

/**
 * Start activity safely and handle errors where an activity is not found.
 *
 * Prefer to **always** use this method instead of the default one, as any activity you may want to launch
 * **may not be found** and the original function will **throw**.
 *
 * Even system activities like file pickers, galleries and settings may be missing.
 * Even your app's activities may be unavailable.
 */
public inline fun Context.startActivityCatching(intent: Intent, onNotFound: (ActivityNotFoundException) -> Unit) {
    try {
        startActivity(intent)
    } catch (expected: ActivityNotFoundException) {
        onNotFound(expected)
    }
}

/**
 * Start activity safely and handle errors where an activity is not found.
 *
 * Prefer to **always** use this method instead of the default one, as any activity you may want to launch
 * **may not be found** and the original function will **throw**.
 *
 * Even system activities like file pickers, galleries and settings may be missing.
 * Even your app's activities may be unavailable.
 */
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
 * Saves file using [DownloadManager] to users /sdcard/[directory]
 *
 * @param notificationMode an int flag specified in [DownloadManager.Request] outlining the notification visibility.
 * If enabled, the download manager posts notifications about downloads through the system NotificationManager.
 * By default, a notification is shown only when the download is in progress.
 * It can take the following values: VISIBILITY_HIDDEN, VISIBILITY_VISIBLE, VISIBILITY_VISIBLE_NOTIFY_COMPLETED.
 * If set to `VISIBILITY_HIDDEN`, this requires the permission [Manifest.permission.DOWNLOAD_WITHOUT_NOTIFICATION]
 *
 * @param directory - one of the [Environment] `DIRECTORY_*` constants. Not all directories are available without a
 * permission to write to external storage. Available dirs are:
 * [Environment.DIRECTORY_DOWNLOADS], [Environment.DIRECTORY_PICTURES], [Environment.DIRECTORY_MOVIES], among others
 *
 * @param onFailure is called if there was an exception. Possible exceptions are:
 *     - ActivityNotFoundException
 *     - SecurityException - when permission to write to storage was not granted
 *     - IllegalStateException - when provided parameters are invalid (i.e. download directory can't be created)
 * */
public inline fun Context.downloadFile(
    url: Uri,
    fileName: String,
    userAgent: String? = null,
    title: String = fileName,
    description: String? = null,
    mimeType: String? = null,
    notificationMode: Int = DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED,
    directory: String = Environment.DIRECTORY_DOWNLOADS,
    onFailure: (e: Exception) -> Unit,
) {
    val request = DownloadManager.Request(url).apply {
        val cookies = CookieManager.getInstance().getCookie(url.toString())
        addRequestHeader("cookie", cookies)
        addRequestHeader("User-Agent", userAgent)
        if (description != null) setDescription(description)
        setTitle(title)
        setMimeType(mimeType)
        setNotificationVisibility(notificationMode)
        setDestinationInExternalPublicDir(directory, fileName)
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

/**
 * Open the system browser for the specified [url].
 *
 * **[onAppNotFound] **must** be handled as some phones do not have browsers installed. Display an error message with a
 * prompt to install a browser in this case.
 */
public inline fun Context.openBrowser(url: Uri, onAppNotFound: (e: ActivityNotFoundException) -> Unit) {
    val intent = Intent(Intent.ACTION_VIEW, url).apply {
        addCategory(Intent.CATEGORY_BROWSABLE)
    }
    startActivityCatching(intent, onAppNotFound)
}

/**
 * Open the system share sheet for sharing the specified [text].
 *
 * **[onAppNotFound] **must** be handled as some phones do not have any app to share with.
 * Display an error message with a prompt to install an app in this case.
 */
public inline fun Context.shareAsText(text: String, onAppNotFound: (e: ActivityNotFoundException) -> Unit) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(intent, null)
    startActivityCatching(shareIntent, onAppNotFound)
}

/**
 * Open an email app to send the specified [mail].
 *
 * **[onAppNotFound] **must** be handled as some phones do not have any email apps.
 * Display an error message with a prompt to install an app in this case.
 */
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

/**
 * Open system notification settings for this app.
 */
@RequiresApi(VERSION_CODES.O)
public fun Context.openNotificationSettings(onError: (ActivityNotFoundException) -> Unit): Unit = startActivityCatching(
    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    },
    onNotFound = onError,
)

/**
 * Open system notification settings for the current app.
 */
public fun Context.openAppDetails(onError: (Exception) -> Unit): Unit = startActivityCatching(
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri),
    onNotFound = onError,
)

/**
 * Open system overlay (SYSTEM_ALERT_WINDOW) settings.
 *
 * Due to platform limitations, this will display a simple list of all device apps and the user must scroll and find
 * your app there. Instruct the user as appropriate.
 */
public inline fun Context.openSystemOverlaysSettings(onError: (Exception) -> Unit) {
    startActivityCatching(
        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageUri),
        onNotFound = onError,
    )
}

/**
 * Display a prompt to ignore Doze mode optimizations and unrestrict [AlarmManager] APIs for this app.
 */
@SuppressLint("BatteryLife")
public inline fun Context.requestIgnoreBatteryOptimization(onError: (Exception) -> Unit): Unit = startActivityCatching(
    Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, packageUri),
    onNotFound = onError,
)

/**
 * Request a permission too schedule exact alarms from the user.
 * This will bring the user to a page that prompts them to flip a switch for your app.
 */
@RequiresApi(VERSION_CODES.S)
public inline fun Context.requestExactAlarmPermission(
    onError: (ActivityNotFoundException) -> Unit
): Unit = startActivityCatching(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM), onError)

/**
 * Open the app's play store page.
 *
 * **[onNotFound] must be handled because some phones do not have neither play store nor a browser app installed**
 */
public inline fun Context.openAppPlayStorePage(onNotFound: (e: Exception) -> Unit): Unit =
    openBrowser(getGooglePlayUri(), onNotFound)
