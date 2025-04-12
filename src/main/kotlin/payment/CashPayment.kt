package payment

import payment.model.Money

class CashPayment : Payment() {
    private val cashInserted = mutableListOf<Money>()

    fun insertCash(money: Money) {
        // 지원하는 화폐 단위: 100, 500, 1000, 5000, 10000원
        val supportedDenominations = setOf(100, 500, 1000, 5000, 10000)
        if (!supportedDenominations.contains(money.denomination)) {
            println("Unsupported currency denomination: ${money.denomination}")
            return
        }
        cashInserted.add(money)
        val inserted = money.getTotal()
        amount += inserted
        println("Inserted cash: $inserted. Total inserted: $amount")
    }

    override fun processPayment(price: Int): Boolean {
        if (amount < price) {
            println("Insufficient cash. Required: $price, Inserted: $amount")
            return false
        }
        println("Cash payment of $price processed successfully.")
        return true
    }
}
