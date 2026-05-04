package moa.moabackend.domain.payment.presentation.dto.response

data class PaymentReadyResponse(
    val merchantUid: String,
    val amount: Int
)
