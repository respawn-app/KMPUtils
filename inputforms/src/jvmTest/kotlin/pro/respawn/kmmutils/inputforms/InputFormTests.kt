package pro.respawn.kmmutils.inputforms

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import pro.respawn.kmmutils.inputforms.default.Forms
import pro.respawn.kmmutils.inputforms.dsl.empty
import pro.respawn.kmmutils.inputforms.dsl.input
import pro.respawn.kmmutils.inputforms.dsl.mustMatch

private const val default = "John Doe"
private const val email = "john.doe211@email.com"

@Suppress("StringShouldBeRawString")
private const val blank = "\t\t\n "

@Suppress("UnnecessaryParentheses") // false-positive
class InputFormTests : FreeSpec({
    "given default input created" - {
        "when value is null then type is empty" {
            val input = input()
            input shouldBe Input.Empty()
        }
        "when value contains text then type is valid" {
            val input = input(default)
            input shouldBe Input.Valid(default)
        }
        "when value is blank then type is empty, value is discarded" {
            val input = input(blank)
            input shouldBe empty()
        }
    }
    "given a form" - {
        val form = Forms.Email(ValidationStrategy.FailFast)
        "when value is blank then result is invalid" {
            form(blank) shouldBe Input.Invalid(blank, errors = listOf(ValidationError.Empty(blank)))
        }
        "when value is a valid email then validation passes" {
            form(email) shouldBe Input.Valid(email)
        }
        "and two different inputs" - {
            val parentInput = form(email)
            val input = form(blank)
            "when value is required to match another one then validation errors are preserved" {
                val errors = listOf(ValidationError.Empty(blank), ValidationError.IsNotEqual(blank, email))
                (input mustMatch parentInput) shouldBe Input.Invalid(blank, errors)
            }
        }
    }
})
