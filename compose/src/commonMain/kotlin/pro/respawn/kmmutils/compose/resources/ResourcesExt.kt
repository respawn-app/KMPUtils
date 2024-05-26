package pro.respawn.kmmutils.compose.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringArrayResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
public fun DrawableResource.vector(): ImageVector = vectorResource(this)

@Composable
public fun DrawableResource.painter(): Painter = painterResource(this)

@Composable
public fun DrawableResource.image(): ImageBitmap = imageResource(this)

@Composable
public fun StringArrayResource.strings(): List<String> = stringArrayResource(this)

@Composable
public fun StringResource.string(vararg args: Any): String = stringResource(this, formatArgs = args)

@Composable
public fun PluralStringResource.plural(
    quantity: Int,
    vararg args: Any
): String = pluralStringResource(this, quantity, formatArgs = args)
