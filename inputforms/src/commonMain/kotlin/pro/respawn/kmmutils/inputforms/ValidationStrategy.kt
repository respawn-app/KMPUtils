package pro.respawn.kmmutils.inputforms

public sealed interface ValidationStrategy {
    public object FailFast : ValidationStrategy
    public object LazyEval : ValidationStrategy
}
