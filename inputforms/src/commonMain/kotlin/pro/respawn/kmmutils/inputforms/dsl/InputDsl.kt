@file:Suppress("TooManyFunctions")

package pro.respawn.kmmutils.inputforms.dsl

import pro.respawn.kmmutils.common.isValid
import pro.respawn.kmmutils.inputforms.Form
import pro.respawn.kmmutils.inputforms.Input
import pro.respawn.kmmutils.inputforms.Rule
import pro.respawn.kmmutils.inputforms.default.Rules
import kotlin.jvm.JvmName

/**
 * @return [Input.Valid] if this is a non-empty string, or [Input.Empty] if the string is null or blank
 * Use this when building an input for the first time to specify previous (pre-filled) values
 */
public fun input(value: String?): Input = if (value.isValid) Input.Valid(value!!) else Input.Empty(value ?: "")

/**
 * @return [Input.Empty] with [default] -> [Input.value]
 */
public fun empty(default: String? = null): Input.Empty = Input.Empty(default ?: "")

// string

public fun String?.validate(form: Form): Input = form(this ?: "")
public fun String?.validate(rule: Rule): Input = (this ?: "").let { rule(it).fold(it) }

@JvmName("inputString")
public fun String?.input(): Input = input(this)
public fun String?.emptyInput(): Input.Empty = empty(this)
public infix fun Form.validates(value: String): Input = validate(value)
public infix fun String?.validateWith(form: Form): Input = validate(form)

// input

public fun Input.validate(form: Form): Input = form(value)
public fun Input.validate(rule: Rule): Input = when (this) {
    is Input.Invalid -> Input.Invalid(value, errors + rule(value))
    else -> rule(value).fold(value)
}

public infix fun Input.mustMatch(other: Input): Input = validate(Rules.Equals(other.value, false))

public val Input.isValid: Boolean get() = this is Input.Valid

public val Input.isInvalid: Boolean get() = this is Input.Invalid

public val Input.isEmpty: Boolean get() = this is Input.Empty

public val Input.isValidOrEmpty: Boolean get() = this is Input.Valid || this is Input.Empty

public val Input.isEmptyValue: Boolean get() = !value.isValid

public fun Input.valueIfValid(): String? = takeIf { it.isValid }?.value

public fun Input.orNull(): String? = if (this is Input.Valid) value else null

public fun Input.orEmpty(): String = if (this is Input.Valid) value else ""
