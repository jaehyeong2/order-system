package jjfactory.order_system.application

import jjfactory.order_system.domain.Order
import jjfactory.order_system.domain.OrderState
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate

@Component
class OrderDataProcessorImpl(
    private val restTemplate: RestTemplate
) : OrderDataProcessor {

    private val ORDER_API_URL = "http://localhost:8090/v1/orders?date=2024-09-22"

    override fun processData(date: LocalDate): List<Order> {
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


        val orderResponses = responseEntity.body

        return orderResponses?.map {
            Order(
                orderId = it.id,
                username = it.orderer.username,
                createdAt = it.orderBase.createdAt,
                state = OrderState.valueOf(it.orderBase.status)
            )
        } ?: emptyList()
    }
}