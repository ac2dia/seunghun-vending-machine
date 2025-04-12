package payment

abstract class Payment {
    var amount: Int = 0
    abstract fun processPayment(price: Int): Boolean
}
