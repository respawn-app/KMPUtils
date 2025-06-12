package pro.respawn.kmmutils.inputforms.default

import pro.respawn.kmmutils.common.isAscii
import pro.respawn.kmmutils.common.isValid
import pro.respawn.kmmutils.common.spans
import pro.respawn.kmmutils.inputforms.Rule
import pro.respawn.kmmutils.inputforms.ValidationError
import pro.respawn.kmmutils.inputforms.ValidationError.ContainsDigits
import pro.respawn.kmmutils.inputforms.ValidationError.ContainsLetters
import pro.respawn.kmmutils.inputforms.ValidationError.DoesNotContain
import pro.respawn.kmmutils.inputforms.ValidationError.DoesNotEndWith
import pro.respawn.kmmutils.inputforms.ValidationError.DoesNotMatch
import pro.respawn.kmmutils.inputforms.ValidationError.DoesNotStartWith
import pro.respawn.kmmutils.inputforms.ValidationError.Empty
import pro.respawn.kmmutils.inputforms.ValidationError.HasNoDigits
import pro.respawn.kmmutils.inputforms.ValidationError.HasNoLetters
import pro.respawn.kmmutils.inputforms.ValidationError.HasWhitespace
import pro.respawn.kmmutils.inputforms.ValidationError.IsNotEqual
import pro.respawn.kmmutils.inputforms.ValidationError.LengthIsNotExactly
import pro.respawn.kmmutils.inputforms.ValidationError.NotAlphaNumeric
import pro.respawn.kmmutils.inputforms.ValidationError.NotAscii
import pro.respawn.kmmutils.inputforms.ValidationError.NotDigitsOnly
import pro.respawn.kmmutils.inputforms.ValidationError.NotInRange
import pro.respawn.kmmutils.inputforms.ValidationError.NotLettersOnly
import pro.respawn.kmmutils.inputforms.ValidationError.NotLowercase
import pro.respawn.kmmutils.inputforms.ValidationError.NotSingleline
import pro.respawn.kmmutils.inputforms.ValidationError.NotUppercase
import pro.respawn.kmmutils.inputforms.ValidationError.TooLong
import pro.respawn.kmmutils.inputforms.ValidationError.TooShort
import pro.respawn.kmmutils.inputforms.default.Rules.LengthInRange
import pro.respawn.kmmutils.inputforms.default.Rules.LongerThan
import pro.respawn.kmmutils.inputforms.default.Rules.ShorterThan
import pro.respawn.kmmutils.inputforms.dsl.checks

/**
 * Predefined rules for form validation.
 * @see Rule
 */
public data object Rules {

    /**
     * A rule that requires that the string is not blank, does not contain spaces only, and is not equal to "null".
     * "null" text is **not** a valid value.
     *
     * @see String.isValid
     */
    public val NonEmpty: Rule = Rule {
        {
            it.isValid()
        } checks { Empty(it) }
    }

    /**
     * A rule that checks that the input fits the [range]
     *
     * For separate rules, check [LongerThan] and [ShorterThan]
     */
    public fun LengthInRange(range: IntRange): Rule = Rule {
        {
            it spans range
        } checks { NotInRange(it, range) }
    }

    /**
     * A rule that requires length to be longer than [minLength]
     *
     * @see LengthInRange
     */
    public fun LongerThan(minLength: Int): Rule = Rule {
        {
            it.length >= minLength
        } checks { TooShort(it, minLength) }
    }

    /**
     * A rule that requires length to be shorter than [maxLength]
     *
     * @see LengthInRange
     */
    public fun ShorterThan(maxLength: Int): Rule = Rule {
        {
            it.length <= maxLength
        } checks { TooLong(it, maxLength) }
    }

    /**
     * A rule that requires to input to match [pattern]
     *
     * There are predefined patterns: [Patterns]
     */
    public fun Matches(pattern: Regex): Rule = Rule {
        {
            it.matches(pattern)
        } checks { DoesNotMatch(it, pattern) }
    }

    /**
     * A rule that requires the input to start with [prefix]
     */
    public fun StartsWith(prefix: String, ignoreCase: Boolean = false): Rule = Rule {
        {
            it.startsWith(prefix, ignoreCase)
        } checks { DoesNotStartWith(it, prefix) }
    }

