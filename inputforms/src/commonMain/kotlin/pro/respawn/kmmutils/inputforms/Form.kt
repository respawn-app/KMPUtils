package pro.respawn.kmmutils.inputforms

import pro.respawn.kmmutils.inputforms.dsl.fold
import pro.respawn.kmmutils.inputforms.dsl.invoke

/**
 * Form is a combination of a [ValidationStrategy] and a list of rules to use.
 * Use [invoke] to run a validation and produce an [Input].
 * @param strategy A [ValidationStrategy] to use
 * @param rules A list of rules to use when validating. *Other of rules matters!*
 */
public open class Form(
    public val strategy: ValidationStrategy,
    protected vararg val rules: Rule,
) {

    /**
     * Shorthand for [validate].
     */
    public operator fun invoke(input: String): Input = validate(input)

    /**
     * Run a validation using [rules].
     */
    public open fun validate(input: String): Input = rules(input, strategy).fold(input)

    public companion object
}
