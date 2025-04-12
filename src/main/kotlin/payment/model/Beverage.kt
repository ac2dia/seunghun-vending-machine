package payment.model

data class Beverage(
    var name: String,
    var price: Int,
    var stock: Int
) {
    fun dispense() {
        println("Dispensing $name...")
    }

    fun reduceStock() {
        if (stock > 0) {
            stock--
            println("Stock for $name reduced. Remaining: $stock")
        } else {
            println("$name is out of stock!")
        }
    }
}
