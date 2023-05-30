# Input Forms

Browse
code: [InputForms](https://github.com/respawn-app/kmmutils/tree/master/inputforms/src/commonMain/kotlin/pro/respawn/kmmutils/inputforms)

InputForms is a stateful form validation framework with clean dsl and a collection of prebuilt validations.
The reason for its existence lies in the need for shifting the responsibility for validating inputs down from the UI
layer to the business logic layer. A set of abstractions is needed in order to accomplish that, that will allow you to
pass the resulting value up and handle it both in the business logic and in the UI layer.
Forms play very well with our architecture, [FlowMVI](https://opensource.respawn.pro/FlowMVI) and MVI in general.

## Usage

The framework comes with a few basic classes you'll need:

- `Form` - basically, a collection of `Rule`s that specify how to validate the input.
- `Input` - a wrapper for a text value that can be `Empty`, `Valid`, or `Invalid`. An invalid value
  contains `ValidationErrors`.
- `Rule` - an interface that runs a single validation on an input.
- `ValidationError` - an error that happened during validation.
- `ValidationStrategy` - either `FailFast` or `LazyEval`, depending on how you want your form errors to be shown.

### 1. Understand how to use and create Rules

There are quite a few prebuilt Rules. See the `Rules` object to check them out.  
Rules are a simple `fun interface`s that Specify how to validate a string.  
Create your own rules like this:

```kotlin
fun LongerThan(minLength: Int): Rule = Rule { it: String ->
    { it.length >= minLength } checks { TooShort(it, minLength) }
}
```

A `Rule`'s `invoke` function takes a `String` and returns a `Sequence` of `ValidationError`s.  
`infix fun (() -> Boolean).checks(error: ()-> ValidationError)` is a syntactic sugar for one-condition -> one error
validations. When the block returns `false`, an `error` is evaluated lazily and returned.

### 2. Choose a ValidationStrategy

There are 2 strategies: `FailFast` and `LazyEval`.

- `FailFast` means that as soon as an error is encountered, validation stops. A list of `ValidationError`s will only
  contain 0 or 1 values. That's why the order of `Rule`s in the `Form` matters.
- `LazyEval` iterates through all the Rules, collects their `ValidationErrors`, and only then returns an appropriate
  Input. Order of rules is preserved too.

### 3. Create your validation Form

A custom Form is built like this

```kotlin
val EmailForm = Form(
    ValidationStrategy.FailFast,
    Rules.NonEmpty,
    Rules.LengthInRange(FieldLengths.Email),
    Rules.Matches(Patterns.Email),
)
```

* First, specify a strategy for validation.
* Then list all the Rules you want to apply to the input of the form.
    * **The order of Rules matters!**
* The Form instance is usually located in the business logic layer.
    * It can be a static object or a builder function return value.
* Some prebuilt forms are in the `Forms` object.
* If you want to add custom fields, you can subclass `Form` instead
    ```kotlin
    object EmailForm : Form(
        ValidationStrategy.FailFast,
        /* ... */
    ) {
        val LengthRange = 1..256 // can use on UI
    }
   ```
### 4. Create Input values for string values you want to validate

For example, when building your state, use this to set up blank or default Inputs:

```kotlin
val defaultName = "John Doe"

data class DisplayingSignInForm(
    val email: Input = input(), // Input.Empty("")
    val password: Input = input(),
    val passwordConfirm: Input = input(),
    val name: Input = input(defaultName), // Input.Valid("John Doe")
    val isPasswordVisible: Boolean = false,
) : EmailSignInState
```

### 5. Display `ValidationError`s

As simple as that, these are validation errors.
To add your own errors (when writing custom `Rule`s), subclass `ValidationError.Generic` and iterate over types.
To represent validation errors, you'll need a function that maps `List<ValidationError>` to a `String` or other
structure you want to use to display errors. Before you can draw your inputs, define that function.
This part of the implementation is always on you since it differs from app to app.
Simple example for Compose:

```kotlin
@Composable
fun List<ValidationError>.toRepresentation() = map { it.toRepresentation() }.joinToString("\n")

@Composable
private fun ValidationError.toRepresentation() = when (this) {
    is ValidationError.Empty -> R.string.validation_error_empty
    /* ... iterate over all types you want to support ... */
}.let(::stringResource)
```

### 6. Display the Input in the UI

Each validation will return either an `Input.Valid`, or `Input.Invalid` . An `Invalid` value contains a field
called `errors`, which is a collection of validation errors . Use that, the function you defined earlier, and the type
of the input type to display it. Example for Compose:

```kotlin
@Composable
fun InputTextField(input: Input, onTextChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = input.value,
            onValueChange = onTextChange,
            isError = input is Input.Invalid,
            // add max length display, available symbols, etc, coloring as needed.
        )
        // display errors below the text field
        AnimatedVisibility(visible = input is Invalid) {
            if (input !is Invalid) return@AnimatedVisibility
            Text(
                text = input.errors.toRepresentation(),
                modifier = Modifier.padding(2.dp),
            )
        }
    }
}
```

### 7. Validate the input when the user changes it

Invoke a validation on the user's input (String value) each time the user changes it, or whenever you want to validate
forms. For example, like this:

```kotlin
val PasswordForm = Forms.Password()
fun onPasswordConfirmationChange(value: String) = _viewState.update {
    it.copy(passwordConfirm = PasswordForm(value) mustMatch it.password)
}
```

- `Form.invoke(input: String)` returns a validated Input
- `Input.mustMatch(other: Input)` returns an input that is additionally required to be equal to another input

That's it.

* If you are using mustMatch, be sure to validate **both** the dependant and the base fields whenever one
  changes. Do this for each Input in your state.
* Validation can happen on a background thread, but be sure to update the state with the newest value
* **on the main thread and immediately**. Otherwise, you will face delays and jitter when trying to edit the text in the
  form. So, copy the value of the input immediately, and validate it later
