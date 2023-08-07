package pro.respawn.kmmutils.inputforms

/**
 * Specifies how to validate the input to a form.
 * @see FailFast
 * @see LazyEval
 */
public sealed interface ValidationStrategy {

    /**
     * FailFast strategy goes through all [Rule]s of a [Form] in order, and stops validation as soon as
     * a single error is encountered, and populates the resulting [Input.Invalid] with that **single** error.
     * So the list of errors will always contain just one element.
     */
    public data object FailFast : ValidationStrategy

    /**
     * LazyEval iterates through all [Rule]s of a [Form] in order until all rules are exhausted, and then returns
     * the list of errors in the resulting [Input.Invalid]. When using this strategy,
     * the list of inputs may contain more than one error.
     */
    public data object LazyEval : ValidationStrategy

    public companion object
}
