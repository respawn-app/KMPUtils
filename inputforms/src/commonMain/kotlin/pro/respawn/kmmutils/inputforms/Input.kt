package pro.respawn.kmmutils.inputforms

import kotlin.jvm.JvmInline

public sealed interface Input {

    public val value: String

    public data class Invalid(override val value: String, val errors: List<ValidationError>) : Input

    @JvmInline
    public value class Empty(override val value: String = "") : Input

    @JvmInline
    public value class Valid(override val value: String) : Input
}
