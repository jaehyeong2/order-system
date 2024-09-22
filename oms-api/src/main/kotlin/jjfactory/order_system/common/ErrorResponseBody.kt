package jjfactory.order_system.common

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorResponse(
    val status: Int = HttpStatus.INTERNAL_SERVER_ERROR.value(),
    val errorCode: String? = status.toString(),
    val message: String?,
    val timestamp: LocalDateTime = LocalDateTime.now().withNano(0)
)