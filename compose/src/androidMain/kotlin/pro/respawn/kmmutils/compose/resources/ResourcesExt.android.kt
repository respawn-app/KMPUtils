package pro.respawn.kmmutils.compose.resources

import android.text.format.DateFormat
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp

@Composable
public fun Int.plural(
    quantity: Int,
    vararg args: Any,
): String = pluralStringResource(this, quantity, formatArgs = args)

@Composable
public fun Int?.plural(quantity: Int, vararg args: Any): String? = this?.plural(quantity, args)

@Composable
public fun Int.vector(): ImageVector = ImageVector.vectorResource(this)

@Composable
public fun Int?.vector(): ImageVector? = this?.vector()

@ExperimentalAnimationGraphicsApi
@Composable
public fun Int.animatedVector(): AnimatedImageVector = AnimatedImageVector.animatedVectorResource(id = this)

@ExperimentalAnimationGraphicsApi
@Composable
public fun Int?.animatedVector(): AnimatedImageVector? = this?.animatedVector()

@Composable
public fun Int.string(vararg args: Any): String = stringResource(id = this, formatArgs = args)

@Composable
public fun Int?.string(vararg args: Any): String? = this?.string(args)

@Composable
public fun Int.painter(): Painter = painterResource(this)

@Composable
public fun Int?.painter(): Painter? = this?.painter()

@Composable
public fun Int.integerRes(): Int = integerResource(this)

@Composable
public fun Int?.integerRes(): Int? = this?.integerRes()

@Composable
public fun Int.integerArrayRes(): IntArray = integerArrayResource(this)

@Composable
public fun Int?.integerArrayRes(): IntArray? = this?.integerArrayRes()

@Composable
public fun Int.booleanRes(): Boolean = booleanResource(this)

@Composable
public fun Int?.booleanRes(): Boolean? = this?.booleanRes()

@Composable
public fun Int.color(): Color = colorResource(this)

@Composable
public fun Int?.color(): Color? = this?.color()

@Composable
public fun Int.dimen(): Dp = dimensionResource(this)

@Composable
public fun Int?.dimen(): Dp? = this?.dimen()

public val isSystem24Hour: Boolean @Composable get() = DateFormat.is24HourFormat(LocalContext.current)

public val displayDensity: Int @Composable get() = LocalConfiguration.current.densityDpi
