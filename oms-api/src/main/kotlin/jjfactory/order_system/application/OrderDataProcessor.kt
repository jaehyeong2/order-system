package jjfactory.order_system.application

import jjfactory.order_system.domain.Order
import java.time.LocalDate

interface OrderDataProcessor {
    fun processData(date: LocalDate): List<Order>
}