package jjfactory.order_system.application

import jjfactory.order_system.domain.Order
import jjfactory.order_system.domain.OrderInfo

interface OrderService {
    fun getOrder(orderId: String): OrderInfo.Detail
    fun getList(states: List<String>): List<OrderInfo.List>
    fun cancelOrder(orderId: String)
    fun save(order: Order)
}