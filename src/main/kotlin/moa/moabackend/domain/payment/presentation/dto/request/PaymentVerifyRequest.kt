package moa.moabackend.domain.payment.presentation.dto.request

data class PaymentReadyRequest(
    val quantity: Int,
    val shippingAddress: String
)

data class PaymentVerifyRequest(
    val impUid: String,
    val merchantUid: String
)
