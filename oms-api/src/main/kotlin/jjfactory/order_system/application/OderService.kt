package jjfactory.order_system.application

import jjfactory.order_system.domain.OrderCommand
import jjfactory.order_system.domain.OrderInfo
import jjfactory.order_system.domain.OrderState
import java.time.LocalDate

interface OderService {
    fun getOrder(orderId: String): OrderInfo.Detail
    fun getList(states: List<OrderState>): List<OrderInfo.List>
    fun cancelOrder(orderId: String)
    fun fetchOrders(date: LocalDate)
}