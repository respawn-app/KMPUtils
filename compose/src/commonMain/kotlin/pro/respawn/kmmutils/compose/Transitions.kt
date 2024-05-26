package pro.respawn.kmmutils.compose

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

private val DefaultSpec = spring(
    stiffness = Spring.StiffnessMediumLow,
    visibilityThreshold = IntOffset.VisibilityThreshold
)

/**
 * Positive [itemHeightOffsetFraction] means the item is moving **up**.
 */
@Stable
public fun slideInVertically(
    itemHeightOffsetFraction: Float,
    spec: FiniteAnimationSpec<IntOffset> = DefaultSpec
): EnterTransition = slideInVertically(spec) { (it * itemHeightOffsetFraction).roundToInt() }

/**
 * Positive [itemHeightOffsetFraction] means the item is moving **down**.
 */
@Stable
public fun slideOutVertically(
    itemHeightOffsetFraction: Float,
    spec: FiniteAnimationSpec<IntOffset> = DefaultSpec
): ExitTransition = slideOutVertically(spec) { (it * itemHeightOffsetFraction).roundToInt() }

@Stable
public fun slideToBottom(
    spec: FiniteAnimationSpec<IntOffset> = DefaultSpec
): ExitTransition = slideOutVertically(1f, spec)

@Stable
public fun slideToTop(
    spec: FiniteAnimationSpec<IntOffset> = DefaultSpec
): ExitTransition = slideOutVertically(-1f, spec)

@Stable
public fun slideFromBottom(
    spec: FiniteAnimationSpec<IntOffset> = DefaultSpec
): EnterTransition = slideInVertically(1f, spec)

@Stable
public fun slideFromTop(
    spec: FiniteAnimationSpec<IntOffset> = DefaultSpec
): EnterTransition = slideInVertically(-1f, spec)

/**
 *  A positive value means sliding from right to left, whereas a negative value would slide the content from left to right.
 */
@Stable
public fun slideInHorizontally(
    widthOffsetFraction: Float,
    spec: FiniteAnimationSpec<IntOffset> = DefaultSpec
): EnterTransition = slideInHorizontally(spec) { (it * widthOffsetFraction).roundToInt() }

/**
 * A positive value means sliding to the right, whereas a negative value would slide the content towards the left.
 */
@Stable
public fun slideOutHorizontally(
    widthOffsetFraction: Float,
    spec: FiniteAnimationSpec<IntOffset> = DefaultSpec
): ExitTransition = slideOutHorizontally(spec) { (it * widthOffsetFraction).roundToInt() }

@Stable
public fun slideFromLeft(
    spec: FiniteAnimationSpec<IntOffset> = DefaultSpec
): EnterTransition = slideInHorizontally(-1f, spec)

@Stable
public fun slideFromRight(
    spec: FiniteAnimationSpec<IntOffset> = DefaultSpec
): EnterTransition = slideInHorizontally(1f, spec)

@Stable
public fun slideToLeft(
    spec: FiniteAnimationSpec<IntOffset> = DefaultSpec
): ExitTransition = slideOutHorizontally(-1f, spec)

@Stable
public fun slideToRight(
    spec: FiniteAnimationSpec<IntOffset> = DefaultSpec
): EnterTransition = slideInHorizontally(1f, spec)
