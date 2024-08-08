package pro.respawn.kmmutils.compose.lazy

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerState

/**
 * Same behavior as [LazyListState.firstVisibleItemIndex].
 */
public inline val PagerState.firstVisiblePage: Int
    get() = when {
        currentPageOffsetFraction >= 0 -> currentPage
        else -> currentPage - 1
    }.coerceIn(0..pageCount)

/**
 * Same behavior as [LazyListState.firstVisibleItemScrollOffset].
 */
public inline val PagerState.firstVisiblePageOffsetFraction: Float
    get() = getOffsetDistanceInPages(firstVisiblePage.coerceIn(0..pageCount))
