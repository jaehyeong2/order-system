package jjfactory.order_system

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrderSystemApplication

fun main(args: Array<String>) {
	runApplication<OrderSystemApplication>(*args)
}
