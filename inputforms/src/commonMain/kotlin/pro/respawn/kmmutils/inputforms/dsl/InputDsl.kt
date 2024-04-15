@file:Suppress("TooManyFunctions")

package pro.respawn.kmmutils.inputforms.dsl

import pro.respawn.kmmutils.common.isValid
import pro.respawn.kmmutils.common.takeIfValid
import pro.respawn.kmmutils.inputforms.Form
import pro.respawn.kmmutils.inputforms.Input
import pro.respawn.kmmutils.inputforms.Rule
import pro.respawn.kmmutils.inputforms.default.Rules
import kotlin.jvm.JvmName

/**
 * @return [Input.Valid] if this is a non-empty string, or [Input.Empty] if the string is null or blank
 * Use this when building an input for the first time to specify previous (pre-filled) values
 */
public fun input(value: String? = null): Input = value.input()

/**
 * @return [Input.Empty] with [default] -> [Input.value]
 */
public fun empty(default: String? = null): Input.Empty = Input.Empty(default ?: "")

// string

/**
 * Validates this String using a given [Form]
 * @return an [Input]
 */
public fun String?.validate(form: Form): Input = form(this ?: "")

/**
 * Validates this String using a given [Rule]
 * @return an [Input]
 */
public fun String?.validate(rule: Rule): Input = (this ?: "").let { rule(it).fold(it) }

/**
 * Creates a new [Input] from this string. Whether this will be an [Input.Empty] or [Input.Valid] depends on whether the
 * value is a blank string.
 */
@JvmName("inputString")
public fun String?.input(): Input = takeIfValid()?.let(Input::Valid) ?: Input.Empty("")

/**
 * Create an [Input.Empty] from this string as a default.
 */
public fun String?.emptyInput(): Input.Empty = empty(this)

/**
 * Infix fun syntax shorthand for [validateWith].
 */
public infix fun Form.validates(value: String): Input = validate(value)

/**
 * Validate this string with the given [form].
 */
public infix fun String?.validateWith(form: Form): Input = validate(form)

// input

/**
 * Validate this [Input] using a given [form].
 * @return a new resulting input. Previous errors are discarded.
 */
public fun Input.validate(form: Form): Input = form(value)

/**
 * Validate this [Input] using the given [rule]. Previous errors are preserved.
 */
public fun Input.validate(rule: Rule): Input = when (this) {
    is Input.Invalid -> Input.Invalid(value, errors + rule(value))
    else -> rule(value).fold(value)
}

/**
 * Applies an additional [Rules.Equals] to this input,
 * then appends the result of the validation to other validation errors present, if any.
 * Allows to create an Input that must always match some other one
 * Don't forget to run the validation on **both** the dependent and the original inputs when any of them changes.
 */
public infix fun Input.mustMatch(other: Input): Input = validate(Rules.Equals(other.value, false))

/**
 * Whether this is [Input.Valid]
 */
public val Input.isValid: Boolean get() = this is Input.Valid

/**
 * Whether this is [Input.Invalid]
 */
public val Input.isInvalid: Boolean get() = this is Input.Invalid

/**
 * Whether this is [Input.Empty]
 */
public val Input.isEmpty: Boolean get() = this is Input.Empty

/**
 * Whether this Input is valid or empty
 */
public val Input.isValidOrEmpty: Boolean get() = this is Input.Valid || this is Input.Empty

/**
 * Whether the actual string of this [Input] is blank
 */
public val Input.isEmptyValue: Boolean get() = !value.isValid()

/**
 * Returns null if this [Input] is not valid, otherwise returns [Input.value]
 */
public fun Input.valueIfValid(): String? = takeIf { it.isValid }?.value

/**
 * Returns [Input.value] if it is valid, otherwise null
 */
public fun Input.orNull(): String? = if (this is Input.Valid) value else null

/**
 * Returns [Input.value] if it is valid, otherwise an empty string
 */
public fun Input.orEmpty(): String = if (this is Input.Valid) value else ""
