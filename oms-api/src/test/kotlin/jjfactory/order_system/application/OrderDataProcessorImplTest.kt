package jjfactory.order_system.application

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.net.URI
import java.time.LocalDate
import java.time.LocalDateTime


@ExtendWith(MockitoExtension::class)
class OrderDataProcessorImplTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    private lateinit var orderDataProcessor: OrderDataProcessorImpl

    private var ORDER_API_URL: String = "http://localhost:8090/v1/orders"

    private val logger: Logger = mock(Logger::class.java)

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        orderDataProcessor = OrderDataProcessorImpl(restTemplate)
    }

    @Test
    fun `페칭 성공 시 주문 1개가 저장된다`() {
        // given
        val date = LocalDate.now()
        val mockOrderResponse = listOf(
            OrderResponse(
                id = "1L",
                orderer = Orderer(username = "test_user", phoneNumber = "01012341234"),
                orderBase = OrderBase(createdAt = LocalDateTime.now(), status = "PREPARING", id = 2L, modifiedAt = "", paidAt = "", paymentId = 2L),
            )
        )

        val responseEntity = ResponseEntity.ok(mockOrderResponse)

        // when
        `when`(
            restTemplate.exchange(
                any(URI::class.java),
                eq(HttpMethod.GET),
                any(HttpEntity::class.java),
                any(ParameterizedTypeReference::class.java)
            )
        ).thenReturn(responseEntity)

        // then
        val result = orderDataProcessor.processData(date, ORDER_API_URL)
        assertThat(result.size).isEqualTo(1)
    }

    @Test
    fun `json 파싱 에러 발생 시 빈 리스트가 반환된다`() {
        // given
        val date = LocalDate.now()

        // when
        `when`(
            restTemplate.exchange(
                any(URI::class.java),
                eq(HttpMethod.GET),
                any(HttpEntity::class.java),
                any(ParameterizedTypeReference::class.java)
            )
        ).thenThrow(HttpMessageConversionException("Invalid JSON"))

        // then
        val result = orderDataProcessor.processData(date, ORDER_API_URL)

        assertThat(result).isEmpty()
    }
}
