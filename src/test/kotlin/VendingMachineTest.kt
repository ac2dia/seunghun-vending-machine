package com.demo

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import payment.CardPayment
import payment.CashPayment
import payment.model.Money

class VendingMachineTest : BehaviorSpec({

    Given("Call selectPaymentMethod") {
        When("CashPayment is selected") {
            val vendingMachine = VendingMachine()
            val cashPayment = CashPayment()
            vendingMachine.selectPaymentMethod(cashPayment)

            Then("inserting cash should work") {
                vendingMachine.insertMoney(Money(1000, 1))
                cashPayment.amount shouldBe 1000
            }
        }

        When("CardPayment is selected") {
            val vendingMachine = VendingMachine()
            val cardPayment = CardPayment("1234-5678-9012-3456", 2000)
            vendingMachine.selectPaymentMethod(cardPayment)

            Then("inserting cash should be rejected") {
                vendingMachine.insertMoney(Money(1000, 1))
                cardPayment.amount shouldBe 0
            }
        }
    }

    Given("Call selectBeverage") {
        When("the requested beverage exists and is in stock") {
            val vendingMachine = VendingMachine()
            val beverage = vendingMachine.selectBeverage("콜라")
            Then("a beverage object should be returned") {
                beverage shouldNotBe null
                beverage?.let {
                    it.name shouldBe "콜라"
                    it.price shouldBe 1100
                }
            }
        }

        When("the requested beverage does not exist") {
            val vendingMachine = VendingMachine()
            val beverage = vendingMachine.selectBeverage("쥬스")
            Then("null should be returned") {
                beverage shouldBe null
            }
        }

        When("the requested beverage is out of stock") {
            val vendingMachine = VendingMachine()
            val water = vendingMachine.selectBeverage("물")
            water?.stock = 0

            val selected = vendingMachine.selectBeverage("물")
            Then("null should be returned as the beverage is out of stock") {
                selected shouldBe null
            }
        }
    }

    Given("Call dispenseBeverage") {
        When("no payment method is selected") {
            val vendingMachine = VendingMachine()
            val beverage = vendingMachine.selectBeverage("커피")

            vendingMachine.dispenseBeverage(beverage!!)
            Then("the beverage should not be dispensed (stock remains unchanged)") {
                beverage.stock shouldBe 3
            }
        }

        When("payment fails due to insufficient funds") {
            val vendingMachine = VendingMachine()
            val cashPayment = CashPayment()

            vendingMachine.selectPaymentMethod(cashPayment)
            cashPayment.insertCash(Money(500, 1))

            val beverage = vendingMachine.selectBeverage("커피")
            vendingMachine.dispenseBeverage(beverage!!)
            Then("the beverage should not be dispensed") {
                beverage.stock shouldBe 3
            }
        }

        When("cash payment succeeds and beverage is dispensed") {
            val vendingMachine = VendingMachine()
            val cashPayment = CashPayment()

            vendingMachine.selectPaymentMethod(cashPayment)
            cashPayment.insertCash(Money(1000, 1))

            val beverage = vendingMachine.selectBeverage("커피")
            vendingMachine.dispenseBeverage(beverage!!)
            Then("the beverage stock should be reduced and the beverage dispensed") {
                beverage.stock shouldBe 2
            }
        }

        When("card payment succeeds and beverage is dispensed") {
            val vendingMachine = VendingMachine()
            val cardPayment = CardPayment("1234-5678-9012-3456", 2000)
            vendingMachine.selectPaymentMethod(cardPayment)

            val beverage = vendingMachine.selectBeverage("커피")
            vendingMachine.dispenseBeverage(beverage!!)
            Then("the beverage stock should be reduced and card balance updated") {
                beverage.stock shouldBe 2
                cardPayment.cardBalance shouldBe 1300
            }
        }
    }

    Given("Call refundChange") {
        When("using cash payment with extra money inserted") {
            val vendingMachine = VendingMachine()
            val cashPayment = CashPayment()
            vendingMachine.selectPaymentMethod(cashPayment)
            cashPayment.insertCash(Money(1000, 1))
            cashPayment.insertCash(Money(500, 1))

            val beverage = vendingMachine.selectBeverage("커피")
            vendingMachine.dispenseBeverage(beverage!!)
            vendingMachine.refundChange(beverage)
            Then("the payment amount should be reset and change refunded") {
                cashPayment.amount shouldBe 0
            }
        }

        When("using card payment") {
            val vendingMachine = VendingMachine()
            val cardPayment = CardPayment("1234-5678-9012-3456", 2000)
            vendingMachine.selectPaymentMethod(cardPayment)

            val beverage = vendingMachine.selectBeverage("커피")
            vendingMachine.dispenseBeverage(beverage!!)
            vendingMachine.refundChange(beverage)
            Then("no change should be refunded for card payments and payment should be reset") {
                cardPayment.amount shouldBe 0
            }
        }
    }

    Given("Call refillBeverage") {
        When("the beverage is refilled") {
            val vendingMachine = VendingMachine()
            val beverage = vendingMachine.selectBeverage("콜라")
            beverage?.stock = 0

            val result = vendingMachine.refillBeverage("콜라", 5)
            Then("the stock should be updated") {
                beverage?.stock shouldBe 5
                result?.name shouldBe "콜라"
            }
        }

        When("the beverage does not exist") {
            val vendingMachine = VendingMachine()
            val result = vendingMachine.refillBeverage("쥬스", 5)
            Then("an error message should be printed") {
                result shouldBe null
            }
        }
    }
})
