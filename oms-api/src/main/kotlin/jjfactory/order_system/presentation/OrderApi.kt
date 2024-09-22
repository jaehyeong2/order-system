package jjfactory.order_system.presentation

import jjfactory.order_system.application.OderService
import jjfactory.order_system.application.OrderFacade
import jjfactory.order_system.common.CommonResponse
import jjfactory.order_system.domain.OrderInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate


@RequestMapping("/v1/orders")
@RestController
class OrderApi(
    private val orderService: OderService,
    private val orderFacade: OrderFacade
) {

    @PostMapping
    fun fetchOrders(@RequestParam(required = false) date: LocalDate?){
        orderFacade.fetchOrders(date ?: LocalDate.now())
    }

    @GetMapping
    fun getOrders(@RequestParam(required = false) states: List<String>?): CommonResponse<List<OrderInfo.List>> {
        return CommonResponse(orderService.getList(states ?: emptyList()))
    }

    @GetMapping("/{orderId}")
    fun getOrder(@PathVariable orderId: String): CommonResponse<OrderInfo.Detail> {
        return CommonResponse(orderService.getOrder(orderId))
    }

    @PostMapping("/cancel")
    fun cancelOrder(@RequestParam orderId: String): CommonResponse<Unit> {
        orderService.cancelOrder(orderId)
        return CommonResponse.OK
    }
}