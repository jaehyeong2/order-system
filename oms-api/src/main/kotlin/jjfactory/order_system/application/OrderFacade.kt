package jjfactory.order_system.application

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class OrderFacade(
    private val orderDataProcessor: OrderDataProcessor,
    private val orderService: OrderService
) {
    @Value("\${order-api.uri}")
    private lateinit var ORDER_API_URL: String

    fun fetchOrders(date: LocalDate) {
        val orders = orderDataProcessor.processData(date, ORDER_API_URL)

        orders.forEach {
            orderService.save(it)
        }
    }
}