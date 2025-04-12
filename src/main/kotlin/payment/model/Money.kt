package payment.model

data class Money(
    val denomination: Int,
    val count: Int
) {
    fun getTotal(): Int = denomination * count
}
