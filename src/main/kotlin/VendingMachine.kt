package com.demo

import payment.model.Beverage
import payment.model.Money
import payment.CardPayment
import payment.CashPayment
import payment.Payment

class VendingMachine {
    private val beverages = mutableListOf<Beverage>()
    private var payment: Payment? = null

    init {
        beverages.add(Beverage("콜라", 1100, 3))
        beverages.add(Beverage("물", 600, 3))
        beverages.add(Beverage("커피", 700, 3))
    }

    fun selectPaymentMethod(payment: Payment) {
        this.payment = payment
        when (payment) {
            is CashPayment -> println("Cash payment method selected.")
            is CardPayment -> println("Card payment method selected.")
            else -> println("Unknown payment method selected.")
        }
    }

    fun insertMoney(money: Money) {
        if (payment is CashPayment) {
            (payment as CashPayment).insertCash(money)
        } else {
            println("Current payment method is not cash. Cannot insert money.")
        }
    }

    fun selectBeverage(name: String): Beverage? {
        val beverage = beverages.find { it.name == name }
        if (beverage == null) {
            println("model.Beverage '$name' not found.")
        } else if (beverage.stock <= 0) {
            println("model.Beverage '$name' is out of stock.")
            return null
        }
        return beverage
    }

    fun dispenseBeverage(beverage: Beverage) {
        if (payment == null) {
            println("No payment method selected. Cannot dispense beverage.")
            return
        }

        if (!payment!!.processPayment(beverage.price)) {
            println("payment.Payment failed. Cannot dispense beverage.")
            return
        }

        beverage.dispense()
        beverage.reduceStock()
        println("Enjoy your ${beverage.name}!")
    }

    fun refundChange(beverage: Beverage) {
        if (payment is CashPayment) {
            val cashPayment = payment as CashPayment
            val change = cashPayment.amount - beverage.price
            if (change > 0) {
                println("Refunding change: $change")

                // @TODO : Implement logic when the vending machine is short of change
            } else {
                println("No change to refund.")
            }
        } else {
            println("No change refund for card payment.")
        }
        payment?.amount = 0
        payment = null
    }

    fun refillBeverage(name: String, amount: Int): Beverage? {
        val beverage = beverages.find { it.name == name }
        if (beverage != null) {
            beverage.stock += amount
            println("Refilled $amount of $name. New stock: ${beverage.stock}")
        } else {
            // @TODO : If beverage type management policy changed, implement logic to add new beverage
            println("Beverage '$name' not found.")
        }

        return beverage
    }
}
