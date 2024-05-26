@file:OptIn(ExperimentalFoundationApi::class)

package pro.respawn.kmmutils.compose.lazy

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.util.lerp

private const val DefaultPagerScaleFactor = 0.85f
private const val MaxRotationDegrees = 30f

/**
 * Calculate absolute offset for current page [page].
 * * For previous page, this will be -1
 * * For current, 0
 * * For next, 1
 * * For one after the next - 2
 *
 * and so on...
 */
@Stable
private fun PagerState.offsetForPage(
    page: Int
) = (currentPage - page + currentPageOffsetFraction) * -1

public fun Modifier.scalePagerEffect(
    state: PagerState,
    index: Int,
    scaleFactor: Float = DefaultPagerScaleFactor
): Modifier = graphicsLayer {
    //  Calculate the absolute offset for the current page from the
    //  scroll position. We use the absolute value which allows us to mirror
    //  any effects for both directions
    val pageOffset = state.offsetForPage(index)

    //  We animate the scaleX + scaleY, between 85% and 100%
    lerp(
        start = ScaleFactor(scaleFactor, scaleFactor),
        stop = ScaleFactor(1f, 1f),
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    ).also { scale ->
        scaleX = scale.scaleX
        scaleY = scale.scaleY
    }
}

public fun Modifier.spinPagerEffect(
    state: PagerState,
    index: Int,
    maxRotationDeg: Float = MaxRotationDegrees
): Modifier = graphicsLayer {
    val pageOffset = state.offsetForPage(index)

    rotationZ = lerp(
        start = 0f,
        stop = maxRotationDeg,
        fraction = pageOffset.coerceIn(-1f, 1f)
    )
}
