package pro.respawn.kmmutils.compose.modifier

import androidx.compose.ui.Modifier

public inline fun Modifier.thenIf(
    condition: Boolean,
    ifFalse: Modifier.() -> Modifier = { this },
    ifTrue: Modifier.() -> Modifier
): Modifier = then(Modifier.let { if (condition) it.ifTrue() else it.ifFalse() })
