@file:Suppress("unused")

package pro.respawn.kmmutils.compose

import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

/**
 * Plays a medium haptic feedback, similar to force-touch or long-tap effect.
 *
 * The effect can vary between platforms or be unavailable (e.g. desktop, browser).
 *
 * @see HapticFeedbackType.LongPress
 */
public fun HapticFeedback.medium(): Unit = performHapticFeedback(HapticFeedbackType.LongPress)

/**
 * Plays a short haptic feedback, similar to a software keyboard typing vibration.
 *
 * The effect can vary between platforms or be unavailable (e.g. desktop, browser).
 *
 * @see HapticFeedbackType.TextHandleMove
 */
public fun HapticFeedback.short(): Unit = performHapticFeedback(HapticFeedbackType.TextHandleMove)
