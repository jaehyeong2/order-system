package jjfactory.order_system.domain

import java.time.LocalDateTime

class Order(
    val orderId: String,
    val username: String,
    val createdAt: LocalDateTime,
    var state: OrderState = OrderState.PAYMENT_WAITING
) {

    fun cancel(){
        verifyNotShipped()
        state = OrderState.CANCELED
    }

    private fun verifyNotCanceled(){
        if (isCanceled()) throw CanceledOrderException()
    }

    private fun isCanceled(): Boolean {
        return state == OrderState.CANCELED
    }

    fun markAsShipped(){
        verifyShippable()
        state = OrderState.SHIPPED
    }

    private fun verifyShippable(){
        verifyNotCanceled()
        verifyNotShipped()
    }

    private fun verifyNotShipped(){
        if (!isNotShipped()) throw AlreadyShippedException()
    }

    private fun isNotShipped(): Boolean {
        return state == OrderState.PREPARING || state == OrderState.PAYMENT_WAITING
    }

}