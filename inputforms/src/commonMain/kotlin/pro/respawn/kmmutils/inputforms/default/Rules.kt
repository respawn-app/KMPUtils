package pro.respawn.kmmutils.inputforms.default

import pro.respawn.kmmutils.common.isValid
import pro.respawn.kmmutils.common.spans
import pro.respawn.kmmutils.inputforms.Rule
import pro.respawn.kmmutils.inputforms.ValidationError.ContainsDigits
import pro.respawn.kmmutils.inputforms.ValidationError.ContainsLetters
import pro.respawn.kmmutils.inputforms.ValidationError.DoesNotContain
import pro.respawn.kmmutils.inputforms.ValidationError.DoesNotEndWith
import pro.respawn.kmmutils.inputforms.ValidationError.DoesNotMatch
import pro.respawn.kmmutils.inputforms.ValidationError.DoesNotStartWith
import pro.respawn.kmmutils.inputforms.ValidationError.Empty
import pro.respawn.kmmutils.inputforms.ValidationError.HasNoDigits
import pro.respawn.kmmutils.inputforms.ValidationError.HasNoLetters
import pro.respawn.kmmutils.inputforms.ValidationError.IsNotEqual
import pro.respawn.kmmutils.inputforms.ValidationError.NotAlphaNumeric
import pro.respawn.kmmutils.inputforms.ValidationError.NotInRange
import pro.respawn.kmmutils.inputforms.ValidationError.TooLong
import pro.respawn.kmmutils.inputforms.ValidationError.TooShort
import pro.respawn.kmmutils.inputforms.dsl.checks

/**
 * Predefined rules for form validation.
 * @see Rule
 */
public object Rules {

    /**
     * Rule that requires that the string is not blank, does not contain spaces only, and is not equal to "null".
     * "null" text is **not** a valid value.
     */
    public val NonEmpty: Rule = Rule {
        { it.isValid } checks { Empty(it) }
    }

    /**
     * A rule that checks that the input fits the [range].
     * For separate rules, check [LongerThan] and [ShorterThan]
     */
    public fun LengthInRange(range: IntRange): Rule = Rule {
        { it spans range } checks { NotInRange(it, range) }
    }

    /**
     * Rule that requires length to be longer than [minLength]
     * @see LengthInRange
     */
    public fun LongerThan(minLength: Int): Rule = Rule {
        { it.length >= minLength } checks { TooShort(it, minLength) }
    }

    /**
     * Rule that requires length to be shorter than [maxLength]
     * @see LengthInRange
     */
    public fun ShorterThan(maxLength: Int): Rule = Rule {
        { it.length <= maxLength } checks { TooLong(it, maxLength) }
    }

    /**
     * Rule that requires to input to match [pattern]
     */
    public fun Matches(pattern: Regex): Rule = Rule {
        { it.matches(pattern) } checks { DoesNotMatch(it, pattern) }
    }

    /**
     * Rule that requires the input to start with [prefix]
     */
    public fun StartsWith(prefix: String, ignoreCase: Boolean = false): Rule = Rule {
        { it.startsWith(prefix, ignoreCase) } checks { DoesNotStartWith(it, prefix) }
    }

    /**
     * Rule that requires the input to end with [suffix]
     */
    public fun EndsWith(suffix: String, ignoreCase: Boolean = false): Rule = Rule {
        { it.endsWith(suffix, ignoreCase) } checks { DoesNotEndWith(it, suffix) }
    }

    /**
     * Rule that requires the input to contain [needle]
     */
    public fun Contains(needle: String, ignoreCase: Boolean = false): Rule = Rule {
        { it.contains(needle, ignoreCase) } checks { DoesNotContain(it, needle) }
    }

    /**
     * Rule that requires the input to contain only letters or digits.
     * @see Char.isLetterOrDigit
     */
    public val AlphaNumeric: Rule = Rule {
        { it.any { it.isLetterOrDigit() } } checks { NotAlphaNumeric(it) }
    }

    /**
     * Rule that requires the input to have no digits
     * @see Char.isDigit
     */
    public val NoDigits: Rule = Rule {
        { it.none { it.isDigit() } } checks { ContainsDigits(it) }
    }

    /**
     * Rule that requires the input to have at least one digit
     * @see Char.isDigit
     */
    public val HasDigit: Rule = Rule {
        { it.any { it.isDigit() } } checks { HasNoDigits(it) }
    }

    /**
     * Rule that requires the input to not have any letters
     * @see Char.isLetter
     */
    public val NoLetters: Rule = Rule {
        { it.none { it.isLetter() } } checks { ContainsLetters(it) }
    }

    /**
     * Rule that requires the input to have at least one letter
     * @see Char.isLetter
     */
    public val HasLetter: Rule = Rule {
        { it.any { it.isLetter() } } checks { HasNoLetters(it) }
    }

    /**
     * Rule that requires the input to be equal to the [other] string.
     */
    public fun Equals(other: String, ignoreCase: Boolean = false): Rule = Rule {
        { it.equals(other, ignoreCase) } checks { IsNotEqual(it, other) }
    }
}
