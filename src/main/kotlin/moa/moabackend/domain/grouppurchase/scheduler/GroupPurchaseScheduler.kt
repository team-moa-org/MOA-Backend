package moa.moabackend.domain.grouppurchase.scheduler

import moa.moabackend.domain.grouppurchase.domain.GroupPurchaseStatus
import moa.moabackend.domain.grouppurchase.domain.repository.GroupPurchaseRepository
import moa.moabackend.domain.payment.domain.PaymentStatus
import moa.moabackend.domain.payment.domain.repository.PaymentRepository
import moa.moabackend.infra.portone.PortOneClient
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class GroupPurchaseScheduler(
    private val groupPurchaseRepository: GroupPurchaseRepository,
    private val paymentRepository: PaymentRepository,
    private val portOneClient: PortOneClient
) {

    @Scheduled(cron = "0 * * * * *") // 매 분 0초에 실행
    @Transactional
    fun checkGroupPurchaseDeadline() {
        val now = LocalDateTime.now()
        val expiredPurchases = groupPurchaseRepository.findAllByStatusAndDeadlineBefore(
            GroupPurchaseStatus.RECRUITING, now
        )

        expiredPurchases.forEach { purchase ->
            if (purchase.currentCount < purchase.targetCount) {
                // 1. 최소 인원 미달 시 취소 및 전체 환불
                purchase.status = GroupPurchaseStatus.CANCELLED
                refundAllParticipants(purchase)
            } else {
                // 2. 최소 인원 달성 시 완료 처리 및 차액 부분 환불
                purchase.status = GroupPurchaseStatus.COMPLETED
                refundDifferenceToParticipants(purchase)
            }
        }
    }

    private fun refundAllParticipants(purchase: moa.moabackend.domain.grouppurchase.domain.GroupPurchase) {
        val payments = paymentRepository.findAllByGroupPurchaseAndStatus(purchase, PaymentStatus.PAID)
        val accessToken = portOneClient.getAccessToken()

        payments.forEach { payment ->
            try {
                payment.impUid?.let { impUid ->
                    portOneClient.refund(impUid, accessToken, "공동구매 인원 미달로 인한 자동 환불")
                    payment.cancel()
                }
            } catch (e: Exception) {
                println("Full refund failed for payment ${payment.merchantUid}: ${e.message}")
            }
        }
    }

    private fun refundDifferenceToParticipants(purchase: moa.moabackend.domain.grouppurchase.domain.GroupPurchase) {
        val finalPrice = purchase.getCurrentPrice()
        val payments = paymentRepository.findAllByGroupPurchaseAndStatus(purchase, PaymentStatus.PAID)
        val accessToken = portOneClient.getAccessToken()

        payments.forEach { payment ->
            val refundAmount = payment.amount - finalPrice
            if (refundAmount > 0) {
                try {
                    payment.impUid?.let { impUid ->
                        portOneClient.refund(
                            impUid = impUid,
                            accessToken = accessToken,
                            reason = "공동구매 최종 할인 차액 환불",
                            amount = refundAmount
                        )
                        // 참고: 부분 환불 후에도 상태는 PAID로 유지 (필요 시 부분 환불 완료 필드 추가 가능)
                    }
                } catch (e: Exception) {
                    println("Partial refund failed for payment ${payment.merchantUid}: ${e.message}")
                }
            }
        }
    }
}
