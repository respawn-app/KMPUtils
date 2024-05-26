package pro.respawn.kmmutils.compose.lazy

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PageSize
import androidx.compose.ui.unit.Density

@OptIn(ExperimentalFoundationApi::class)
public class PagesPerScreen(
    public val amount: Int
) : PageSize {

    override fun Density.calculateMainAxisPageSize(
        availableSpace: Int,
        pageSpacing: Int
    ): Int = (availableSpace - 2 * pageSpacing) / amount
}
