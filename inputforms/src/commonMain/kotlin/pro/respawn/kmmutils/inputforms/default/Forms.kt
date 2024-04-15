package pro.respawn.kmmutils.inputforms.default

import pro.respawn.kmmutils.inputforms.Form
import pro.respawn.kmmutils.inputforms.ValidationStrategy
import pro.respawn.kmmutils.inputforms.ValidationStrategy.FailFast

/**
 * An object that contains predefined [Form]s.
 */
public data object Forms {

    /**
     * A form for validating emails
     * @see Patterns.Email
     * @see FieldLengths.Email
     */
    public fun Email(
        strategy: ValidationStrategy = FailFast,
        pattern: Regex = Patterns.Email,
        length: IntRange = FieldLengths.Email,
    ): Form = Form(
        strategy,
        Rules.NonEmpty,
        Rules.LengthInRange(length),
        Rules.NoWhitespace,
        Rules.Contains("@"),
        Rules.Matches(pattern),
    )

    /**
     * A form for validating passwords using somewhat opinionated defaults.
     * How passwords are validated by default is described in [Patterns.Password]
     * @see FieldLengths.Password
     */
    public fun Password(
        strategy: ValidationStrategy = FailFast,
        pattern: Regex = Patterns.Password,
        length: IntRange = FieldLengths.Password,
    ): Form = Form(
        strategy,
        Rules.NonEmpty,
        Rules.LengthInRange(length),
        Rules.HasDigit,
        Rules.HasLetter,
        Rules.Matches(pattern),
    )

    /**
     * A form for validating international numeric (not delimited) phone numbers.
     * @see Patterns.InternationalNumericPhoneNumber
     * @see FieldLengths.NumericPhoneNumber
     */
    public fun NumericPhoneNumber(
        strategy: ValidationStrategy = FailFast,
        pattern: Regex = Patterns.InternationalNumericPhoneNumber,
        length: IntRange = FieldLengths.NumericPhoneNumber,
    ): Form = Form(
        strategy,
        Rules.NonEmpty,
        Rules.LengthInRange(length),
        Rules.StartsWith("+"),
        Rules.HasDigit,
        Rules.NoLetters,
        Rules.Matches(pattern),
    )

    /**
     * A form for validating international phone numbers that allows delimiters
     * For a sibling, see [NumericPhoneNumber]
     * @see NumericPhoneNumber
     * @see Patterns.InternationalDelimitedPhoneNumber
     * @see FieldLengths.DelimitedPhoneNumber
     */
    public fun DelimitedPhoneNumber(
        strategy: ValidationStrategy = FailFast,
        pattern: Regex = Patterns.InternationalDelimitedPhoneNumber,
        length: IntRange = FieldLengths.NumericPhoneNumber,
    ): Form = Form(
        strategy,
        Rules.NonEmpty,
        Rules.LengthInRange(length),
        Rules.HasDigit,
        Rules.StartsWith("+"),
        Rules.NoLetters,
        Rules.Matches(pattern),
    )

    /**
     * Arbitrary code with a length of [length] digits.
     */
    public fun NumericCode(
        length: Int,
        strategy: ValidationStrategy = FailFast,
    ): Form = Form(
        strategy,
        Rules.NonEmpty,
        Rules.DigitsOnly,
        Rules.LengthExact(length),
    )

    /**
     * Web-based url, starting with either http, https, www and containing a domain part and an optional path and query
     * parameters
     */
    public fun WebUrl(
        strategy: ValidationStrategy = FailFast,
        pattern: Regex = Patterns.UrlPattern,
    ): Form = Form(
        strategy,
        Rules.NonEmpty,
        Rules.NoWhitespace,
        Rules.SingleLine,
        Rules.Matches(pattern),
    )
}