    /**
     * A rule that requires the input to end with [suffix]
     */
    public fun EndsWith(suffix: String, ignoreCase: Boolean = false): Rule = Rule {
        {
            it.endsWith(suffix, ignoreCase)
        } checks { DoesNotEndWith(it, suffix) }
    }

    /**
     * A rule that requires the input to contain [needle]
     */
    public fun Contains(needle: String, ignoreCase: Boolean = false): Rule = Rule {
        {
            it.contains(needle, ignoreCase)
        } checks { DoesNotContain(it, needle) }
    }

    /**
     * A rule that requires the input to contain only letters or digits.
     *
     * @see Char.isLetterOrDigit
     */
    public val AlphaNumeric: Rule = Rule { input ->
        {
            input.all { it.isLetterOrDigit() }
        } checks { NotAlphaNumeric(input) }
    }

    /**
     * A rule that requires the input to have no digits
     *
     * @see Char.isDigit
     */
    public val NoDigits: Rule = Rule {
        {
            it.none { it.isDigit() }
        } checks { ContainsDigits(it) }
    }

    /**
     * A rule that requires the input to have at least one digit
     * @see Char.isDigit
     */
    public val HasDigit: Rule = Rule {
        {
            it.any { it.isDigit() }
        } checks { HasNoDigits(it) }
    }

    /**
     * A rule that requires the input to not have any letters
     * @see Char.isLetter
     */
    public val NoLetters: Rule = Rule {
        {
            it.none { it.isLetter() }
        } checks { ContainsLetters(it) }
    }

    /**
     * A rule that requires the input to have at least one letter
     * @see Char.isLetter
     */
    public val HasLetter: Rule = Rule {
        {
            it.any { it.isLetter() }
        } checks { HasNoLetters(it) }
    }

    /**
     * A rule that requires the input to be equal to the [other] string.
     */
    public fun Equals(other: String, ignoreCase: Boolean = false): Rule = Rule {
        {
            it.equals(other, ignoreCase)
        } checks { IsNotEqual(it, other) }
    }

    /**
     * A rule that requires the input to have letters only
     *
     * @see Char.isLetter
     */
    public val LettersOnly: Rule = Rule {
        {
            it.all { it.isLetter() }
        } checks { NotLettersOnly(it) }
    }

    /**
     * A rule that requires the input to have digits only
     *
     * @see Char.isDigit
     */
    public val DigitsOnly: Rule = Rule {
        {
            it.all { it.isDigit() }
        } checks { NotDigitsOnly(it) }
    }

    /**
     * A rule that requires the input to have digits only
     *
     * @see Char.isDigit
     */
    public val AsciiOnly: Rule = Rule {
        {
            it.isAscii
        } checks { NotAscii(it) }
    }

    /**
     * A rule that requires the input to be lowercase
     *
     * @see Char.isLowerCase
     */
    public val LowercaseOnly: Rule = Rule {
        {
            it.all { it.isLowerCase() }
        } checks { NotLowercase(it) }
    }

    /**
     * A rule that requires the input to be upper case
     *
     * @see Char.isUpperCase
     */
    public val UppercaseOnly: Rule = Rule {
        {
            it.all { it.isUpperCase() }
        } checks { NotUppercase(it) }
    }

    /**
     * A rule that requires the input to have no whitespace characters
     *
     * @see Char.isWhitespace
     */
    public val NoWhitespace: Rule = Rule {
        {
            it.none { it.isWhitespace() }
        } checks { HasWhitespace(it) }
    }

    /**
     * A rule that requires the input to have no newline characters ('\n' or '\r').
     */
    public val SingleLine: Rule = Rule {
        {
            it.none { it == '\n' || it == '\r' }
        } checks { NotSingleline(it) }
    }

    /**
     * Input must contain at least one uppercase unicode letter
     */
    public val HasUppercaseLetter: Rule = Rule {
        {
            it.any { it.isLetter() && it.isUpperCase() }
        } checks { ValidationError.NoUppercaseLetters(it) }
    }

    /**
     * Length must be exactly [length] characters
     */
    public fun LengthExact(length: Int): Rule = Rule {
        {
            it.length == length
        } checks { LengthIsNotExactly(it, length) }
    }
}
