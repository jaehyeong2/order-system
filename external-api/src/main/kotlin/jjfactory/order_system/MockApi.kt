package jjfactory.order_system

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RequestMapping("/v1/orders")
@RestController
class MockApi {

    @GetMapping
    fun getOrders(@RequestParam date: String): List<OrderResponse> {
        val sampleOrder = OrderResponse(
            id = 3000805151L,
            orderBase = OrderBase(
                id = 3000805151L,
                paymentId = 3000749085L,
                status = OrderState.values().random().toString(),
                createdAt = "20240604140633",
                paidAt = "20240604140719",
                modifiedAt = "20240604145319"
            ),
            orderer = Orderer(
                phoneNumber = "+82 10-1234-1234",
                username = "kim223"
            ),
            orderProduct = OrderProduct(
                id = 10044612L,
                name = "OPEN API 가이드 상품",
                sellerItemNo = "",
                optionContent = "",
                quantity = 1,
                productPrice = 7700,
                optionPrice = 0,
                sellerDiscountPrice = 300,
                sellerCouponDiscountPrice = 0,
                adminDiscountPrice = 0,
                settlementBasicPrice = 7400,
                baseFee = 244,
                displayFee = 0,
                afCommissionFee = 0,
                discountFee = 0,
                refererCode = "",
                brandName = "카카오",
                deliveryAmountOriginId = 1460896L,
                deliveryAmountPayPointTime = "PayedToOrder",
                deliveryAmountType = "PAID",
                deliveryAmount = 2500,
                areaAdditionalDeliveryAmount = 0
            ),
            orderDeliveryRequest = OrderDeliveryRequest(
                receiverName = "홍길동",
                receiverAddress = "경기 성남시 분당구 판교역로 166 (백현동, 카카오 판교 아지트) 카카오",
                receiverAddress1 = "경기 성남시 분당구 판교역로 166 (백현동, 카카오 판교 아지트)",
                receiverAddress2 = "카카오",
                receiverPhoneNumber = "",
                receiverMobileNumber = "010-1234-1234",
                zipcode = "13529",
                roadZipCode = "13529",
                requirement = ""
            ),
            orderDelivery = OrderDelivery(
                deliveryRequestAt = "20240604140719",
                confirmedAt = "20240604145301",
                invoiceRegisteredAt = "20240604145319",
                shippingMethod = "SHIPPING",
                deliveryCompanyCode = "CJGLS",
                invoiceNumber = "1234567890"
            )
        )

        return listOf(sampleOrder)
    }
}