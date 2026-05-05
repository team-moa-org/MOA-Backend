package moa.moabackend.domain.participation.service

import moa.moabackend.domain.grouppurchase.domain.exception.GroupPurchaseNotFoundException
import moa.moabackend.domain.grouppurchase.domain.repository.GroupPurchaseRepository
import moa.moabackend.domain.participation.domain.repository.PurchaseParticipantRepository
import moa.moabackend.domain.participation.presentation.dto.response.ParticipantDetailResponse
import moa.moabackend.domain.participation.presentation.dto.response.ParticipantResponse
import moa.moabackend.domain.payment.domain.repository.PaymentRepository
import moa.moabackend.domain.user.domain.exception.UserNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QueryParticipantService(
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val purchaseParticipantRepository: PurchaseParticipantRepository,
    private val paymentRepository: PaymentRepository
) {

    @Transactional(readOnly = true)
    fun queryList(groupPurchaseId: Long): List<ParticipantResponse> {
        val groupPurchase = groupPurchaseRepository.findByIdOrNull(groupPurchaseId)
            ?: throw GroupPurchaseNotFoundException

        val participants = purchaseParticipantRepository.findAllByGroupPurchase(groupPurchase)

        return participants.map { participant ->
            val payment = paymentRepository.findAllByGroupPurchaseAndStatus(groupPurchase, moa.moabackend.domain.payment.domain.PaymentStatus.PAID)
                .find { it.user.id == participant.user.id }

            ParticipantResponse(
                userId = participant.user.id,
                name = participant.user.name,
                email = participant.user.email,
                phoneNumber = participant.user.phoneNumber,
                quantity = participant.quantity,
                paymentAmount = payment?.amount ?: 0,
                status = payment?.status?.name ?: "UNKNOWN",
                orderedAt = participant.orderedAt
            )
        }
    }

    @Transactional(readOnly = true)
    fun queryDetail(groupPurchaseId: Long, userId: Long): ParticipantDetailResponse {
        val groupPurchase = groupPurchaseRepository.findByIdOrNull(groupPurchaseId)
            ?: throw GroupPurchaseNotFoundException

        val participant = purchaseParticipantRepository.findByGroupPurchaseAndUser_Id(groupPurchase, userId)
            ?: throw UserNotFoundException

        val payment = paymentRepository.findAllByGroupPurchaseAndStatus(groupPurchase, moa.moabackend.domain.payment.domain.PaymentStatus.PAID)
            .find { it.user.id == userId }

        return ParticipantDetailResponse(
            userId = participant.user.id,
            name = participant.user.name,
            email = participant.user.email,
            phoneNumber = participant.user.phoneNumber,
            status = payment?.status?.name ?: "UNKNOWN",
            shippingAddress = participant.shippingAddress,
            quantity = participant.quantity,
            orderedAt = participant.orderedAt,
            paymentAmount = payment?.amount ?: 0
        )
    }
}
