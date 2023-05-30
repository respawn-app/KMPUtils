package pro.respawn.kmmutils.inputforms

/**
 * A rule is used by a [Form] and defines a single validation of that form.
 * **If the validation passed, return an empty sequence from [invoke]**
 */
public fun interface Rule {

    /**
     * A rule, when invoked, returns a sequence of errors that were found.
     * Usually that sequence contains just one error.
     * **If the validation passed, return an empty sequence**
     */
    public operator fun invoke(value: String): Sequence<ValidationError>

    public companion object
}
