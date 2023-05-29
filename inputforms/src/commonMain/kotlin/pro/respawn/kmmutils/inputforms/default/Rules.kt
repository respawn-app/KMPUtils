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

public object Rules {

    public val NonEmpty: Rule = Rule {
        { it.isValid } checks { Empty(it) }
    }

    public fun LengthInRange(range: IntRange): Rule = Rule {
        { it spans range } checks { NotInRange(it, range) }
    }

    public fun LongerThan(minLength: Int): Rule = Rule {
        { it.length >= minLength } checks { TooShort(it, minLength) }
    }

    public fun ShorterThan(maxLength: Int): Rule = Rule {
        { it.length <= maxLength } checks { TooLong(it, maxLength) }
    }

    public fun Matches(pattern: Regex): Rule = Rule {
        { it.matches(pattern) } checks { DoesNotMatch(it, pattern) }
    }

    public fun StartsWith(prefix: String, ignoreCase: Boolean = false): Rule = Rule {
        { it.startsWith(prefix, ignoreCase) } checks { DoesNotStartWith(it, prefix) }
    }

    public fun EndsWith(suffix: String, ignoreCase: Boolean = false): Rule = Rule {
        { it.endsWith(suffix, ignoreCase) } checks { DoesNotEndWith(it, suffix) }
    }

    public fun Contains(needle: String, ignoreCase: Boolean = false): Rule = Rule {
        { it.contains(needle, ignoreCase) } checks { DoesNotContain(it, needle) }
    }

    public val AlphaNumeric: Rule = Rule {
        { it.any { it.isLetterOrDigit() } } checks { NotAlphaNumeric(it) }
    }

    public val NoDigits: Rule = Rule {
        { it.none { it.isDigit() } } checks { ContainsDigits(it) }
    }

    public val HasDigit: Rule = Rule {
        { it.any { it.isDigit() } } checks { HasNoDigits(it) }
    }

    public val NoLetters: Rule = Rule {
        { it.none { it.isLetterOrDigit() } } checks { ContainsLetters(it) }
    }

    public val HasLetter: Rule = Rule {
        { it.any { it.isLetter() } } checks { HasNoLetters(it) }
    }

    public fun Equals(other: String, ignoreCase: Boolean = false): Rule = Rule {
        { it.equals(other, ignoreCase) } checks { IsNotEqual(it, other) }
    }
}
