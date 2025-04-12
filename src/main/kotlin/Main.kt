package com.demo

import payment.CardPayment
import payment.CashPayment
import payment.model.Money

fun main() {
    println("=== Vending Machine Simulation ===")
    val vendingMachine = VendingMachine()

    // 현금 결제 거래 시나리오
    println("\n-- Cash payment.Payment Transaction --")
    val cashPayment = CashPayment()
    vendingMachine.selectPaymentMethod(cashPayment)

    // 1000원권 1매, 100원권 1매 투입 (총 1100원)
    vendingMachine.insertMoney(Money(1000, 1))
    vendingMachine.insertMoney(Money(100, 1))

    // '콜라' 선택
    val selectedBeverage = vendingMachine.selectBeverage("콜라")
    if (selectedBeverage != null) {
        vendingMachine.dispenseBeverage(selectedBeverage)
        vendingMachine.refundChange(selectedBeverage)
    }

    // 카드 결제 거래 시나리오
    println("\n-- Card payment.Payment Transaction --")
    val cardPayment = CardPayment(cardNumber = "1234-5678-9012-3456", cardBalance = 2000)
    vendingMachine.selectPaymentMethod(cardPayment)

    // 카드 결제에서는 현금 투입 없이 '커피' 선택
    val selectedBeverage2 = vendingMachine.selectBeverage("커피")
    if (selectedBeverage2 != null) {
        vendingMachine.dispenseBeverage(selectedBeverage2)
        vendingMachine.refundChange(selectedBeverage2)
    }
}
