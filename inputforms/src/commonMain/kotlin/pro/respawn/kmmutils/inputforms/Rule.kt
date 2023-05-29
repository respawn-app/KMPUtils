package pro.respawn.kmmutils.inputforms

public fun interface Rule {

    public operator fun invoke(value: String): Sequence<ValidationError>
}
