@file:Suppress("unused")

package pro.respawn.kmmutils.compose

import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

public fun HapticFeedback.medium(): Unit = performHapticFeedback(HapticFeedbackType.LongPress)

public fun HapticFeedback.short(): Unit = performHapticFeedback(HapticFeedbackType.TextHandleMove)
