package moa.moabackend.domain.grouppurchase.service

import moa.moabackend.domain.grouppurchase.domain.GroupPurchaseStatus
import moa.moabackend.domain.grouppurchase.domain.exception.GroupPurchaseNotFoundException
import moa.moabackend.domain.grouppurchase.domain.repository.GroupPurchaseRepository
import moa.moabackend.domain.participation.domain.repository.PurchaseParticipantRepository
import moa.moabackend.domain.payment.domain.PaymentStatus
import moa.moabackend.domain.payment.domain.repository.PaymentRepository
import moa.moabackend.domain.user.service.facade.UserFacade
import moa.moabackend.infra.portone.PortOneClient
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CancelGroupPurchaseByAdminService(
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val paymentRepository: PaymentRepository,
    private val purchaseParticipantRepository: PurchaseParticipantRepository,
    private val userFacade: UserFacade,
    private val portOneClient: PortOneClient
) {

    @Transactional
    fun execute(groupPurchaseId: Long) {
        val admin = userFacade.getCurrentUser()
        val groupPurchase = groupPurchaseRepository.findByIdOrNull(groupPurchaseId)
            ?: throw GroupPurchaseNotFoundException

        
        if (groupPurchase.user.id != admin.id) {
            throw RuntimeException("본인의 공동구매 게시글만 취소할 수 있습니다.")
        }

        
        if (groupPurchase.status == GroupPurchaseStatus.CANCELLED) {
            throw RuntimeException("이미 취소된 공동구매입니다.")
        }

        
        val payments = paymentRepository.findAllByGroupPurchaseAndStatus(groupPurchase, PaymentStatus.PAID)
        val accessToken = portOneClient.getAccessToken()

        payments.forEach { payment ->
            portOneClient.refund(
                impUid = payment.impUid!!,
                accessToken = accessToken,
                reason = "판매자에 의한 공동구매 취소",
                amount = payment.amount
            )
            payment.cancel()
        }

        
        val participants = purchaseParticipantRepository.findAllByGroupPurchase(groupPurchase)
        purchaseParticipantRepository.deleteAll(participants)

        
        groupPurchase.status = GroupPurchaseStatus.CANCELLED
        groupPurchaseRepository.save(groupPurchase)

        
        applyPenalty(admin)
    }

    private fun applyPenalty(admin: moa.moabackend.domain.user.domain.User) {
        admin.penaltyCount += 1
        
        val now = LocalDateTime.now()
        when (admin.penaltyCount) {
            1 -> {
                
            }
            2 -> {
                
                admin.suspendedUntil = now.plusDays(3)
            }
            3 -> {
                
                admin.suspendedUntil = now.plusDays(3)
            }
            else -> {
                
                admin.suspendedUntil = now.plusYears(100)
            }
        }
    }
}
