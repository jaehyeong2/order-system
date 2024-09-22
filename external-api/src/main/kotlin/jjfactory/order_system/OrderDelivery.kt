package jjfactory.order_system

import java.time.LocalDateTime

data class OrderResponse(
    val id: Long,
    val orderBase: OrderBase,
    val orderer: Orderer,
    val orderProduct: OrderProduct,
    val orderDeliveryRequest: OrderDeliveryRequest,
    val orderDelivery: OrderDelivery
)

data class OrderBase(
    val id: Long,
    val paymentId: Long,
    val status: String,
    val createdAt: LocalDateTime,
    val paidAt: String,
    val modifiedAt: String
)

data class Orderer(
    val phoneNumber: String,
    val username: String
)

data class OrderProduct(
    val id: Long,
    val name: String,
    val sellerItemNo: String,
    val optionContent: String,
    val quantity: Int,
    val productPrice: Int,
    val optionPrice: Int,
    val sellerDiscountPrice: Int,
    val sellerCouponDiscountPrice: Int,
    val adminDiscountPrice: Int,
    val settlementBasicPrice: Int,
    val baseFee: Int,
    val displayFee: Int,
    val afCommissionFee: Int,
    val discountFee: Int,
    val refererCode: String,
    val brandName: String,
    val deliveryAmountOriginId: Long,
    val deliveryAmountPayPointTime: String,
    val deliveryAmountType: String,
    val deliveryAmount: Int,
    val areaAdditionalDeliveryAmount: Int
)

data class OrderDeliveryRequest(
    val receiverName: String,
    val receiverAddress: String,
    val receiverAddress1: String,
    val receiverAddress2: String,
    val receiverPhoneNumber: String,
    val receiverMobileNumber: String,
    val zipcode: String,
    val roadZipCode: String,
    val requirement: String
)

data class OrderDelivery(
    val deliveryRequestAt: String,
    val confirmedAt: String,
    val invoiceRegisteredAt: String,
    val shippingMethod: String,
    val deliveryCompanyCode: String,
    val invoiceNumber: String
)
