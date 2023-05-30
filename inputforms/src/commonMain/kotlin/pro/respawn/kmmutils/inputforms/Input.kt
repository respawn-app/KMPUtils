package pro.respawn.kmmutils.inputforms

import kotlin.jvm.JvmInline

/**
 * A user input. An input can have 3 states:
 *
 * - Invalid: contains validation errors
 * - Valid: an input that has been validated and is considered valid
 * - Empty: an input that has either a default value, an empty value, or has never been validated.
 *     How you want to handle [Empty] values depends on the business logic of your app.
 */
public sealed interface Input {

    /**
     * A current string value that the input holds
     */
    public val value: String

    /**
     * A value of Input that signifies an invalid input
     * @param errors current validation errors that were produced during input validation
     */
    public data class Invalid internal constructor(
        override val value: String,
        val errors: List<ValidationError>
    ) : Input

    /**
     * An input that has either a default value, an empty value, or has never been validated.
     * How you want to handle [Empty] values depends on the business logic of your app.
     */
    @JvmInline
    public value class Empty internal constructor(
        override val value: String = "",
    ) : Input

    /**
     * A valid input.
     */
    @JvmInline
    public value class Valid internal constructor(
        override val value: String
    ) : Input

    /**
     * Copy the input. Type and errors are preserved
     */
    public fun copy(value: String = this.value): Input = when (this) {
        is Empty -> Empty(value)
        is Invalid -> copy(value)
        is Valid -> Valid(value)
    }
}
