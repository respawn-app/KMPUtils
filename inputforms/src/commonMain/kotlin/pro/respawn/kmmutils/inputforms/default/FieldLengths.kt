package pro.respawn.kmmutils.inputforms.default

/**
 * Default field lengths for common input types used in applications.
 */
public object FieldLengths {

    /**
     * The smallest possible, most commonly used minimum and maximum url lengths in web browsers.
     * The value of 2048 is mostly used by IE up to 7.0.
     * The urls can be as small as one-character long.
     */
    public val Url: IntRange = 1..2048

    /**
     * A range of values the length of an email can take
     * The [RFC-5322](https://datatracker.ietf.org/doc/html/rfc5322) standard does not specify an exact limit on the maximum length of the email, however, it recommends
     * that the email is no longer than 256 characters as longer emails are not exactly useful to have.
     *
     */
    public val Email: IntRange = 3..256

    /**
     * A Bcrypt hash compliant password lengths with a sensible upper limit of 48 chars
     * A minimum length of a password using BCRYPT algorithm has to be at least 8 characters, as all other password
     * lengths can be cracked in seconds using the hash of the password using moder algorithms.
     */
    public val Password: IntRange = 8..48

    /**
     * An [E.164](https://en.wikipedia.org/wiki/E.164)-compliant numeric (no delimiters) phone number minimum and maximum length.
     * Note that the minimum length can vary and the lowest possible value is specified here.
     * @see DelimitedPhoneNumber
     */
    public val NumericPhoneNumber: IntRange = 3..15

    /**
     * An [E.164](https://en.wikipedia.org/wiki/E.164)-compliant delimited phone number minimum and maximum length.
     * Note that the minimum length can vary and the lowest possible value is specified here.
     * The maximum length is derived from the assumption that no more than 5 delimiters may be used (not in the standard).
     * @see DelimitedPhoneNumber
     */
    public val DelimitedPhoneNumber: IntRange = 3..20
}
