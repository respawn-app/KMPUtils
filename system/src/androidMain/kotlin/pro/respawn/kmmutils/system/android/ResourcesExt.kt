@file:Suppress("unused")

package pro.respawn.kmmutils.system.android

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.os.ConfigurationCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Get dimension dp value from your xml.
 * When you use [Resources.getDimension] you get the amount of pixels for that dimen.
 * This function returns a proper dp value just like what you wrote in your dimen.xml
 */
public fun Resources.getDimenInDP(id: Int): Int = (getDimension(id) / displayMetrics.density).toInt()

/**
 * Rescales the bitmap
 * @param maxSize The maximum size of the longest side of the image (can be either height or width) in pixels
 * @return scaled bitmap
 */
public suspend fun Bitmap.scale(maxSize: Int): Bitmap = withContext(Dispatchers.Default) {
    val ratio = width.toFloat() / height.toFloat()
    var newWidth = maxSize
    var newHeight = maxSize
    if (ratio > 1) {
        newHeight = (maxSize / ratio).toInt()
    } else {
        newWidth = (maxSize * ratio).toInt()
    }
    Bitmap.createScaledBitmap(this@scale, newWidth, newHeight, true)
}

/**
 * Uses the value of this int as a **resource id** to parse an [android.graphics.Color] object
 */
public fun Int.asColor(context: Context): Int = ContextCompat.getColor(context, this)

/**
 * Uses this int as a **resource id** to get a drawable
 */
public fun Int.asDrawable(context: Context): Drawable? = ContextCompat.getDrawable(context, this)

/**
 * Returns the currently used locale on the device.
 * Returns the first locale the user has listed in the system settings.
 */
public val Resources.currentLocale: Locale
    get() = ConfigurationCompat.getLocales(configuration).get(0)!!

/**
 * Returns an uri to the given [resourceId].
 * **This uri is NOT safe to store outside of app lifecycle!**
 *
 * The uri may change on app update or relaunch.
 */
public fun Context.getResourceUri(resourceId: Int): Uri = Uri.Builder()
    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
    .authority(resources.getResourcePackageName(resourceId))
    .appendPath(resources.getResourceTypeName(resourceId))
    .appendPath(resources.getResourceEntryName(resourceId))
    .build()

/**
 * Returns an URI to [this] raw resource id.
 *
 * **This uri is NOT safe to store outside of app lifecycle!**
 *
 * The uri may change on app update or relaunch.
 */
public fun Int.raw(context: Context): Uri = Uri.Builder()
    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
    .authority(context.applicationContext.packageName)
    .appendPath(toString())
    .build()
