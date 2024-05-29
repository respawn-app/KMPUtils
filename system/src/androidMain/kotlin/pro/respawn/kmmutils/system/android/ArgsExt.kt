package pro.respawn.kmmutils.system.android

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.reflect.typeOf

/**
 * Creates an intent to launch a given class. The type must be of an Android system component, such as an Activity.
 */
public inline fun <reified T : Any> intentFor(context: Context): Intent = Intent(context, T::class.java)

/**
 * Obtain a serializable object of type [T] from this [Bundle]
 * @see requireSerializable
 */
@Suppress("DEPRECATION")
public inline fun <reified T : Serializable> Bundle.serializable(
    key: String
): T? = withApiLevel(
    Build.VERSION_CODES.TIRAMISU,
    below = { getSerializable(key) as? T? },
) { getSerializable(key, T::class.java) }

/**
 * Obtain a serializable object of type [T] from this [Intent]'s extras
 * @see requireSerializable
 */
public inline fun <reified T : Serializable> Intent.serializable(key: String): T? = extras?.serializable(key)

/**
 * Obtain a serializable object of type [T] from this [Bundle]
 * @see serializable
 */
public inline fun <reified T : Serializable> Bundle.requireSerializable(
    key: String
): T = requireNotNull(serializable(key)) { "Bundle contained no Serializable of type ${typeOf<T>()}" }

/**
 * Obtain a serializable object of type [T] from this [Intent]'s extras
 * @see serializable
 */
public inline fun <reified T : Serializable> Intent.requireSerializable(
    key: String
): T = requireNotNull(serializable(key)) { "Bundle contained no Serializable of type ${typeOf<T>()}" }

/**
 * Obtain a parcelable object of type [T] from this [Bundle].
 *
 * @see requireParcelable
 */
@Suppress("DEPRECATION")
public inline fun <reified T : Parcelable> Bundle.parcelable(
    key: String
): T? = withApiLevel(
    Build.VERSION_CODES.TIRAMISU,
    below = { getParcelable(key) as? T? }
) { getParcelable(key, T::class.java) }

/**
 * Obtain a parcelable object of type [T] from this [Intent]'s extras
 *
 * @see requireParcelable
 */
public inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = extras?.parcelable(key)

/**
 * Obtain a parcelable object of type [T] from this [Bundle].
 *
 * @see parcelable
 */
public inline fun <reified T : Parcelable> Bundle.requireParcelable(key: String): T = requireNotNull(parcelable(key)) {
    "Bundle contained no Parcelable of type ${typeOf<T>()}"
}

/**
 * Obtain a parcelable object of type [T] from this [Intent]'s extras.
 *
 * @see parcelable
 */
public inline fun <reified T : Parcelable> Intent.requireParcelable(key: String): T = requireNotNull(parcelable(key)) {
    "Bundle contained no Parcelable of type ${typeOf<T>()}"
}
