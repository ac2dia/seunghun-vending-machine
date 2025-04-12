package payment

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import payment.model.CardStatus

class CardPaymentTest : BehaviorSpec({

    Given("Call processPayment") {
        When("the payment is processed") {
            val cardPayment = CardPayment(
                cardNumber = "1234-5678-9012-3456",
                cardBalance = 10000,
            )

            val result = cardPayment.processPayment(1000)

            Then("the payment should be successful") {
                result shouldBe true
            }
        }

        When("the payment is failed with insufficient balance") {
            val cardPayment = CardPayment(
                cardNumber = "1234-5678-9012-3456",
                cardBalance = 500,
            )

            val result = cardPayment.processPayment(1000)

            Then("the payment should be failed") {
                result shouldBe false
            }
        }

        When("the payment is failed with not active card") {
            val cardPayment = CardPayment(
                cardNumber = "1234-5678-9012-3456",
                cardBalance = 10000,
                cardStatus = CardStatus.BLOCKED,
            )

            val result = cardPayment.processPayment(1000)

            Then("the payment should be failed") {
                result shouldBe false
            }
        }
    }
})
