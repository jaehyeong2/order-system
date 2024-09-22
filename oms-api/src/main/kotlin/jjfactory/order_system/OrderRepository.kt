package jjfactory.order_system

import jjfactory.order_system.domain.Order
import org.springframework.stereotype.Repository

@Repository
class OrderRepository {

    private val store: MutableMap<String, Order> = mutableMapOf()

    fun save(order: Order) {
        store[order.orderId] = order
    }

    fun findByOrderId(orderId: String): Order {
        return store[orderId] ?: throw ResourceNotFoundException()
    }

    fun getList(): List<Order> {
        return store.values.map { it }
    }
}