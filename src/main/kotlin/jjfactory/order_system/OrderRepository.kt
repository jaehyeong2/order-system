package jjfactory.order_system

class OrderRepository {

    private val map: MutableMap<String, Order> = mutableMapOf()

    fun save(order: Order) {
        map[order.orderId] = order
    }

    fun findByOrderId(orderId: String): Order {
        return map[orderId] ?: throw ResourceNotFoundException()
    }
}