package pro.respawn.kmmutils.compose.lazy

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PageSize
import androidx.compose.ui.unit.Density

/**
 * Defines a [PageSize] that shows [amount] pages on the screen at all times. The size of pages will be changed
 * to always show [amount] pages.
 */
@OptIn(ExperimentalFoundationApi::class)
public class PagesPerScreen(
    public val amount: Int
) : PageSize {

    override fun Density.calculateMainAxisPageSize(
        availableSpace: Int,
        pageSpacing: Int
    ): Int = (availableSpace - 2 * pageSpacing) / amount
}
