package pro.respawn.kmmutils.compose

import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

private val TextUnitVectorConverter = TwoWayConverter<TextUnit, _>({ AnimationVector1D(it.value) }, { it.value.sp })

/**
 * A vector converter that converts this [TextUnit] value to a [Float] to allow you to animate it.
 */
public val TextUnit.Companion.VectorConverter: TwoWayConverter<TextUnit, AnimationVector1D>
    get() = TextUnitVectorConverter

private val LongVectorConverter = TwoWayConverter<Long, _>({ AnimationVector1D(it.toFloat()) }, { it.value.toLong() })

/**
 * A vector converter that converts this [Long] value to a [Float] to allow you to animate it.
 */
public val Long.Companion.VectorConverter: TwoWayConverter<Long, AnimationVector1D>
    get() = LongVectorConverter
