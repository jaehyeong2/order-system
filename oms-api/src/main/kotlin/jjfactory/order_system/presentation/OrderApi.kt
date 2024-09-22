package jjfactory.order_system.presentation

import jjfactory.order_system.application.OderService
import jjfactory.order_system.application.OrderFacade
import jjfactory.order_system.common.CommonResponse
import jjfactory.order_system.domain.OrderInfo
import org.springframework.web.bind.annotation.GetMapping
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
    fun fetchOrders(){
        orderFacade.fetchOrders(LocalDate.now())
    }

    @GetMapping
    fun getOrders(@RequestParam(required = false) states: List<String>?): CommonResponse<List<OrderInfo.List>> {
        return CommonResponse(orderService.getList(states ?: emptyList()))
    }
}