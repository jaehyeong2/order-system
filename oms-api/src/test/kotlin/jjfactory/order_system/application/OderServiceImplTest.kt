package jjfactory.order_system.application

import jjfactory.order_system.domain.Order
import jjfactory.order_system.domain.OrderState
import jjfactory.order_system.repository.OrderRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class OderServiceImplTest {
    private val orderRepository: OrderRepository = OrderRepository()
    private val orderService: OrderServiceImpl = OrderServiceImpl(orderRepository)

    @Test
    fun `주문 저장 성공 시 메모리에 저장된다`() {
        val order = Order(
            orderId = "test",
            username = "lee",
            createdAt = LocalDateTime.now(),
        )

        orderService.save(order)

        assertThat(orderRepository.getList().size).isEqualTo(1)
    }

    @Test
    fun `주문 저장 후 리스트 조회 시 입력상태값에 맞게 조회된다`() {
        val order = Order(
            orderId = "test",
            username = "lee",
            createdAt = LocalDateTime.now(),
        )

        val order2 = Order(
            orderId = "test2",
            username = "lee",
            createdAt = LocalDateTime.now(),
            state = OrderState.CANCELED
        )

        orderRepository.save(order)
        orderRepository.save(order2)

        assertThat(orderService.getList(listOf("CANCELED")).size).isEqualTo(1)
    }

    @Test
    fun `주문 저장 후 주문 아이디로 조회가 가능하다`() {
        val order = Order(
            orderId = "test",
            username = "lee",
            createdAt = LocalDateTime.now(),
        )

        orderRepository.save(order)

        assertThat(orderService.getOrder("test").username).isEqualTo("lee")
    }

    @Test
    fun `주문 저장 후 주문 아이디로 취소가 가능하다`() {
        val order = Order(
            orderId = "test",
            username = "lee",
            createdAt = LocalDateTime.now(),
        )

        orderRepository.save(order)
        orderService.cancelOrder("test")

        assertThat(orderService.getOrder("test").state).isEqualTo(OrderState.CANCELED.toString())
    }

}