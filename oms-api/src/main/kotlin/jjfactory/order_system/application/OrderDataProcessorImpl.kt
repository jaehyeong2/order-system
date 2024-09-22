package jjfactory.order_system.application

import jjfactory.order_system.domain.Order
import jjfactory.order_system.domain.OrderState
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.net.URI
import java.time.LocalDate

@Component
class OrderDataProcessorImpl(
    private val restTemplate: RestTemplate
) : OrderDataProcessor {

    @Value("\${order-api.uri}")
    private lateinit var ORDER_API_URL: String

    @Retryable(
        maxAttempts = 3,
        backoff = Backoff(delay = 1000),
        include = [IOException::class]
    )
    override fun processData(date: LocalDate): List<Order> {
        val (entity, uri) = buildOrderUri(date)

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

    private fun buildOrderUri(date: LocalDate): Pair<HttpEntity<String>, URI> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val entity = HttpEntity<String>(headers)
        val params = mapOf("date" to date)

        val uri = UriComponentsBuilder.fromHttpUrl(ORDER_API_URL)
            .queryParam("date", "{date}")
            .buildAndExpand(params)
            .toUri()
        return Pair(entity, uri)
    }
}