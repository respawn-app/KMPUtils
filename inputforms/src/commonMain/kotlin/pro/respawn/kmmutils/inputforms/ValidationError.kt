package pro.respawn.kmmutils.inputforms

import kotlin.jvm.JvmInline

/**
 * An error that occurred during validation.
 * Presence of ValidationErrors in the resulting [Input.Invalid] indicates
 * that the value of the input is invalid.
 */
public sealed interface ValidationError {

    /**
     * A value that is being validated
     */
    public val value: String

    /**
     * A generic validation error that can be subclassed. In this case,
     * you need to keep track of your custom errors yourself.
     */
    public abstract class Generic(
        override val value: String,
    ) : ValidationError

    /**
     * The input is blank.
     * @see pro.respawn.kmmutils.inputforms.default.Rules.NonEmpty
     */
    @JvmInline
    public value class Empty(
        override val value: String
    ) : ValidationError

    /**
     * Length of the input was not in [range]
     * @see pro.respawn.kmmutils.inputforms.default.Rules.LengthInRange
     */
    public data class NotInRange(
        override val value: String,
        public val range: IntRange,
    ) : ValidationError

    /**
     * Input was shorter than [minLength]
     * @see pro.respawn.kmmutils.inputforms.default.Rules.LongerThan
     */
    public data class TooShort(
        override val value: String,
        public val minLength: Int,
    ) : ValidationError

    /**
     * Input was longer than [maxLength]
     * @see pro.respawn.kmmutils.inputforms.default.Rules.ShorterThan
     */
    public data class TooLong(
        override val value: String,
        public val maxLength: Int,
    ) : ValidationError

    /**
     * Input does not match the [pattern]
     * @see pro.respawn.kmmutils.inputforms.default.Rules.Matches
     */
    public data class DoesNotMatch(
        override val value: String,
        val pattern: Regex,
    ) : ValidationError

    /**
     * Input does not start with [prefix]
     * @see pro.respawn.kmmutils.inputforms.default.Rules.StartsWith
     */
    public data class DoesNotStartWith(
        override val value: String,
        public val prefix: String
    ) : ValidationError

    /**
     * Input does not end with [suffix]
     * @see pro.respawn.kmmutils.inputforms.default.Rules.EndsWith
     */
    public data class DoesNotEndWith(
        override val value: String,
        public val suffix: String,
    ) : ValidationError

    /**
     * Input does not contain a [needle]
     * @see pro.respawn.kmmutils.inputforms.default.Rules.Contains
     */
    public data class DoesNotContain(
        override val value: String,
        public val needle: String
    ) : ValidationError

    /**
     * Input is not alpha-numeric
     * @see pro.respawn.kmmutils.inputforms.default.Rules.AlphaNumeric
     */
    @JvmInline
    public value class NotAlphaNumeric(
        override val value: String,
    ) : ValidationError

    /**
     * Input has digits
     * @see pro.respawn.kmmutils.inputforms.default.Rules.NoDigits
     */
    public data class ContainsDigits(
        override val value: String,
    ) : ValidationError

    /**
     * Input has no digits
     * @see pro.respawn.kmmutils.inputforms.default.Rules.HasDigit
     */
    public data class HasNoDigits(
        override val value: String,
    ) : ValidationError

    /**
     * Input has letters
     * @see pro.respawn.kmmutils.inputforms.default.Rules.NoLetters
     */
    public data class ContainsLetters(
        override val value: String,
    ) : ValidationError

    /**
     * Input has no letters
     * @see pro.respawn.kmmutils.inputforms.default.Rules.HasLetter
     */
    @JvmInline
    public value class HasNoLetters(
        override val value: String
    ) : ValidationError

    /**
     * Input's value was not equal to [other]
     * @see pro.respawn.kmmutils.inputforms.default.Rules.Equals
     */
    public data class IsNotEqual(
        override val value: String,
        val other: String
    ) : ValidationError

    /**
     * Input's value contained anything except letters.
     * @see pro.respawn.kmmutils.inputforms.default.Rules.LettersOnly
     */
    @JvmInline
    public value class NotLettersOnly(
        override val value: String,
    ) : ValidationError

    /**
     * Input's value contained anything except digits
     * @see pro.respawn.kmmutils.inputforms.default.Rules.DigitsOnly
     */
    @JvmInline
    public value class NotDigitsOnly(
        override val value: String
    ) : ValidationError

    /**
     * Input's value is not an ascii symbol
     * @see pro.respawn.kmmutils.inputforms.default.Rules.AsciiOnly
     */
    @JvmInline
    public value class NotAscii(
        override val value: String,
    ) : ValidationError

    /**
     * Input's value is not lowercase
     * @see pro.respawn.kmmutils.inputforms.default.Rules.LowercaseOnly
     */
    @JvmInline
    public value class NotLowercase(
        override val value: String,
    ) : ValidationError

    /**
     * Input's value is not uppercase
     * @see pro.respawn.kmmutils.inputforms.default.Rules.UppercaseOnly
     */
    @JvmInline
    public value class NotUppercase(
        override val value: String,
    ) : ValidationError

    /**
     * Input's value has whitespace
     * @see pro.respawn.kmmutils.inputforms.default.Rules.NoWhitespace
     */
    @JvmInline
    public value class HasWhitespace(
        override val value: String,
    ) : ValidationError

    /**
     * Input's value has newlines ('\n')
     * @see pro.respawn.kmmutils.inputforms.default.Rules.SingleLine
     */
    @JvmInline
    public value class NotSingleline(
        override val value: String,
    ) : ValidationError

    public companion object
}
