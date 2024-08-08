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
public fun PagerState.offsetForPage(page: Int): Float = (currentPage - page + currentPageOffsetFraction) * -1

/**
 * Applies a scale effect when scrolling through pages.
 *
 * The highest scale change factor possible is determined by [scaleFactor], achieved when the item is fully swiped away.
 */
public fun Modifier.scalePagerEffect(
    state: PagerState,
    index: Int,
    scaleFactor: Float = DefaultPagerScaleFactor
): Modifier = graphicsLayer {
    //  Calculate the absolute offset for the current page from the
    //  scroll position. We use the absolute value which allows us to mirror
    //  any effects for both directions
    lerp(
        start = ScaleFactor(scaleFactor, scaleFactor),
        stop = ScaleFactor(1f, 1f),
        fraction = 1f - state.offsetForPage(index).coerceIn(0f, 1f)
    ).also { scale ->
        scaleX = scale.scaleX
        scaleY = scale.scaleY
    }
}

/**
 * Applies a "spin" (rotate) effect when scrolling through pages.
 *
 * The highest angle possible is determined by [maxRotationDeg], achieved when the item is fully swiped away.
 */
public fun Modifier.spinPagerEffect(
    state: PagerState,
    index: Int,
    maxRotationDeg: Float = MaxRotationDegrees
): Modifier = graphicsLayer {
    rotationZ = lerp(
        start = 0f,
        stop = maxRotationDeg,
        fraction = state.offsetForPage(index).coerceIn(-1f, 1f)
    )
}
