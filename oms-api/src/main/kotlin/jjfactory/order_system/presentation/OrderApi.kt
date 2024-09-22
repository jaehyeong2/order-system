package jjfactory.order_system.presentation

import jjfactory.order_system.application.OderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate


@RequestMapping("/v1/orders")
@RestController
class OrderApi(
    private val orderService: OderService
) {

    @PostMapping
    fun fetchOrders(){
        orderService.fetchOrders(LocalDate.now())
    }

    @GetMapping
    fun getOrders(){
        orderService.getList(emptyList()).forEach {
            println("it = ${it}")
        }
    }
}