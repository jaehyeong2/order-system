package jjfactory.order_system.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate


@Configuration
class RestTemplateConfig {
    private val logger: Logger = LoggerFactory.getLogger(RestTemplateConfig::class.java)

    @Bean
    fun restTemplate(): RestTemplate {
        val factory: ClientHttpRequestFactory = httpRequestFactory()
        val restTemplate = RestTemplate(factory)

        restTemplate.interceptors.add(loggingInterceptor())
        return restTemplate
    }

    private fun httpRequestFactory(): ClientHttpRequestFactory {
        val factory = HttpComponentsClientHttpRequestFactory()
        factory.setConnectTimeout(5000)
        factory.setConnectionRequestTimeout(5000)
        return factory
    }

    /**
     * 커스텀 로깅 인터셉터
     */
    private fun loggingInterceptor(): ClientHttpRequestInterceptor {
        return ClientHttpRequestInterceptor { request, body, execution ->
            // 요청 로깅
            logger.info("=== Request Begin ===")
            logger.info("URI         : ${request.uri}")
            logger.info("Method      : ${request.method}")
            logger.info("Headers     : ${request.headers}")
            logger.info("Request Body: ${String(body)}")
            logger.info("=== Request End ===")

            val response = execution.execute(request, body)

            // 응답 로깅
            logger.info("=== Response Begin ===")
            logger.info("Status Code : ${response.statusCode}")
            logger.info("Status Text : ${response.statusText}")
            logger.info("Headers      : ${response.headers}")
            logger.info("=== Response End ===")

            response
        }
    }
}
