package pro.respawn.kmmutils.compose.lazy

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable

public fun LazyGridScope.fullWidthItem(
    content: @Composable LazyGridItemScope.() -> Unit,
): Unit = item(span = { GridItemSpan(maxLineSpan) }, content = content)
