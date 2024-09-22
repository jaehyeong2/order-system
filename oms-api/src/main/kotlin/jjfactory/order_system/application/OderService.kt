package jjfactory.order_system.application

import jjfactory.order_system.domain.Order
import jjfactory.order_system.domain.OrderInfo
import jjfactory.order_system.domain.OrderState

interface OderService {
    fun getOrder(orderId: String): OrderInfo.Detail
    fun getList(states: List<OrderState>): List<OrderInfo.List>
    fun cancelOrder(orderId: String)
    fun save(order: Order)
}