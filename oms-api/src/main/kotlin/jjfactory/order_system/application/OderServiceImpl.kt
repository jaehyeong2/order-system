package jjfactory.order_system.application

import jjfactory.order_system.domain.Order
import jjfactory.order_system.domain.OrderInfo
import jjfactory.order_system.domain.OrderState
import jjfactory.order_system.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OderServiceImpl(
    private val orderRepository: OrderRepository,
) : OderService {

    override fun save(order: Order) {
        orderRepository.save(order)
    }

    override fun getOrder(orderId: String): OrderInfo.Detail {
        return orderRepository.findByOrderId(orderId).let {
            OrderInfo.Detail(
                orderId = it.orderId,
                username = it.username,
                createdAt = it.createdAt,
                state = it.state.toString()
            )
        }
    }

    override fun getList(stateStrings: List<String>): List<OrderInfo.List> {
        val states = stateStrings.map {
            OrderState.valueOf(it)
        }

        return orderRepository.getList()
            .filter {
                if (states.isNotEmpty()) states.contains(it.state)
                else true
            }
            .map {
                OrderInfo.List(
                    orderId = it.orderId,
                    username = it.username,
                    createdAt = it.createdAt,
                    state = it.state.toString()
                )
            }
    }

    override fun cancelOrder(orderId: String) {
        return orderRepository.findByOrderId(orderId).cancel()
    }
}