package jjfactory.order_system.application

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class OrderFacade(
    private val orderDataProcessor: OrderDataProcessor,
    private val orderService: OderService
) {

    fun fetchOrders(date: LocalDate) {
        val orders = orderDataProcessor.processData(date)

        orders.forEach {
            orderService.save(it)
        }
    }
}