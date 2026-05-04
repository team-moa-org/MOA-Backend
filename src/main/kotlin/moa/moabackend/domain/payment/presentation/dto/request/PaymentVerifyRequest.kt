package moa.moabackend.domain.payment.presentation.dto.request

data class PaymentVerifyRequest(
    val impUid: String,
    val merchantUid: String
)
