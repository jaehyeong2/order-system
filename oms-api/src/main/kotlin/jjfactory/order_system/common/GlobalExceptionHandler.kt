package jjfactory.order_system.common

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.RestClientException

@RestControllerAdvice
class GlobalExceptionHandler {
    val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    /**
     * 500 에러 처리
     */
    @ExceptionHandler(Exception::class, RestClientException::class)
    fun handleException(
        ex: Exception,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ErrorResponse {
        log.error("[Internal server Error] {} \n", ex)
        return ErrorResponse(
            message = "Internal server error"
        )
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(
        ex: RuntimeException,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ErrorResponse {
        log.error("[runtime exception] {} \n", ex)

        return ErrorResponse(
            message = ex.message
        )
    }

}