package jjfactory.order_system.application

import jjfactory.order_system.domain.Order
import jjfactory.order_system.domain.OrderInfo
import jjfactory.order_system.domain.OrderState
import jjfactory.order_system.repository.OrderRepository
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate

@Service
class OderServiceImpl(
    private val orderRepository: OrderRepository,
    private val restTemplate: RestTemplate
) : OderService {

    private val ORDER_API_URL = "http://localhost:8090/v1/orders?date=2024-09-22"

    override fun fetchOrders(date: LocalDate) {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val entity = HttpEntity<String>(headers)
        val params = mapOf("date" to date)

        val uri = UriComponentsBuilder.fromHttpUrl(ORDER_API_URL)
            .queryParam("date", "{date}")
            .buildAndExpand(params)
            .toUri()

        val responseEntity = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            entity,
            object : ParameterizedTypeReference<List<OrderResponse>>() {}
        )

        responseEntity.body?.forEach {
            val order = Order(
                orderId = it.id,
                username = it.orderer.username,
                createdAt = it.orderBase.createdAt,
                state = OrderState.valueOf(it.orderBase.status)
            )

            orderRepository.save(order)
        }

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

    override fun getList(states: List<OrderState>): List<OrderInfo.List> {
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