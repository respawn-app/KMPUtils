package pro.respawn.kmmutils.compose.modifier

import androidx.compose.ui.Modifier

/**
 * Apply the [ifTrue] block to the modifier chain only if the [condition] is `true`, otherwise apply [ifFalse]
 * (nothing additional by default)
 *
 * @return the continued modifier chain with changes applied, if any.
 */
public inline fun Modifier.thenIf(
    condition: Boolean,
    ifFalse: Modifier.() -> Modifier = { this },
    ifTrue: Modifier.() -> Modifier
): Modifier = then(Modifier.let { if (condition) it.ifTrue() else it.ifFalse() })

/**
 * Apply the  [block] to the modifier chain only if the [value] is not null, otherwise apply [ifNull]
 * (nothing additional by default)
 *
 * @return the continued modifier chain with changes applied, if any.
 */
public inline fun <T> Modifier.thenIfNotNull(
    value: T,
    ifNull: Modifier.() -> Modifier = { this },
    block: Modifier.(T & Any) -> Modifier
): Modifier = then(Modifier.let { if (value != null) it.block(value) else it.ifNull() })
