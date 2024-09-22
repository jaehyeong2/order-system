package jjfactory.order_system.application

import jjfactory.order_system.common.config.RestTemplateConfig
import jjfactory.order_system.domain.Order
import jjfactory.order_system.domain.OrderState
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.http.converter.HttpMessageConversionException
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
    private val logger: Logger = LoggerFactory.getLogger(RestTemplateConfig::class.java)

    @Retryable(
        maxAttempts = 3,
        backoff = Backoff(delay = 1000),
        include = [IOException::class],
        exclude = [HttpMessageConversionException::class]
    )
    override fun processData(date: LocalDate, url: String): List<Order> {
        val (entity, uri) = buildOrderUri(date, url)

        lateinit var responseEntity: ResponseEntity<List<OrderResponse>>

        try {
            responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                object : ParameterizedTypeReference<List<OrderResponse>>() {}
            )
        } catch (e: HttpMessageConversionException) {
            logger.error("json parse failed..")
            //todo noti logic
            return emptyList()
        }

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

    private fun buildOrderUri(date: LocalDate, url: String): Pair<HttpEntity<String>, URI> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val entity = HttpEntity<String>(headers)
        val params = mapOf("date" to date)

        val uri = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("date", "{date}")
            .buildAndExpand(params)
            .toUri()
        return Pair(entity, uri)
    }
}