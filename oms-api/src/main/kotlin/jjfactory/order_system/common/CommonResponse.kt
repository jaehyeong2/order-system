package jjfactory.order_system.common

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class CommonResponse<T> (
    var status: Int = HttpStatus.OK.value(),
    var data: T?,
    var message: String?,
    val responseTime: LocalDateTime = LocalDateTime.now(),
) {
    constructor(data: T, message: String) : this(HttpStatus.OK.value(), data, message, LocalDateTime.now())
    constructor(data: T) : this(HttpStatus.OK.value(), data, null, LocalDateTime.now())

    companion object {
        val OK = CommonResponse(Unit);
    }
}
