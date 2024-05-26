package pro.respawn.kmmutils.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Remember the value produced by [calculation].
 * It behaves similarly to [rememberSaveable], but uses [ViewModel] to store the value.
 *
 * You **must** have a [ViewModelStoreOwner] provided via a composition local to use this function
 */
@Composable
@Suppress("ComposableParametersOrdering")
public fun <T> rememberRetainedValue(
    key: String? = null,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    onDispose: (T.() -> Unit)? = null,
    calculation: () -> T,
): T {
    val finalKey = if (!key.isNullOrBlank()) key else currentCompositeKeyHash.toString()
    return viewModel(
        key = finalKey, viewModelStoreOwner = viewModelStoreOwner
    ) { CacheViewModel(calculation(), onDispose) }.value
}

private class CacheViewModel<T>(val value: T, private val dispose: (T.() -> Unit)?) : ViewModel() {

    override fun onCleared() {
        dispose?.invoke(value)
    }
}
