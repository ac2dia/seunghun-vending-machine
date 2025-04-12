package payment

import payment.model.CardStatus

class CardPayment(
    val cardNumber: String,
    var cardBalance: Int,
    var cardStatus: CardStatus = CardStatus.ACTIVE
) : Payment() {
    override fun processPayment(price: Int): Boolean {
        when (cardStatus) {
            CardStatus.ACTIVE -> println("Card is active.")
            else -> {
                println("Card is not active. Status: $cardStatus")
                return false
            }
        }

        if (cardBalance < price) {
            println("Card payment failed. Insufficient balance on card $cardNumber. Available: $cardBalance, Required: $price")
            return false
        }
        cardBalance -= price
        amount = price
        println("Card payment of $price processed successfully. New balance: $cardBalance")
        return true
    }
}
