package pro.respawn.kmmutils.system.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract

/**
 * Contract for picking ringtones.
 * Returns one of:
 *  * An uri of the picked ringtone,
 *  * a Uri that equals System#DEFAULT_RINGTONE_URI, System#DEFAULT_NOTIFICATION_URI, or System#DEFAULT_ALARM_ALERT_URI
 *  if the default was chosen,
 *  * null if the "Silent" item was picked or if there was an error.
 */
public class PickRingtoneContract : ActivityResultContract<PickRingtoneContract.RingtoneOptions, Uri?>() {

    override fun createIntent(context: Context, input: RingtoneOptions): Intent =
        Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, input.showDefault)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, input.allowSilent)
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, input.ringtoneType)
            input.title?.let { putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, it) }
            input.defaultUri?.let { putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, it) }
            input.existingUri?.let { putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, it) }
        }

    override fun getSynchronousResult(context: Context, input: RingtoneOptions): SynchronousResult<Uri?>? = null

    @Suppress("DEPRECATION")
    override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
        intent?.takeIf { resultCode == Activity.RESULT_OK }?.run {
            data ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, Uri::class.java)
            } else {
                intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            }
        }

    /**
     * Ringtone options
     * @param ringtoneType one of TYPE_RINGTONE, TYPE_NOTIFICATION, TYPE_ALARM, or TYPE_ALL (bitwise or'd together)
     * @param title The title of the ringtone picker. Default value is suitable for most cases
     * @param defaultUri The uri to the sound to play when the user previews "default" sound.
     *   This can be one of System#DEFAULT_RINGTONE_URI,
     *   System#DEFAULT_NOTIFICATION_URI, or System#DEFAULT_ALARM_ALERT_URI
     *   to have the "Default" point to the current sound for the given default sound type.
     *   If you are showing a ringtone picker for some other type of sound, you are free to provide any Uri here.
     * @param existingUri Given to the ringtone picker as a Uri. The Uri of the current ringtone,
     * which will be used to show a checkmark next to the item for this Uri.
     * If showing an item for "Default" (@see EXTRA_RINGTONE_SHOW_DEFAULT),
     * this can also be one of System#DEFAULT_RINGTONE_URI,
     * System#DEFAULT_NOTIFICATION_URI, or System#DEFAULT_ALARM_ALERT_URI to have the "Default" item checked.
     * @param allowSilent Whether to show the "Silent" item in the list.
     * @param showDefault Whether to show the "Default" item in the list.
     * @see RingtoneManager
     */
    public data class RingtoneOptions(
        val ringtoneType: Int,
        val showDefault: Boolean = true,
        val allowSilent: Boolean = false,
        val title: String? = null,
        val defaultUri: Uri? = null,
        val existingUri: Uri? = null,
    )
}
