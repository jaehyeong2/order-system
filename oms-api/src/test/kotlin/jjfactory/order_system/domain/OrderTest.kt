package jjfactory.order_system.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.LocalDateTime

class OrderTest {

    @Test
    fun `주문 취소시 상태가 취소로 바뀐다`() {
        val order = Order(
            orderId = "test",
            username = "lee",
            createdAt = LocalDateTime.now(),
        )

        order.cancel()
        assertThat(order.state).isEqualTo(OrderState.CANCELED)
    }

    @ParameterizedTest
    @EnumSource(
        value = OrderState::class,
        names = ["SHIPPED", "DELIVERING", "DELIVERY_COMPLETED"]
    )
    fun `취소 불가능 상태에서 취소할 때는 예외를 던진다`(state: OrderState) {
        val order = Order(
            orderId = "test",
            username = "lee",
            createdAt = LocalDateTime.now(),
        )

        order.state = state

        assertThatThrownBy {
            order.cancel()
        }.isInstanceOf(AlreadyShippedException::class.java)
    }

    @Test
    fun `제품 발송 시 발송으로 상태가 바뀐다`() {
        val order = Order(
            orderId = "test",
            username = "lee",
            createdAt = LocalDateTime.now(),
        )

        order.markAsShipped()
        assertThat(order.state).isEqualTo(OrderState.SHIPPED)
    }

    @ParameterizedTest
    @EnumSource(
        value = OrderState::class,
        names = ["SHIPPED", "DELIVERING", "DELIVERY_COMPLETED"]
    )
    fun `이미 발송되었는데 재발송 요청시 예외를 던진다`(state: OrderState) {
        val order = Order(
            orderId = "test",
            username = "lee",
            createdAt = LocalDateTime.now(),
        )

        order.state = state

        assertThatThrownBy {
            order.markAsShipped()
        }.isInstanceOf(AlreadyShippedException::class.java)
    }

    @Test
    fun `취소 상품 발송 요청시 예외를 던진다`() {
        val order = Order(
            orderId = "test",
            username = "lee",
            createdAt = LocalDateTime.now(),
        )

        order.state = OrderState.CANCELED

        assertThatThrownBy {
            order.markAsShipped()
        }.isInstanceOf(CanceledOrderException::class.java)
    }

}