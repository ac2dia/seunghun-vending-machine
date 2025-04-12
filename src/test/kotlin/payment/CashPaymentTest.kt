package payment

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import payment.model.Money

class CashPaymentTest : BehaviorSpec({

    Given("Call insertCash") {
        When("the insert cash is supported denomination") {
            val cashPayment = CashPayment()
            cashPayment.insertCash(Money(1000, 1))

            Then("the cash should be inserted successfully") {
                cashPayment.amount shouldBe 1000
            }
        }

        When("the insert cash is not supported denomination") {
            val cashPayment = CashPayment()
            cashPayment.insertCash(Money(2000, 1))

            Then("the cash should not be inserted") {
                cashPayment.amount shouldBe 0
            }
        }
    }

    Given("Call processPayment") {
        When("the payment is processed") {
            val cashPayment = CashPayment()
            cashPayment.insertCash(Money(1000, 1))

            val result = cashPayment.processPayment(1000)

            Then("the payment should be successful") {
                result shouldBe true
            }
        }

        When("the payment is failed with insufficient balance") {
            val cashPayment = CashPayment()
            cashPayment.insertCash(Money(500, 1))

            val result = cashPayment.processPayment(1000)

            Then("the payment should be failed") {
                result shouldBe false
            }
        }
    }
})
