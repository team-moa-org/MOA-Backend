package moa.moabackend.infra.portone.dto.response

data class PortOneTokenResponse(
    val code: Int,
    val message: String?,
    val response: TokenData?
)

data class TokenData(
    val access_token: String,
    val now: Long,
    val expired_at: Long
)

data class PortOnePaymentResponse(
    val code: Int,
    val message: String?,
    val response: PaymentData?
)

data class PaymentData(
    val imp_uid: String,
    val merchant_uid: String,
    val amount: Int,
    val status: String
)
