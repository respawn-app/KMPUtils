package pro.respawn.kmmutils.inputforms.default

import pro.respawn.kmmutils.inputforms.Form
import pro.respawn.kmmutils.inputforms.ValidationStrategy
import pro.respawn.kmmutils.inputforms.ValidationStrategy.FailFast

public object Forms {

    public fun Email(
        regex: Regex = Patterns.Email,
        length: IntRange = FieldLengths.Email,
        strategy: ValidationStrategy = FailFast,
    ): Form = Form(
        strategy,
        Rules.NonEmpty,
        Rules.LengthInRange(length),
        Rules.Matches(regex),
    )

    public fun Password(
        regex: Regex = Patterns.DefaultPassword,
        length: IntRange = FieldLengths.Password,
        strategy: ValidationStrategy = FailFast,
    ): Form = Form(
        strategy,
        Rules.NonEmpty,
        Rules.LengthInRange(length),
        Rules.HasDigit,
        Rules.HasLetter,
        Rules.Matches(regex),
    )

    public fun NumericPhoneNumber(
        regex: Regex = Patterns.InternationalNumericPhoneNumber,
        length: IntRange = FieldLengths.NumericPhoneNumber,
        strategy: ValidationStrategy = FailFast,
    ): Form = Form(
        strategy,
        Rules.NonEmpty,
        Rules.LengthInRange(length),
        Rules.HasDigit,
        Rules.StartsWith("+"),
        Rules.NoLetters,
        Rules.Matches(regex),
    )

    public fun DelimitedPhoneNumber(
        regex: Regex = Patterns.InternationalDelimitedPhoneNumber,
        length: IntRange = FieldLengths.NumericPhoneNumber,
        strategy: ValidationStrategy = FailFast,
    ): Form = Form(
        strategy,
        Rules.NonEmpty,
        Rules.LengthInRange(length),
        Rules.HasDigit,
        Rules.StartsWith("+"),
        Rules.NoLetters,
        Rules.Matches(regex),
    )
}
