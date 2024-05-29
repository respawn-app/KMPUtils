package pro.respawn.kmmutils.compose

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Remembers a reference to a [Service] [BoundService]'s [Binder] [BoundServiceBinder] in composition.
 * The function will bind to a service while it is in the composition and unbind when it has left the composition.
 * The function will maintain a [ServiceConnection] and return a [State] with a nullable value of service.
 *
 * Binding to the service takes some time and connection breakages can occur,
 * so the return state value may be null at times.
 */
@Composable
@Suppress("ComposableParametersOrdering")
public inline fun <reified BoundService : Service, reified BoundServiceBinder : Binder> rememberBoundLocalService(
    flags: Int = Context.BIND_AUTO_CREATE,
    noinline getService: @DisallowComposableCalls BoundServiceBinder.() -> BoundService,
): State<BoundService?> {
    val context: Context = LocalContext.current
    val boundService = remember(context) { mutableStateOf<BoundService?>(null) }

    val serviceConnection: ServiceConnection = remember(context, getService) {
        object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                boundService.value = (service as BoundServiceBinder).getService()
            }

            override fun onServiceDisconnected(arg0: ComponentName) {
                boundService.value = null
            }
        }
    }
    DisposableEffect(context, serviceConnection, flags) {
        context.bindService(Intent(context, BoundService::class.java), serviceConnection, flags)

        onDispose { context.unbindService(serviceConnection) }
    }
    return boundService
}
