package pro.respawn.kmmutils.inputforms

public sealed interface ValidationError {

    public val value: String

    public abstract class Generic(
        override val value: String,
    ) : ValidationError

    public data class Empty(
        override val value: String
    ) : ValidationError

    public data class NotInRange(
        override val value: String,
        public val range: IntRange,
    ) : ValidationError

    public data class TooShort(
        override val value: String,
        public val minLength: Int,
    ) : ValidationError

    public data class TooLong(
        override val value: String,
        public val maxLength: Int,
    ) : ValidationError

    public data class DoesNotMatch(
        override val value: String,
        val pattern: Regex,
    ) : ValidationError

    public data class DoesNotStartWith(
        override val value: String,
        public val prefix: String
    ) : ValidationError

    public data class DoesNotEndWith(
        override val value: String,
        public val postfix: String,
    ) : ValidationError

    public data class DoesNotContain(override val value: String, public val needle: String) : ValidationError

    public data class NotAlphaNumeric(override val value: String) : ValidationError

    public data class ContainsDigits(override val value: String) : ValidationError

    public data class HasNoDigits(override val value: String) : ValidationError

    public data class ContainsLetters(override val value: String) : ValidationError

    public data class HasNoLetters(override val value: String) : ValidationError

    public data class IsNotEqual(override val value: String, val other: String) : ValidationError
}
