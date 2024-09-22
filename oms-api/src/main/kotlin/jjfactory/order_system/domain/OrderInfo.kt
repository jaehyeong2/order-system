package jjfactory.order_system.domain

import java.time.LocalDateTime

class OrderInfo {
    data class Detail(
        val orderId: String,
        val username: String,
        val createdAt: LocalDateTime,
        val state: String
    )

    data class List(
        val orderId: String,
        val username: String,
        val createdAt: LocalDateTime,
        val state: String
    )

}