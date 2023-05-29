package pro.respawn.kmmutils.inputforms

import pro.respawn.kmmutils.inputforms.ValidationStrategy.FailFast
import pro.respawn.kmmutils.inputforms.dsl.fold
import pro.respawn.kmmutils.inputforms.dsl.invoke

public class Form(
    public val strategy: ValidationStrategy = FailFast,
    protected vararg val rules: Rule,
) {

    public operator fun invoke(input: String): Input = validate(input)
    public fun validate(input: String): Input = rules(input, strategy).fold(input)

    public companion object
}
