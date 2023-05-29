package pro.respawn.kmmutils.inputforms.dsl

import pro.respawn.kmmutils.inputforms.Input
import pro.respawn.kmmutils.inputforms.Rule
import pro.respawn.kmmutils.inputforms.ValidationError
import pro.respawn.kmmutils.inputforms.ValidationStrategy
import kotlin.jvm.JvmName

/**
 * A function that evaluates [this] lambda, and if it returns false, returns the result of [error] wrapped in a sequence.
 * Shorthand for an if statement and a sequence build
 */
@Suppress("ThrowingExceptionsWithoutMessageOrCause") // false-positive
public inline infix fun (() -> Boolean).checks(
    crossinline error: () -> ValidationError
): Sequence<ValidationError> = sequence {
    if (!invoke()) yield(error())
}

/**
 * A function that evaluates [this] lambda, and if it returns false, returns the result of [error] wrapped in a sequence.
 * Shorthand for an if statement and a sequence build
 */
@Suppress("ThrowingExceptionsWithoutMessageOrCause") // false-positive
@OverloadResolutionByLambdaReturnType
@JvmName("checksAll")
public inline infix fun (() -> Boolean).checks(
    crossinline error: () -> Iterable<ValidationError>,
): Sequence<ValidationError> = sequence {
    if (!invoke()) yieldAll(error())
}

/**
 * Runs all validations on a given sequence of rules.
 * @return a list of [ValidationError]s resulted from a validation
 */
internal operator fun Sequence<Rule>.invoke(
    input: String,
    strategy: ValidationStrategy
): List<ValidationError> = when (strategy) {
    is ValidationStrategy.FailFast -> listOfNotNull(firstNotNullOfOrNull { it(input).firstOrNull() })
    is ValidationStrategy.LazyEval -> flatMap { it(input) }.toList()
}

/**
 * @see invoke
 */
internal operator fun Array<out Rule>.invoke(
    input: String,
    strategy: ValidationStrategy,
): List<ValidationError> = asSequence()(input, strategy)

/**
 * Fold [this] list of [ValidationError]s to an [Input] value. Use after running validation on a string.
 */
internal fun Iterable<ValidationError>.fold(value: String): Input = asSequence().fold(value)

/**
 * Transform these [ValidationError]s into an [Input] value based on whether there are any errors
 * If no errors, returns [Input.Valid] or [Input.Empty]
 */
internal fun Sequence<ValidationError>.fold(value: String): Input =
    if (none()) input(value) else Input.Invalid(value, toList())

/**
 * Returns a new [Rule] instantiated lazily for a given [check].
 */
public fun lazyRule(check: (String) -> Sequence<ValidationError>): Lazy<Rule> = lazy { Rule(check) }
