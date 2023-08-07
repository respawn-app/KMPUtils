package pro.respawn.kmmutils.inputforms.default

/**
 * Common regular expressions used for form validation
 */
@Suppress("MaxLineLength")
public data object Patterns {

    /**
     * An international phone number regex, that does not allow delimiters.
     * - Only numbers are allowed
     * - the "+" sign at the start is required
     * The first group in this regex will be the country code.
     * @see InternationalDelimitedPhoneNumber
     */
    public val InternationalNumericPhoneNumber: Regex by lazy {
        """\+(9[976]\d|8[987530]\d|6[987]\d|5[90]\d|42\d|3[875]\d|2[98654321]\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\d{1,14}${'$'}""".toRegex()
    }

    /**
     * An international phone number regex with delimiters allowed.
     * - Any delimiters including spaces are allowed (\W char in regex). Strip them from your final number.
     * - Otherwise equal to [InternationalNumericPhoneNumber].
     * @See InternationalNumericPhoneNumber
     */
    public val InternationalDelimitedPhoneNumber: Regex by lazy {
        """^\+((?:9[679]|8[035789]|6[789]|5[90]|42|3[578]|2[1-689])|9[0-58]|8[1246]|6[0-6]|5[1-8]|4[013-9]|3[0-469]|2[70]|7|1)(?:\W*\d){0,13}\d$""".toRegex()
    }

    /**
     * An [RFC-5322](https://datatracker.ietf.org/doc/html/rfc5322)-compliant email regex. Most commonly used among web services.
     * Main features:
     * - Requires a domain part per 5322 standard.
     * - Does not limit the length of the input. Use other validations to limit the length according to your needs.
     */
    public val Email: Regex by lazy {
        """(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])""".toRegex()
    }

    /**
     * A password-type regular expression with the following properties:
     * These password requirements are commonly used, but you are encouraged to use your own regex if needed
     * - At least one latin Uppercase letter
     * - At least one latin lowercase letter
     * - At least one digit
     * - Optional special characters (not required)
     * - Minimum length at least 8 characters
     * - Maximum can be any. Use other rules to specify max length
     */
    public val Password: Regex by lazy {
        """^(?=.*\p{Upper})(?=.*\p{Lower})(?=.*\d)[\p{Upper}\p{Lower}\d\p{Punct}]{8,}$""".toRegex()
    }
}
