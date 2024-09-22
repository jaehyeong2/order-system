package jjfactory.order_system.domain

import java.time.LocalDateTime

class OrderCommand {
    data class Create(
        val orderId: String,
        val username: String,
        val createdAt: LocalDateTime
    ) {
        fun toEntity(): Order {
            return Order(
                orderId = orderId,
                username = username,
                createdAt = createdAt
            )
        }
    }

}