package pro.respawn.kmmutils.inputforms.dsl

import pro.respawn.kmmutils.inputforms.Input
import pro.respawn.kmmutils.inputforms.Rule
import pro.respawn.kmmutils.inputforms.ValidationError
import pro.respawn.kmmutils.inputforms.ValidationStrategy
import kotlin.jvm.JvmName

public inline infix fun (() -> Boolean).checks(
    crossinline error: () -> ValidationError
): Sequence<ValidationError> = sequence {
    if (invoke()) yield(error())
}

@OverloadResolutionByLambdaReturnType
@JvmName("checksAll")
public inline infix fun (() -> Boolean).checks(
    crossinline error: () -> Iterable<ValidationError>,
): Sequence<ValidationError> = sequence {
    if (invoke()) yieldAll(error())
}

public operator fun Sequence<Rule>.invoke(
    input: String,
    strategy: ValidationStrategy
): List<ValidationError> = when (strategy) {
    is ValidationStrategy.FailFast -> listOfNotNull(firstNotNullOfOrNull { it(input).firstOrNull() })
    is ValidationStrategy.LazyEval -> flatMap { it(input) }.toList()
}

public operator fun Array<out Rule>.invoke(
    input: String,
    strategy: ValidationStrategy,
): List<ValidationError> = asSequence()(input, strategy)

/**
 * Fold [this] list of [ValidationError]s to an [Input] value. Use after running validation on a string.
 */
public fun Iterable<ValidationError>.fold(value: String): Input = asSequence().fold(value)

public fun Sequence<ValidationError>.fold(value: String): Input =
    if (none()) input(value) else Input.Invalid(value, toList())

/**
 * Returns a new [Rule] instantiated lazily for a given [check].
 */
public fun lazyRule(check: (String) -> Sequence<ValidationError>): Lazy<Rule> = lazy { Rule(check) }
