package moa.moabackend.domain.participation.presentation.dto.response

import java.time.LocalDateTime

data class ParticipantResponse(
    val userId: Long,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val quantity: Int,
    val paymentAmount: Int,
    val status: String,
    val orderedAt: LocalDateTime
)

data class ParticipantDetailResponse(
    val userId: Long,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val status: String,
    val shippingAddress: String,
    val quantity: Int,
    val orderedAt: LocalDateTime,
    val paymentAmount: Int
)
