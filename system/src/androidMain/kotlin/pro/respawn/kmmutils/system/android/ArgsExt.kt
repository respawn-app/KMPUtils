package pro.respawn.kmmutils.system.android

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

public inline fun <reified T : Any> intentFor(context: Context): Intent = Intent(context, T::class.java)

@Suppress("DEPRECATION")
public inline fun <reified T : Serializable> Bundle.serializable(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getSerializable(key, T::class.java)
    else getSerializable(key) as? T?

public inline fun <reified T : Serializable> Intent.serializable(key: String): T? = extras?.serializable(key)

public inline fun <reified T : Serializable> Bundle.requireSerializable(key: String): T =
    requireNotNull(serializable(key))

public inline fun <reified T : Serializable> Intent.requireSerializable(key: String): T =
    requireNotNull(serializable(key))

@Suppress("DEPRECATION")
public inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getParcelable(key, T::class.java)
    else getParcelable(key) as? T?

public inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = extras?.parcelable(key)

public inline fun <reified T : Parcelable> Bundle.requireParcelable(key: String): T = requireNotNull(parcelable(key))

public inline fun <reified T : Parcelable> Intent.requireParcelable(key: String): T = requireNotNull(parcelable(key))
