# ApiResult

Browse
code: [ApiResult](https://github.com/respawn-app/kmmutils/tree/master/apiresult/src/commonMain/kotlin/pro/respawn/kmmutils/apiresult)

ApiResult is a class that wraps the result of a computation.
Similar to monads, it has 2 main and 1 additional state:

* Success - contains a value returned by a computation
* Error - wraps an exception caught during a computation
* Loading - intermediate and optional state for async operations

## Features

* ApiResult is **extremely lightweight**. It is lighter than kotlin.Result.
  All instances of it are `value class`es, all operations are `inline`, which means literally 0 overhead.
* ApiResult offers dozens of operators to cover most of possible use cases to turn your
  code from imperative and procedural to declarative and functional, which is way more readable and extensible.
* ApiResult defines a contract that you can use in your code. No one will be able to obtain a result of a computation
  without being forced to handle errors at compilation time.

## Usage

Example usages cover three main cases:

* Wrapping a result of a computation
* Wrapping a result of an async computation with multiple coroutines
* Turning a computation into a flow

```kotlin
// wrap a result of a computation
suspend fun getSubscriptions(userId: String): ApiResult<List<SubscriptionResponse>> = ApiResult {
    api.getSubscriptions(userId)
}

// emits: Loading -> User / Error
fun getSubscriptionsAsync(userId: String): Flow<Apiresult<List<SubscriptionResponse>>> = ApiResult.flow {
    api.getSubscriptions(id)
}

// SuspendResult will wait for the result of nested coroutines and propagate exceptions thrown in them
suspend fun getVerifiedSubs(userId: String) = SuspendResult { // this: CoroutineScope
    val subs = api.getSubscriptions(userId)

    launch {
        api.verifySubscriptions(subs)
    }
    launch {
        storage.saveSubsscriptions(subs)
    }

    user
}
```

After you create your ApiResult, apply a variety of transformations on it:

```kotlin
val state: SubscriptionState = repo.getSubscriptions(userId)
    .mapValues(::SubscriptionModel) // map list items
    .map { subs -> subs.filter { it.isPurchased } } // map success value to filtered list
    .then { validateSubscriptions(it) } // execute a computation and continue with its result, propagating errors
    .onSuccess { updateGracePeriod(it) } // executed  on success
    .fold(
        onSuccess = { SubscriptionState.Subscribed(it) },
        onError = { SubscriptionState.Error(it) },
    ) // unwrap the result to another value
```

## Operators

This is not a comprehensive list of operators as new ones may be added in the future.
Check out source code for a full list.

### Create:

* `ApiResult { computation() } ` - wrap the result of a computation
* `ApiResult.flow { computation() }` - produce a flow
* `ApiResult(value) ` - either Error or Success based on the type of the value
* `runResulting { computation() }` - for parity with `runCatching`

### Handle errors:

* `or(value)` - returns `value` if the result is an Error
* `orElse { computeValue() }`
* `orNull()`
* `exceptionOrNull()` - if this is an error, returns the exception and discards the result
* `orThrow()` - throw `Error`s
* `fold(onSuccess = { /* ... */ }, onError = { /* ... */ })` - fold the result to type [T]
* `onError<CustomExceptionType> { /* ... */ }`
* `onLoading { setLoading(true) }`
* `onSuccess { computation(it) }`

### Transform:

* `unwrap()` - sometimes you get into a situation where you have `ApiResult<ApiResult<T>>`. Fix using this operator.
* `chain { anotherCall(it) }` - execute another ApiResult call,
  but discard it's Success result and continue with the previous result
* `then { anotherCall(it) }` - execute another ApiResult call and continue with its result type
* `map { it.transform() }`
* `mapWrapping { it.transformThrowing() } ` - map, but catch exceptions in the `transform` block
* `mapLoading { null }`
* `mapError { e -> e.transform() } `  - map only `Error`s
* `mapValues { item -> item.transform() } ` - for collection results
* `errorIf { it.isInvalid }` - error if the predicate is true
* `errorIfNot { it.isAuthorized }`
* `errorOnNull()`
* `nullOnError()` - returns `Success<T?>` if the result is an error
* `recover<MyException, _> { it.handle() }` - recover from a specific exception type
* `recoverIf(condition = { it.isRecoverable }, block = { null })`

## Notes and usage advice

* ApiResult is **not** an async scheduling engine like Rx.
  As soon as you call an operator on the result, it is executed.
* ApiResult does **not** catch `Throwable`s. This was a purposeful decision. We want to only catch Exceptions that
  can be handled.
* ApiResult does **not** catch `CancellationException`s as they are not meant to be caught.
  In case you think you might have wrapped a `CancellationException` in your result,
  use `ApiResult.rethrowCancellation()` at the end of the chain.
* Same as `kotlin.Result`, ApiResult is not meant to be passed around to the UI layer.
  Be sure not to propagate results everywhere in your code, and handle them on the layer responsible for error handling.

## How does ApiResult differ from other wrappers?

* `kotlin.Result` is an existing solution for result wrapping,
  however, it's far less efficient, less safe and, most importantly, doesn't offer the declarative api as rich as
  ApiResult. You could call ApiResult a successor to `kotlin.Result`.
* ApiResult serves a different purpose than [Sandwich](https://github.com/skydoves/sandwich).
  Sandwich specializes in integration with Retrofit and, therefore, is not multiplatform.  
  ApiResult allows you to wrap any computation, be it Ktor, Retrofit, or database call. ApiResult is more lightweight
  and extensible, because it does not hardcode error handling logic. A simple extension on an ApiResult that
  uses `mapErrors` will allow you to transform exceptions to your own error types.
* ApiResult is different from [EitherNet](https://github.com/slackhq/EitherNet) because once again -
  it doesn't hardcode your error types. ApiResult is multiplatform and lightweight:
  no crazy mappings that use reflection to save you from writing 0.5 lines of code to wrap a call in an ApiResult.
* ApiResult is a lighter version of Arrow.kt Monads such as Either. Sometimes you want a monad to wrap your  
  computations, but don't want to introduce the full complexity and intricacies of Arrow and functional programming.
  ApiResult is easier to understand and use, although less powerful than Arrow.
